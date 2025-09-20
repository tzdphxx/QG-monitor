package com.qg.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.FrontendError;
import com.qg.domain.Responsibility;
import com.qg.domain.Role;
import com.qg.domain.Users;
import com.qg.mapper.*;
import com.qg.utils.WechatAlertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.qg.repository.RepositoryConstants.*;
import static com.qg.utils.Constants.USER_ROLE_ADMIN;

@Slf4j
@Repository
public abstract class FrontendErrorFatherRepository extends StatisticsDataRepository<FrontendError> {

    @Autowired
    protected FrontendErrorMapper FrontendErrorMapper;
    @Autowired
    protected AlertRuleMapper alertRuleMapper;
    @Autowired
    protected WechatAlertUtil wechatAlertUtil;
    @Autowired
    protected ProjectMapper projectMapper;
    @Autowired
    protected ResponsibilityMapper responsibilityMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UsersMapper usersMapper;

    protected abstract boolean saveNotification(List<Long> alertReceiverID,FrontendError error);


    @Override
    protected long getTtlMinutes() {
        return TTL_MINUTES.getAsInt();
    }

    @Override
    protected void saveToDatabase(FrontendError error) {
        try {
            FrontendErrorMapper.insert(error);
        } catch (Exception e) {
            log.error("移动端错误统计失败,项目ID: {}: {}", error.getProjectId(), e.getMessage());
        }
    }

    @Override
    protected String generateUniqueKey(FrontendError error) {
        return String.format("%s:%s:%s",
                FRONTEND_ERROR_PREFIX.getAsString(),
                error.getProjectId(),
                error.getErrorType()
        );
    }

    @Override
    protected void incrementEvent(FrontendError error) {
        error.incrementEvent();
    }

    protected abstract boolean shouldAlert(String redisKey, FrontendError error);

    protected abstract String generateAlertMessage(FrontendError error);

