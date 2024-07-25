package com.jzo2o.customer.controller.consumer;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController("ConsumerAddressController")
@RequestMapping("/consumer/address-book")
@Api(tags = "用户端 - 用户地址相关接口")
public class ConsumerAddressController {

    @Resource
    IAddressBookService addressBookService;

    /**
     * 新增地址
     * @param reqDTO
     */
    @PostMapping("")
    @ApiOperation("新增地址接口")
    public void saveAddress(@RequestBody AddressBookUpsertReqDTO reqDTO){
        addressBookService.saveAddress(reqDTO);
    }

    /**
     * 分页查询地址
     * @param reqDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询地址接口")
    public PageResult<AddressBook> selecePage(AddressBookPageQueryReqDTO reqDTO){
        return addressBookService.selectPage(reqDTO);
    }

    /**
     * 编辑地址
     * @param id
     * @param reqDTO
     */
    @PutMapping("{id}")
    @ApiOperation("编辑地址接口")
    public void updateAddress(@PathVariable Long id,@RequestBody AddressBookUpsertReqDTO reqDTO){
        addressBookService.updateAddress(id,reqDTO);
    }

    /**
     * 删除地址
     * @param ids
     */
    @DeleteMapping("/batch")
    @ApiOperation("编辑地址接口")
    public void deleteAddress(@RequestBody List<Long> ids){
        addressBookService.deleteAddress(ids);
    }

    /**
     * 设置默认地址
     * @param id
     * @param flag
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址接口")
    public void setDefault(@RequestParam("id") Long id,@RequestParam("flag") Short flag){
        addressBookService.setDefault(id,flag);
    }

    /**
     * 查看地址详情
     */
    @GetMapping("/{id}")
    @ApiOperation("查看地址详情")
    public AddressBook getDetailAddress(@PathVariable Long id){
        return addressBookService.getDetailAddress(id);
    }

    /**
     * 获取默认地址
     */
    @GetMapping("/defaultAddress")
    @ApiOperation("获取默认地址")
    public AddressBook getDefaultAddress(){
        return addressBookService.getDefaultAddress();
    }
}
