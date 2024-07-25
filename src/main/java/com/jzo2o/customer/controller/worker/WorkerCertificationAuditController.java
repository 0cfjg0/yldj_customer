package com.jzo2o.customer.controller.worker;


import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 服务人员认证审核表 前端控制器
 * </p>
 *
 * @author cfjg
 * @since 2024-07-25
 */
@RestController
@RequestMapping("/worker/worker-certification-audit")
@Api(tags = "服务端 - 认证审核接口")
public class WorkerCertificationAuditController {

    @Resource
    IWorkerCertificationAuditService workerCertificationAuditService;

    /**
     * 提交服务审核申请
     * @param reqDTO
     */
    @PostMapping("")
    @ApiOperation("服务审核提交接口")
    public void submitAudit(@RequestBody WorkerCertificationAuditAddReqDTO reqDTO){
        workerCertificationAuditService.submitAudit(reqDTO);
    }

    /**
     * 查询驳回原因
     */
    @GetMapping("/rejectReason")
    @ApiOperation("查询驳回原因接口")
    public String getReason(){
        return workerCertificationAuditService.getReason();
    }
}