    /**
     * 发送微信企业机器人告警
     * @param error
     */
    public void sendWechatAlert(FrontendError error) {
        log.info("判断是否达到阈值！");
        String redisKey = generateUniqueKey(error);
        String[] data = redisKey.split(":");

        Integer threshold =  alertRuleMapper
                .selectThresholdByProjectAndErrorType(error.getProjectId(), error.getErrorType(), "frontend");


        if(threshold == null){
            log.info("没有设置阈值");
            threshold = DEFAULT_THRESHOLD.getAsInt();
        }


        int currentCount = error.getEvent();

        if(currentCount >= threshold) {
            log.info("发送前端告警");

            //查询同类错误的最新记录
            LambdaQueryWrapper<FrontendError> queryWrapper4 = new LambdaQueryWrapper<>();
            queryWrapper4.eq(FrontendError::getProjectId, error.getProjectId())
                    .eq(FrontendError::getMessage,error.getMessage())
                    .eq(FrontendError::getStack,error.getStack())
                    .eq(FrontendError::getErrorType, error.getErrorType())
                    .eq(FrontendError::getSessionId, error.getSessionId())
                    .orderByDesc(FrontendError::getTimestamp)
                    .last("LIMIT 1");

            FrontendError latestError = FrontendErrorMapper.selectOne(queryWrapper4);
            // 如果存在同类错误记录，检查时间间隔
            if (latestError != null) {
                log.info("最新错误：{}", latestError);
                long timeDiff = Timestamp.valueOf(error.getTimestamp()).getTime()
                        - Timestamp.valueOf(latestError.getTimestamp()).getTime();
                log.info("当前错误时间: {}, 最新错误时间: {}",
                        error.getTimestamp(),
                        latestError.getTimestamp());
                long minutesDiff = timeDiff / (1000 * 60);
                log.info("计算出的时间差(ms): {}, 分钟差: {}",
                        timeDiff,
                        minutesDiff);// 转换为分钟

                // 如果时间间隔小于40分钟，只更新event次数
                if (minutesDiff < 40) {
                    log.info("小于40分钟");
                    latestError.setEvent(latestError.getEvent() + error.getEvent());
                    //latestError.setTimestamp(error.getTimestamp()); // 更新时间戳为最新时间
                    FrontendErrorMapper.updateById(latestError);
                    log.info("时间间隔小于40分钟，只更新错误次数，errorId:{}", latestError.getId());
                }
                else{
                    log.info("大于40分钟");
                    //插入新的错误信息
                    log.info("存储错误数据: {}",error);
                    FrontendErrorMapper.insert(error);
                }
            }else{
                log.info("没有找到错误信息，存储错误数据: {}",error);
                FrontendErrorMapper.insert(error);
            }

            //删除缓存数据
            removeError(error);

            //查询错误id
            LambdaQueryWrapper<FrontendError> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(FrontendError::getProjectId,error.getProjectId())
                    .eq(FrontendError::getErrorType,error.getErrorType())
                    .eq(FrontendError::getMessage,error.getMessage())
                    .eq(FrontendError::getStack,error.getStack())
                    .eq(FrontendError::getSessionId,error.getSessionId())
                    .orderByDesc(FrontendError::getTimestamp)
                    .last("LIMIT 1");  // 只取第一条记录

            error = FrontendErrorMapper.selectOne(queryWrapper2);
            log.info("errorId:{}",error.getId());

            String webhookUrl = getWebhookUrl(error.getProjectId());

            if (shouldAlert(generateUniqueKey(error), error)) {
                String message = generateAlertMessage(error);

                //查看该错误类型是否被委派
                LambdaQueryWrapper<Responsibility> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Responsibility::getErrorType,error.getErrorType())
                        .eq(Responsibility::getProjectId,error.getProjectId());

                Responsibility responsibility = responsibilityMapper.selectOne(queryWrapper);


                if(responsibility != null){
                    log.info("该错误已经被委派" );

                    //更新responsibility中的errorId
                    LambdaQueryWrapper<Responsibility> queryWrapper5 = new LambdaQueryWrapper<>();
                    queryWrapper5.eq(Responsibility::getProjectId, error.getProjectId())
                            .eq(Responsibility::getPlatform, "frontend")
                            .eq(Responsibility::getErrorType, error.getErrorType());
                    Responsibility responsibility1 = responsibilityMapper.selectOne(queryWrapper5);
                    responsibility1.setErrorId(error.getId());
                    responsibilityMapper.update(responsibility1, queryWrapper5);

                    //标记该错误为未解决
                    responsibility.setIsHandle(UN_HANDLED);
                    responsibility.setUpdateTime(LocalDateTime.now());
                    responsibilityMapper.update(responsibility,queryWrapper);

                    //存储进通知表
                    List<Long> alertReceiverID = Arrays.asList(responsibility.getResponsibleId());
                    boolean success = saveNotification(alertReceiverID,error);
                    if(!success){
                        log.error("保存通知进数据库失败！");
                    }

                    if (StrUtil.isBlank(webhookUrl)) {
                        log.warn("未找到对应的企业微信群机器人Webhook地址, 告警失败");
                        return;
                    }

                    //获取负责人手机号码
                    LambdaQueryWrapper<Users> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(Users::getId,responsibility.getResponsibleId());

                    Users responsibleUser = usersMapper.selectOne(queryWrapper1);
                    String responsiblePhone = responsibleUser.getPhone();
                    log.info("发送告警给: {}", responsiblePhone);

                    List<String> alertReceiver = Arrays.asList(responsiblePhone);

                    // TODO: 实现从数据库查找负责人手机号逻辑
                    List<String> alertReceivers = Collections.singletonList("@all");
                    wechatAlertUtil.sendAlert(webhookUrl, message, alertReceiver);
                }else{
                    log.info("该错误未被委派！");
                    //未指派的错误找到管理员
                    LambdaQueryWrapper<Role> queryWrapper3 = new LambdaQueryWrapper<>();
                    queryWrapper3.eq(Role::getProjectId,error.getProjectId())
                            .eq(Role::getUserRole,USER_ROLE_ADMIN);
                    List<Role> roles = roleMapper.selectList(queryWrapper3);

                    // 2. 提取角色中的用户ID集合
                    List<Long> userIds = roles.stream()
                            .map(Role::getUserId)  // 假设Role中有getUserId()
                            .collect(Collectors.toList());

                    //3、保存通知进数据库
                    boolean success = saveNotification(userIds,error);
                    if(!success){
                        log.error("保存通知进数据库失败！");
                    }

                    if (StrUtil.isBlank(webhookUrl)) {
                        log.warn("未找到对应的企业微信群机器人Webhook地址, 告警失败");
                        return;
                    }

                    //4、获取电话号码 发送警告
                    LambdaQueryWrapper<Users> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.in(Users::getId,userIds);
                    List<Users> users = usersMapper.selectList(queryWrapper1);

                    List<String> alertReceivers = users.stream()
                            .map(Users::getPhone)
                            .collect(Collectors.toList());
                    log.info("发送告警给: {}", alertReceivers);

                    wechatAlertUtil.sendAlert(webhookUrl, message, alertReceivers);

                }
            }else{
                //查看该错误类型是否被委派
                LambdaQueryWrapper<Responsibility> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Responsibility::getErrorType,error.getErrorType())
                        .eq(Responsibility::getProjectId,error.getProjectId());

                Responsibility responsibility = responsibilityMapper.selectOne(queryWrapper);

                if(responsibility != null) {
                    log.info("该错误已经被委派");

                    //更新responsibility中的errorId
                    LambdaQueryWrapper<Responsibility> queryWrapper5 = new LambdaQueryWrapper<>();
                    queryWrapper5.eq(Responsibility::getProjectId, error.getProjectId())
                            .eq(Responsibility::getPlatform, "frontend")
                            .eq(Responsibility::getErrorType, error.getErrorType());
                    Responsibility responsibility1 = responsibilityMapper.selectOne(queryWrapper5);
                    responsibility1.setErrorId(error.getId());
                    responsibilityMapper.update(responsibility1, queryWrapper5);
                }
            }
        }

    }

    /**
     * 获取企业机器人webhook
     * @param projectId
     * @return
     */
    protected String getWebhookUrl(String projectId) {
        // 从数据库查询webhook
        return projectMapper.selectWebhookByProjectId(projectId);
    }

    protected void removeError(FrontendError entity){
        // 1. 生成唯一键(与statistics方法一致)
        String key = generateUniqueKey(entity);

        // 2. 从Redis删除
        stringRedisTemplate.delete(key);

        // 3. 从内存缓存删除
        cacheMap.remove(key);
        log.info("缓存已被删除！");
    }



}