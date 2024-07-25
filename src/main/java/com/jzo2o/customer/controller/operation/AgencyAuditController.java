package com.jzo2o.customer.controller.operation;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AuditReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operation/agency-certification-audit")
@Api(tags = "运营端 - 审核机构认证信息相关接口")
public class AgencyAuditController {

    @Resource
    IAgencyCertificationAuditService agencyCertificationAuditService;

    /**
     * 审核服务人员认证接口
     * @param reqDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("审核服务人员认证接口")
    public PageResult<AgencyCertificationAuditResDTO> auditAgencyPage(AgencyCertificationAuditPageQueryReqDTO reqDTO){
        return agencyCertificationAuditService.auditAgencyPage(reqDTO);
    }

    /**
     * 审核机构信息
     */
    @PutMapping("/audit/{id}")
    @ApiOperation("审核机构认证接口")
    public void auditAgency(@RequestBody CertificationAuditReqDTO reqDTO,@PathVariable Long id){
        reqDTO.setId(id);
        agencyCertificationAuditService.auditAgency(reqDTO);
    }
}
