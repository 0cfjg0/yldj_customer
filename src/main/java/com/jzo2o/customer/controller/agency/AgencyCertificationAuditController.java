package com.jzo2o.customer.controller.agency;


import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 机构认证审核表 前端控制器
 * </p>
 *
 * @author cfjg
 * @since 2024-07-25
 */
@RestController
@RequestMapping("/agency/agency-certification-audit")
@Api(tags = "机构端 - 认证审核接口")
public class AgencyCertificationAuditController {

    @Resource
    IAgencyCertificationAuditService agencyCertificationAuditService;

    /**
     * 提交审核
     * @param reqDTO
     */
    @PostMapping("")
    @ApiOperation("提交审核")
    public void submitAudit(@RequestBody AgencyCertificationAuditAddReqDTO reqDTO){
        agencyCertificationAuditService.submitAudit(reqDTO);
    }

    /**
     * 查看被驳回原因
     */
    @GetMapping("/rejectReason")
    @ApiOperation("查看被驳回原因")
    public String getReason(){
        return agencyCertificationAuditService.getReason();
    }
}
