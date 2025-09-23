package com.qg.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qg.common.domain.dto.EncryptionResultDTO;
import com.qg.common.domain.po.Code;

import com.qg.common.domain.po.Result;
import com.qg.common.utils.CryptoUtils;
import com.qg.common.utils.RedisConstants;
import com.qg.common.utils.RegexUtils;
import com.qg.user.utils.EmailService;
import com.qg.user.domain.dto.UsersDTO;

import com.qg.common.domain.po.Users;
import com.qg.user.mapper.UsersMapper;
import com.qg.user.service.UsersService;
import com.qg.user.utils.HashSaltUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UsersMapper usersMapper;

    @Value("${rsa.key-pairs.pair1.public-key}")
    private String rsaPublicKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static String DEFAULT_AVATAR_URL = "http://47.113.224.195:30420/uploads/images/bb6af913-f710-4fdb-81ba-38a3cefeec77.png";

    @Override
    public Map<String, Object> loginByPassword(String email, String password) {

        LambdaQueryWrapper<Users> lqw = new LambdaQueryWrapper<>();

        lqw.eq(Users::getEmail, email);
        log.info("用户登录邮箱：{}", email);

        Users loginUser = usersMapper.selectOne(lqw);

        log.info("登录用户：{}", loginUser);


        if (loginUser == null || !HashSaltUtil.verifyHashPassword(password, loginUser.getPassword())) {
            return null;
        }
        //token验证
        String token = UUID.randomUUID().toString();
        UsersDTO userDto = BeanUtil.copyProperties(loginUser, UsersDTO.class);
        System.out.println(userDto);
        Map<String, Object> usermap = BeanUtil.beanToMap(userDto, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fileName, fileValue) -> fileValue.toString()));


        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, usermap);
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", loginUser);
        return map;
    }

    @Override
    public Result register(Users user, String code) {

        // 判断参数非空
        if (user == null || code == null) {
            System.out.println("存在空参");
            return new Result(Code.BAD_REQUEST, "存在空参");

        }
        // 获取用户邮箱，并做正则验证
        String email = user.getEmail().trim();
        if (RegexUtils.isEmailInvalid(email)) {
            System.out.println("邮箱格式错误");
            return new Result(Code.BAD_REQUEST, "邮箱格式错误");
        }

        LambdaQueryWrapper<Users> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Users::getEmail, email);
        // 判断邮箱是否已经被注册
        Users one = usersMapper.selectOne(lqw);
        if (one != null) {
            System.out.println("该邮箱已被注册");
            return new Result(Code.CONFLICT, "该邮箱已被注册！");
        }

        String phone = user.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            System.out.println("手机号不能为空");
            return new Result(Code.BAD_REQUEST, "手机号不能为空");
        }

        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getPhone, phone);

        if (usersMapper.selectCount(queryWrapper) > 0) {
            System.out.println("该手机号已被注册");
            return new Result(Code.CONFLICT, "该手机号已被注册！");
        }

        // 再查看验证码是否正确
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + email);
        if (cacheCode == null || !cacheCode.equals(code)) {
            System.out.println(cacheCode);
            System.out.println("用户输入的验证码：" + code);
            System.out.println("验证码错误");
            return new Result(Code.NOT_FOUND, "验证码错误");
        }


        // 对密码进行加密处理
        user.setPassword(HashSaltUtil.creatHashPassword(user.getPassword()));

        // 自动生成一个初始姓名
        if (user.getUsername() == null || user.getUsername().trim().equals("")) {
            user.setUsername("用户：" + RandomUtil.randomString(6));
        }
        user.setAvatar(DEFAULT_AVATAR_URL);
        // 向数据库中添加数据
        if (usersMapper.insert(user) != 1) {
            System.out.println("注册失败，请稍后重试");
            return new Result(Code.INTERNAL_ERROR, "注册失败，请稍后重试");
        }
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getEmail, email);
        user = usersMapper.selectOne(wrapper);
        // 注册成功后删除验证码
        stringRedisTemplate.delete(RedisConstants.LOGIN_CODE_KEY + user.getEmail());
        // 随机生成token，作为的登录令牌
        String token = cn.hutool.core.lang.UUID.randomUUID().toString(true);
        // 7.2.将User对象转换为HashMap存储
        UsersDTO userDTO = BeanUtil.copyProperties(user, UsersDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fileName, fileValue) -> fileValue.toString()));
        // 7.3.存储
        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 7.4.设置token有效期
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("user", userDTO);
        map.put("token", token);
        EncryptionResultDTO encryptionResultDTO = null;
        try {
            String jsonData = objectMapper.writeValueAsString(map);//将map类型的数据转化为json字符串
            log.info("加密前的JSON: {}", jsonData);

            encryptionResultDTO = CryptoUtils.encryptWithAESAndRSA(jsonData, rsaPublicKey);

            log.info("加密后的JSON: {}", encryptionResultDTO.getEncryptedData());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(Code.CREATED, encryptionResultDTO, "恭喜你，注册成功！");
    }

    @Override
    public Result sendCodeByEmail(String email) {
        // 判断是否是无效邮箱地址
        if (RegexUtils.isEmailInvalid(email)) {
            return new Result(Code.BAD_REQUEST, "邮箱格式错误");
        }
        // 符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
        System.out.println("验证码：" + code);

        // 发送验证码到邮箱
        // 3. 调用邮件工具类发送验证码
        boolean sendSuccess = emailService.sendSimpleEmail(
                email,
                "你的验证码",
                "尊敬的用户，你的验证码是：" + code + "，有效期2分钟。"
        );
        if (sendSuccess) {
            System.out.println("已发送验证码到邮箱到 " + email);
            // 保存验证码到 redis 中
            stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + email, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
            return new Result(Code.SUCCESS, "验证码已发送至邮箱，请注意查收");
        } else {
            // 发送失败（可能是邮箱不存在或其他原因）
            return new Result(Code.BAD_REQUEST, "验证码发送失败，请检查邮箱地址是否正确");
        }
    }

    //用户获取个人信息
    @Override
    public Result getUser(Long id) {
        if (id == null) {
            return new Result(Code.BAD_REQUEST, "用户ID不能为空");
        }
        Users user = usersMapper.selectById(id);
        if (user == null) {
            return new Result(Code.NOT_FOUND, "用户不存在");
        }
        user.setPassword(null);
        user.setIsDeleted(null);

        //加密
        String jsonData = null;//将map类型的数据转化为json字符串
        EncryptionResultDTO encryptionResultDTO = null;
        try {
            jsonData = objectMapper.writeValueAsString(user);

            log.info("加密前的JSON: {}", jsonData);

            encryptionResultDTO = CryptoUtils.encryptWithAESAndRSA(jsonData, rsaPublicKey);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("加密后的JSON: {}", encryptionResultDTO.getEncryptedData());
        return new Result(Code.SUCCESS, encryptionResultDTO, "获取用户信息成功");
    }


    @Override
    public Result findPassword(Users user, String code) {
        if (user == null || user.getEmail() == null || user.getPassword() == null || code == null) {
            log.error("参数错误");
            return new Result(Code.BAD_REQUEST, "参数错误");
        }
        try {
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Users::getEmail, user.getEmail());
            Users oldUser = usersMapper.selectOne(queryWrapper);
            if (oldUser == null) {
                log.error("用户不存在");
                return new Result(Code.NOT_FOUND, "用户不存在");
            }

            String redisCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + user.getEmail());
            if (!code.equals(redisCode)) {
                log.error("验证码错误");
                return new Result(Code.BAD_REQUEST, "验证码错误");
            }

            LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Users::getEmail, user.getEmail())
                    .set(Users::getPassword, HashSaltUtil.creatHashPassword(user.getPassword()));
            int count = usersMapper.update(null, updateWrapper);

            if (count > 0) {
                log.info("修改密码成功，参数: {}", user);
                return new Result(Code.SUCCESS, "修改密码成功");
            } else {
                log.warn("修改密码失败，参数: {}", user);
                return new Result(Code.INTERNAL_ERROR, "修改密码失败");
            }


        } catch (Exception e) {
            log.error("修改密码失败", e);
            return new Result(Code.INTERNAL_ERROR, "修改密码失败");
        }
    }

    @Override
    public boolean updateAvatar(Long userId, String avatarUrl) {
        return usersMapper.updateAvatar(userId, avatarUrl) > 0;
    }

    @Override
    public Result updateUser(Users users) {
        Long id = users.getId();
        log.info("用户ID: {}", id);
        // 1. 先查询确保用户存在
        Users existingUser = usersMapper.selectById(id);
        log.info("用户：{}", existingUser);
        if (existingUser == null) {
            return new Result(Code.NOT_FOUND, "用户不存在");
        }

        users.setUsername(users.getUsername().trim());

        if (!StrUtil.isBlank(users.getUsername()) && users.getUsername().length() > 10) {
            return new Result(Code.BAD_REQUEST, "用户名不能为空且长度不能大于10");
        }


        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getId, id);

        int count = usersMapper.update(users, queryWrapper);
        if (count == 0) {
            return new Result(Code.NOT_FOUND, "用户数据修改失败");
        }

        Users user = usersMapper.selectById(id);
        user.setPassword(null);
        user.setIsDeleted(null);

        try {
            String jsonData = objectMapper.writeValueAsString(user);
            EncryptionResultDTO encryptionResultDTO = CryptoUtils.encryptWithAESAndRSA(jsonData, rsaPublicKey);
            return new Result(Code.SUCCESS, encryptionResultDTO, "更新用户信息成功");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public UsersDTO findUserById(Long id) {
        Users user = usersMapper.selectById(id);
        if (user == null) {
            return null;
        }
        return BeanUtil.copyProperties(user, UsersDTO.class);
    }

    @Override
    public List<UsersDTO> findUserByIds(Collection<Long> ids) {
        List<Users> users = usersMapper.selectBatchIds(ids);
        return BeanUtil.copyToList(users, UsersDTO.class);
    }

    /**
     * 批量查询用户
     *
     * @param userIds 用户id集合
     * @return 结果
     */
    @Override
    public List<Users> selectBatchIds(Set<Long> userIds) {
        return usersMapper.selectBatchIds(userIds);
    }

    /**
     * 批量查询用户
     * @param queryWrapper 查询器
     * @return  结果
     */
    @Override
    public List<Users> selectList(LambdaQueryWrapper<Users> queryWrapper) {
        return usersMapper.selectList(queryWrapper);
    }
}
