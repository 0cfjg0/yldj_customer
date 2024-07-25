package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;

/**
 * <p>
 * 服务人员认证审核表 服务类
 * </p>
 *
 * @author cfjg
 * @since 2024-07-25
 */
public interface IWorkerCertificationAuditService extends IService<WorkerCertificationAudit> {

    void submitAudit(WorkerCertificationAuditAddReqDTO reqDTO);

    String getReason();

    PageResult<WorkerCertificationAuditResDTO> auditAgencyPage(WorkerCertificationAuditPageQueryReqDTO reqDTO);

    void auditWorker(CertificationAuditReqDTO reqDTO);
}
