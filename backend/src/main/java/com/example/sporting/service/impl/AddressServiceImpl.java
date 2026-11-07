package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Address;
import com.example.sporting.mapper.AddressMapper;
import com.example.sporting.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl
        extends ServiceImpl<AddressMapper, Address>
        implements AddressService {

    /**
     * 查询用户地址列表
     */
    @Override
    public List<Address> getUserAddressList(Long userId) {
        return lambdaQuery()
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreatedAt)
                .list();
    }

    /**
     * 新增地址（处理默认地址逻辑）
     */
    @Override
    public boolean addAddress(Address address) {

        // 如果设为默认 → 先清空该用户所有默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearUserDefault(address.getUserId());
        }

        return save(address);
    }

    /**
     * 更新地址
     */
    @Override
    public boolean updateAddress(Address address) {

        // 如果修改为默认地址
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearUserDefault(address.getUserId());
        }

        return updateById(address);
    }

    /**
     * 删除地址
     */
    @Override
    public boolean deleteAddress(Long id, Long userId) {
        return lambdaUpdate()
                .eq(Address::getId, id)
                .eq(Address::getUserId, userId)
                .remove();
    }

    /**
     * 设置默认地址
     */
    @Override
    public boolean setDefaultAddress(Long id, Long userId) {

        // 1. 清空所有默认
        clearUserDefault(userId);

        // 2. 设置当前为默认
        return lambdaUpdate()
                .eq(Address::getId, id)
                .eq(Address::getUserId, userId)
                .set(Address::getIsDefault, 1)
                .update();
    }

    /**
     * 获取默认地址
     */
    @Override
    public Address getDefaultAddress(Long userId) {
        return lambdaQuery()
                .eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, 1)
                .one();
    }

    /**
     * 清空用户所有默认地址（内部方法）
     */
    private void clearUserDefault(Long userId) {
        UpdateWrapper<Address> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", userId)
                .set("is_default", 0);
        update(wrapper);
    }
}