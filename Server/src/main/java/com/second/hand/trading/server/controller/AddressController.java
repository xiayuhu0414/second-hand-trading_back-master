package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.AddressModel;
import com.second.hand.trading.server.service.AddressService;
import com.second.hand.trading.server.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @GetMapping("/info")
    public  ResultVo getAddress(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                @RequestParam(value = "id",required = false) Long id){
        if(null==id){
            return ResultVo.success(addressService.getAddressByUser(Long.valueOf(shUserId)));
        }else {
            return ResultVo.success(addressService.getAddressById(id,Long.valueOf(shUserId)));
        }
    }

    @PostMapping("/add")
    public ResultVo addAddress(@CookieValue("shUserId")
                                   @NotNull(message = "登录异常 请重新登录")
                                   @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                               @RequestBody AddressModel addressModel){
        //从CookieValue中获取用户id
        addressModel.setUserId(Long.valueOf(shUserId));
        //通过地址表的实体类向数据库中添加新的数据
        if(addressService.addAddress(addressModel)){
            return ResultVo.success(addressModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @PostMapping("/update")
    public ResultVo updateAddress(@CookieValue("shUserId")
                               @NotNull(message = "登录异常 请重新登录")
                               @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                               @RequestBody AddressModel addressModel){
        //从CookieValue中获取用户id
        addressModel.setUserId(Long.valueOf(shUserId));
        //通过地址表的实体类向数据库中添加新的数据
        if(addressService.updateAddress(addressModel)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @PostMapping("/delete")
    public ResultVo deleteAddress(@CookieValue("shUserId")
                                  @NotNull(message = "登录异常 请重新登录")
                                  @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                  @RequestBody AddressModel addressModel){
        //从CookieValue中获取用户id
        addressModel.setUserId(Long.valueOf(shUserId));
        if(addressService.deleteAddress(addressModel)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }
}
