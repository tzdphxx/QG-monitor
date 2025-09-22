package com.qg.alert.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qg.common.domain.po.Responsibility;
import com.qg.alert.service.ResponsibilityService;
import com.qg.common.domain.po.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "责任链管理")
@RestController
@RequestMapping("/responsibilities")
public class ResponsibilityController {
    @Autowired
   private ResponsibilityService responsibilityService;

    /**
    * 添加或修改责任
    * @param responsibility
    * @return
    */
   @PostMapping
   public Result addResponsibility(@RequestBody Responsibility responsibility)
   {
      return responsibilityService.addResponsibility(responsibility);
   }

    /**
     * 获取项目责任列表
     * @param projectId
     * @return
     */
   @GetMapping
   public Result getResponsibilityList(@RequestParam String projectId)
   {
      return responsibilityService.getResponsibilityList(projectId);
   }

    /**
     * 获取个人被委派的责任
     * @param responsibleId
     * @return
     */
   @GetMapping("/selectByRespId")
    public Result selectByRespId(@RequestParam Long responsibleId) {
       return responsibilityService.selectByRespId(responsibleId);
   }

    /**
     * 责任人修改
     * @param responsibility
     * @return
     */
   @PutMapping
    public Result updateResponsibility(@RequestBody Responsibility responsibility){
       return responsibilityService.updateResponsibility(responsibility);
   }

    /**
     * 责任删除
     * @param id
     * @return
     */
   @DeleteMapping
    public Result deleteResponsibility(@RequestParam Long id){
       return responsibilityService.deleteResponsibility(id);
   }


    /**
     * 获取个人被委派的错误
     * @param responsibleId
     * @return
     */
   @GetMapping("/selectResponsibleError")
    public Result selectResponsibleError(@RequestParam String projectId, @RequestParam Long responsibleId,
                                         @RequestParam(required = false) String errorType, @RequestParam(required = false) String platform) {
       return responsibilityService.selectResponsibleError(projectId, responsibleId, errorType, platform);
   }

    /**
     * 修改处理状态
     * @param responsibility
     * @return
     */
   @PutMapping("/updateHandleStatus")
    public Result updateHandleStatus(@RequestBody Responsibility responsibility) {
       return responsibilityService.updateHandleStatus(responsibility);
   }

    /**
     * 查询处理状态
     * @param projectId
     * @param errorType
     * @param platform
     * @return
     */
   @GetMapping("/selectHandleStatus")
    public Result selectHandleStatus(@RequestParam String projectId, @RequestParam String errorType, @RequestParam String platform) {
       return responsibilityService.selectHandleStatus(projectId, errorType, platform);
   }

   @DeleteMapping("/deleteUserId")
    public int deleteUserId(@RequestParam Long userId) {
        return responsibilityService.deleteUserId(userId);
    }


    @GetMapping("/getResponsibilityByWrapper")
    public Responsibility getResponsibilityByWrapper(@RequestParam LambdaQueryWrapper<Responsibility> queryWrapper) {
        return responsibilityService.getResponsibilityByWrapper(queryWrapper);
    }

    @PutMapping("/updateResponsibilityByWrapper")
    public Integer updateResponsibilityByWrapper(@RequestBody Responsibility responsibility, @RequestParam LambdaQueryWrapper<Responsibility> queryWrapper) {
        return responsibilityService.updateResponsibilityByWrapper(responsibility, queryWrapper);
    }


    @GetMapping("/getResponsibilityListByWrapper")
    public List<Responsibility> getResponsibilityListByWrapper(@RequestParam LambdaQueryWrapper<Responsibility> queryWrapper) {
        return responsibilityService.getResponsibilityListByWrapper(queryWrapper);

}
