package com.jzo2o.customer.controller.open;

import com.jzo2o.common.constants.UserType;
import com.jzo2o.customer.model.dto.request.InstitutionRegisterReqDTO;
import com.jzo2o.customer.service.IServeProviderService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("openRegistController")
@RequestMapping("/open/serve-provider")
@Api(tags = "白名单接口 - 机构相关接口")
public class ServeProviderController{

    @Resource
    IServeProviderService serveProviderService;

    @PostMapping("institution/register")
    public void institutionRegister(@RequestBody InstitutionRegisterReqDTO reqdto){
        serveProviderService.regist(reqdto, UserType.INSTITUTION);
    }

}
