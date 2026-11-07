package com.example.sporting.controller;

import com.example.sporting.entity.Address;
import com.example.sporting.service.AddressService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/front/address")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontAddressController {

    @Resource
    private AddressService addressService;

    @GetMapping("/list")
    public R list(@RequestParam Long userId) {
        // 返回按默认地址优先排序的列表
        List<Address> list = addressService.lambdaQuery()
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .list();
        return R.ok(list);
    }

    @PostMapping("/add")
    public R add(@RequestBody Address address) {
        // 如果是该用户的第一个地址，强制设为默认
        long count = addressService.lambdaQuery().eq(Address::getUserId, address.getUserId()).count();
        if (count == 0) address.setIsDefault(1);

        if (Integer.valueOf(1).equals(address.getIsDefault())) {
            addressService.lambdaUpdate().set(Address::getIsDefault, 0)
                    .eq(Address::getUserId, address.getUserId()).update();
        }
        addressService.save(address);
        return R.ok("添加成功");
    }

    @PostMapping("/set-default")
    public R setDefault(@RequestBody Address params) {
        // 1. 先将所有设为 0
        addressService.lambdaUpdate().set(Address::getIsDefault, 0)
                .eq(Address::getUserId, params.getUserId()).update();
        // 2. 将指定 ID 设为 1
        addressService.lambdaUpdate().set(Address::getIsDefault, 1)
                .eq(Address::getId, params.getId()).update();
        return R.ok("设置默认成功");
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Address params) {
        addressService.removeById(params.getId());
        return R.ok("删除成功");
    }
}