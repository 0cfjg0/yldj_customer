package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 地址薄 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
public interface IAddressBookService extends IService<AddressBook> {

    /**
     * 根据用户id和城市编码获取地址
     *
     * @param userId 用户id
     * @param cityCode 城市编码
     * @return 地址编码
     */
    List<AddressBookResDTO> getByUserIdAndCity(Long userId, String cityCode);


    void saveAddress(AddressBookUpsertReqDTO reqDTO);

    PageResult<AddressBook> selectPage(AddressBookPageQueryReqDTO reqDTO);

    void updateAddress(Long id,AddressBookUpsertReqDTO reqDTO);

    void deleteAddress(List<Long> ids);

    void setDefault(Long id, Short type);

    AddressBook getDetailAddress(Long id);

    AddressBook getDefaultAddress();
}
