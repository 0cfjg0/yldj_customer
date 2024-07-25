package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.constants.AddressConstants;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.customer.mapper.AddressBookMapper;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.domain.CommonUser;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.customer.service.ICommonUserService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.redisson.MapWriterTask;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 地址薄 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Resource
    ICommonUserService commonUserService;

    @Override
    public List<AddressBookResDTO> getByUserIdAndCity(Long userId, String city) {

        List<AddressBook> addressBooks = lambdaQuery()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getCity, city)
                .list();
        if(CollUtils.isEmpty(addressBooks)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyToList(addressBooks, AddressBookResDTO.class);
    }

    @Override
    public void saveAddress(AddressBookUpsertReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        //获取当前用户
        Long userId = UserContext.currentUserId();

        //判重
        if(reqDTO.getIsDefault().equals(AddressConstants.IS_DEFAULT)){
            //删除原来的默认地址
            LambdaUpdateWrapper<AddressBook> wrapper = Wrappers.<AddressBook>lambdaUpdate()
                    .eq(AddressBook::getUserId, userId)
                    .eq(AddressBook::getIsDefault, AddressConstants.IS_DEFAULT)
                    .set(AddressBook::getIsDefault, AddressConstants.NOT_DEFAULT);
            boolean flag = this.update(wrapper);
        }

        //保存地址
        AddressBook addressBook = BeanUtil.toBean(reqDTO, AddressBook.class);
        addressBook.setUserId(userId);
        boolean flag = this.save(addressBook);
        if(!flag){
            throw new ForbiddenOperationException("保存失败");
        }
    }

    @Override
    public PageResult<AddressBook> selectPage(AddressBookPageQueryReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        //获取当前用户
        Long userId = UserContext.currentUserId();

        //分页查询
        Page<AddressBook> page = PageUtils.parsePageQuery(reqDTO, AddressBook.class);
        LambdaQueryWrapper<AddressBook> wrapper = Wrappers.<AddressBook>lambdaQuery()
                .eq(AddressBook::getUserId, userId);
        Page<AddressBook> resPage = this.page(page, wrapper);
        return PageResult.of(resPage.getRecords(), Convert.toInt(resPage.getSize()),resPage.getPages(),resPage.getTotal());
    }

    @Override
    public void updateAddress(Long id,AddressBookUpsertReqDTO reqDTO) {
        //参数校验
        if(ObjectUtil.isNull(id) || ObjectUtil.isEmpty(reqDTO)){
            throw new ForbiddenOperationException("参数异常");
        }

        //获取当前用户
        Long userId = UserContext.currentUserId();

        //校验用户
        CommonUser user = commonUserService.getById(userId);
        if(ObjectUtil.isEmpty(user)){
            throw new ForbiddenOperationException("没有对应用户");
        }

        //更新
        AddressBook addressBook = BeanUtil.toBean(reqDTO, AddressBook.class);
        addressBook.setUserId(userId);
        addressBook.setId(id);
        boolean flag = this.updateById(addressBook);
        if(!flag){
            throw new ForbiddenOperationException("更新失败");
        }
    }

    @Override
    public void deleteAddress(List<Long> ids) {
        //参数校验
        if(ObjectUtil.isEmpty(ids)){
            throw new ForbiddenOperationException("参数异常");
        }

        //获取当前用户
        Long userId = UserContext.currentUserId();

        //删除对应地址
        LambdaQueryWrapper<AddressBook> wrapper = Wrappers.<AddressBook>lambdaQuery().in(AddressBook::getId, ids).eq(AddressBook::getUserId, userId);
        boolean flag = this.remove(wrapper);

        if(!flag){
            throw new ForbiddenOperationException("删除失败");
        }
    }

    @Override
    public void setDefault(Long id, Short type) {
        //参数校验
        if(ObjectUtil.isNull(id) || ObjectUtil.isNull(type)){
            throw new ForbiddenOperationException("参数异常");
        }

        //获取当前用户
        Long userId = UserContext.currentUserId();

        //设置默认地址
        LambdaUpdateWrapper<AddressBook> wrapper = Wrappers.<AddressBook>lambdaUpdate().eq(AddressBook::getUserId,userId).eq(AddressBook::getId, id).set(AddressBook::getIsDefault, type);
        boolean flag = this.update(wrapper);
        if(!flag){
            throw new ForbiddenOperationException("更新失败");
        }

    }

    @Override
    public AddressBook getDetailAddress(Long id) {
        //参数校验
        if(ObjectUtil.isNull(id)){
            throw new ForbiddenOperationException("参数异常");
        }

        //获取当前用户
        Long userId = UserContext.currentUserId();

        //查询地址
        LambdaQueryWrapper<AddressBook> wrapper = Wrappers.<AddressBook>lambdaQuery().eq(AddressBook::getUserId, userId).eq(AddressBook::getId, id);
        AddressBook res = this.getOne(wrapper);
        if(ObjectUtil.isEmpty(res)){
            throw new ForbiddenOperationException("没有此地址");
        }
        return res;
    }

    @Override
    public AddressBook getDefaultAddress() {
        //获取当前用户
        Long userId = UserContext.currentUserId();

        //查询默认地址
        LambdaQueryWrapper<AddressBook> wrapper = Wrappers.<AddressBook>lambdaQuery().eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1);
        AddressBook address = this.getOne(wrapper);

        if(ObjectUtil.isEmpty(address)){
            return null;
        }

        return address;
    }


}
