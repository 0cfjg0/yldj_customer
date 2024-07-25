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
import com.jzo2o.customer.mapper.AgencyCertificationAuditMapper;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 机构认证审核表 服务实现类
 * </p>
 *
 * @author cfjg
 * @since 2024-07-25
 */
@Service
public class AgencyCertificationAuditServiceImpl extends ServiceImpl<AgencyCertificationAuditMapper, AgencyCertificationAudit> implements IAgencyCertificationAuditService {

    private static final Integer DONE = 1;

    @Override
    public void submitAudit(AgencyCertificationAuditAddReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        //提交审核
        AgencyCertificationAudit agencyAudit = BeanUtil.toBean(reqDTO, AgencyCertificationAudit.class);
        agencyAudit.setAuditorId(UserContext.currentUserId());
        boolean flag = this.save(agencyAudit);
        if(!flag){
            throw new ForbiddenOperationException("提交审核失败");
        }

    }

    @Override
    public String getReason() {
        //获取当前用户id
        Long userId = UserContext.currentUserId();

        //查询原因
        LambdaQueryWrapper<AgencyCertificationAudit> wrapper = Wrappers.<AgencyCertificationAudit>lambdaQuery().eq(AgencyCertificationAudit::getAuditorId, userId);
        AgencyCertificationAudit res = this.getOne(wrapper);

        return res.getRejectReason();

    }

    @Override
    public PageResult<AgencyCertificationAuditResDTO> auditAgencyPage(AgencyCertificationAuditPageQueryReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        Page<AgencyCertificationAudit> page = PageUtils.parsePageQuery(reqDTO, AgencyCertificationAudit.class);
        LambdaQueryWrapper<AgencyCertificationAudit> wrapper = Wrappers.<AgencyCertificationAudit>lambdaQuery()
                .eq(reqDTO.getAuditStatus() != null, AgencyCertificationAudit::getAuditStatus, reqDTO.getAuditStatus())
                .eq(reqDTO.getCertificationStatus() != null, AgencyCertificationAudit::getCertificationStatus, reqDTO.getCertificationStatus());
        this.page(page, wrapper);
        List<AgencyCertificationAuditResDTO> resList = page.getRecords().stream().map(item -> BeanUtil.toBean(item, AgencyCertificationAuditResDTO.class)).collect(Collectors.toList());

        //封装
        return PageResult.of(resList, Convert.toInt(page.getSize()),page.getPages(),page.getTotal());
    }

    @Override
    public void auditAgency(CertificationAuditReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        //审核
        LambdaUpdateWrapper<AgencyCertificationAudit> wrapper = Wrappers.<AgencyCertificationAudit>lambdaUpdate()
                .eq(AgencyCertificationAudit::getId, reqDTO.getId())
                .set(AgencyCertificationAudit::getCertificationStatus, reqDTO.getCertificationStatus())
                .set(AgencyCertificationAudit::getAuditStatus,DONE)
                .set(AgencyCertificationAudit::getRejectReason, reqDTO.getRejectReason());
        boolean flag = this.update(wrapper);

        if(!flag){
            throw new ForbiddenOperationException("审核错误");
        }
    }
}
