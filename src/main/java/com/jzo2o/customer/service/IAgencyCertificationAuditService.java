package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AgencyCertificationAudit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;

/**
 * <p>
 * 机构认证审核表 服务类
 * </p>
 *
 * @author cfjg
 * @since 2024-07-25
 */
public interface IAgencyCertificationAuditService extends IService<AgencyCertificationAudit> {

    void submitAudit(AgencyCertificationAuditAddReqDTO reqDTO);

    String getReason();

    PageResult<AgencyCertificationAuditResDTO> auditAgencyPage(AgencyCertificationAuditPageQueryReqDTO reqDTO);

    void auditAgency(CertificationAuditReqDTO reqDTO);
}
