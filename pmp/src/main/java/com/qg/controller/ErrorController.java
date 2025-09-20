package com.qg.controller;


import com.qg.domain.Result;
import com.qg.service.*;





@Tag(name = "错误信息")
@RestController
@RequestMapping("/errors")
public class ErrorController {

    @Autowired
    private ErrorService errorService;


    @Autowired
    private BackendResponsibilityService backendResponsibilityService;

    @Autowired
    private FrontendResponsibilityService frontendResponsibilityService;

    @Autowired
    private MobileResponsibilityService mobileResponsibilityService;

    @Autowired
    private AllErrorService allErrorService;


    /**
     * 根据条件查询错误信息
     * @param
     * @return
     */
    @GetMapping("/selectByCondition")
    public Result selectByCondition(@RequestParam String projectId,
                                    @RequestParam(required = false) String errorType, @RequestParam(required = false) String platform) {

        if (platform == null || platform.isEmpty()) {
            return allErrorService.selectByCondition(projectId, errorType);
        }
        switch (platform) {
            case "backend":
                return backendResponsibilityService.selectByCondition(projectId, errorType);
            case "frontend":
                return frontendResponsibilityService.selectByCondition(projectId, errorType);
            case "mobile":
                return mobileResponsibilityService.selectByCondition(projectId, errorType);
            default:
                return new Result(400, "不支持的平台类型");
        }
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Long id) {
        return allErrorService.selectById(id);
    }

    /**
     * 根据id和platform查询错误信息详情
     * @param errorId
     * @param platform
     * @return
     */
    @GetMapping("/selectErrorDetail")
    public Result selectErrorDetail(@RequestParam Long errorId, @RequestParam String platform) {
        return allErrorService.selectErrorDetail(errorId, platform);
    }
}
