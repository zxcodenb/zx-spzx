package com.zx.controller;


import com.zx.AuthContextUtil;
import com.zx.service.SysUserService;
import com.zx.service.ValidateCodeService;
import com.zx.spzx.model.dto.system.LoginDto;
import com.zx.spzx.model.entity.system.SysUser;
import com.zx.spzx.model.vo.common.Result;
import com.zx.spzx.model.vo.common.ResultCodeEnum;
import com.zx.spzx.model.vo.system.LoginVo;
import com.zx.spzx.model.vo.system.ValidateCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "用户接口")
@RequestMapping("/admin/system/index")
public class IndexController {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ValidateCodeService validateCodeService;

    @Operation(summary = "登录接口")
    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {


        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);

    }

    @Operation(summary = "验证码接口")
    @GetMapping("/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {


        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();

        return Result.build(validateCodeVo, ResultCodeEnum.SUCCESS);
    }


    @Operation(summary = "用户信息接口")
    @GetMapping(value = "/getUserInfo")
    public Result<SysUser> getUserInfo() {


        return Result.build(AuthContextUtil.get(), ResultCodeEnum.SUCCESS);
    }


    @GetMapping(value = "logout")
    @Operation(summary = "退出登陆")
    public Result logOut(@RequestHeader(value = "token") String token) {

        sysUserService.logOut(token);

        return Result.build(null, ResultCodeEnum.SUCCESS);

    }


}
