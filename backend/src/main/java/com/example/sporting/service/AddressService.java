package com.example.sporting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sporting.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {

    // 查询用户所有地址
    List<Address> getUserAddressList(Long userId);

    // 新增地址（自动处理默认地址）
    boolean addAddress(Address address);

    // 修改地址
    boolean updateAddress(Address address);

    // 删除地址
    boolean deleteAddress(Long id, Long userId);

    // 设置默认地址
    boolean setDefaultAddress(Long id, Long userId);

    // 获取默认地址
    Address getDefaultAddress(Long userId);
}