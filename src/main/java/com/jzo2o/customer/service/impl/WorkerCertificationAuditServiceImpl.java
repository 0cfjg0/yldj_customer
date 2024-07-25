package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AgencyCertificationAudit;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.mapper.WorkerCertificationAuditMapper;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务人员认证审核表 服务实现类
 * </p>
 *
 * @author cfjg
 * @since 2024-07-25
 */
@Service
public class WorkerCertificationAuditServiceImpl extends ServiceImpl<WorkerCertificationAuditMapper, WorkerCertificationAudit> implements IWorkerCertificationAuditService {

    private static final Integer DONE = 1;

    @Override
    public void submitAudit(WorkerCertificationAuditAddReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        //提交审核
        WorkerCertificationAudit workerAudit = BeanUtil.toBean(reqDTO, WorkerCertificationAudit.class);
        workerAudit.setAuditorId(UserContext.currentUserId());
        boolean flag = this.save(workerAudit);
        if(!flag){
            throw new ForbiddenOperationException("提交审核失败");
        }

    }

    @Override
    public String getReason() {
        //查询当前用户
        Long userId = UserContext.currentUserId();

        //查询拒绝原因
        LambdaQueryWrapper<WorkerCertificationAudit> wrapper = Wrappers.<WorkerCertificationAudit>lambdaQuery().eq(WorkerCertificationAudit::getAuditorId, userId);
        WorkerCertificationAudit res = this.getOne(wrapper);

        return res.getRejectReason();
    }

    @Override
    public PageResult<WorkerCertificationAuditResDTO> auditAgencyPage(WorkerCertificationAuditPageQueryReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        Page<WorkerCertificationAudit> page = PageUtils.parsePageQuery(reqDTO, WorkerCertificationAudit.class);
        LambdaQueryWrapper<WorkerCertificationAudit> wrapper = Wrappers.<WorkerCertificationAudit>lambdaQuery()
                .eq(reqDTO.getAuditStatus() != null, WorkerCertificationAudit::getAuditStatus, reqDTO.getAuditStatus())
                .eq(reqDTO.getCertificationStatus() != null, WorkerCertificationAudit::getCertificationStatus, reqDTO.getCertificationStatus());
        this.page(page, wrapper);
        List<WorkerCertificationAuditResDTO> resList = page.getRecords().stream().map(item -> BeanUtil.toBean(item, WorkerCertificationAuditResDTO.class)).collect(Collectors.toList());

        //封装
        return PageResult.of(resList, Convert.toInt(page.getSize()),page.getPages(),page.getTotal());
    }

    @Override
    public void auditWorker(CertificationAuditReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        //审核
        LambdaUpdateWrapper<WorkerCertificationAudit> wrapper = Wrappers.<WorkerCertificationAudit>lambdaUpdate()
                .eq(WorkerCertificationAudit::getId, reqDTO.getId())
                .set(WorkerCertificationAudit::getCertificationStatus, reqDTO.getCertificationStatus())
                .set(WorkerCertificationAudit::getAuditStatus,DONE)
                .set(WorkerCertificationAudit::getRejectReason, reqDTO.getRejectReason());
        boolean flag = this.update(wrapper);

        if(!flag){
            throw new ForbiddenOperationException("审核错误");
        }
    }

}
