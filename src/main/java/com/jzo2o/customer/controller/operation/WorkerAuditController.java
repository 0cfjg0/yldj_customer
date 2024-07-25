package com.jzo2o.customer.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operation/worker-certification-audit")
@Api(tags = "运营端 - 服务人员认证信息相关接口")
public class WorkerAuditController {
    @Resource
    IWorkerCertificationAuditService workerCertificationAuditService;

    /**
     * 审核服务人员认证接口
     * @param reqDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("审核服务人员认证查询接口")
    public PageResult<WorkerCertificationAuditResDTO> auditAgencyPage(WorkerCertificationAuditPageQueryReqDTO reqDTO){
        return workerCertificationAuditService.auditAgencyPage(reqDTO);
    }

    /**
     * 审核服务人员信息
     */
    @PutMapping("/audit/{id}")
    @ApiOperation("审核服务人员认证接口")
    public void auditWorker(@RequestBody CertificationAuditReqDTO reqDTO,@PathVariable Long id){
        reqDTO.setId(id);
        workerCertificationAuditService.auditWorker(reqDTO);
    }
}