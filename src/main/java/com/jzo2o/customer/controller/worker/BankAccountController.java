package com.jzo2o.customer.controller.worker;

import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.service.IBankAccountService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("BankAccountWorkerController")
@RequestMapping("/worker/bank-account")
@Api(tags = "服务端 - 银行账户接口")
public class BankAccountController {

    @Resource
    IBankAccountService bankAccountService;

    @PostMapping("")
    public void inOrUpAccount(@RequestBody BankAccountUpsertReqDTO reqDTO){
        bankAccountService.inOrUpAccount(reqDTO);
    }

    @GetMapping("/currentUserBankAccount")
    public BankAccount getCurrentAccount(){
        return bankAccountService.getCurrentAccount();
    }



}
