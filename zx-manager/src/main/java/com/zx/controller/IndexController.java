package com.zx.controller;


import com.zx.service.SysUserService;
import com.zx.service.ValidateCodeService;
import com.zx.spzx.model.dto.system.LoginDto;
import com.zx.spzx.model.vo.common.Result;
import com.zx.spzx.model.vo.common.ResultCodeEnum;
import com.zx.spzx.model.vo.system.LoginVo;
import com.zx.spzx.model.vo.system.ValidateCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public Result<LoginVo> login(@RequestBody LoginDto loginDto){

        LoginVo loginVo = sysUserService.login(loginDto) ;
        return Result.build(loginVo , ResultCodeEnum.SUCCESS) ;

    }



    @GetMapping("/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode(){

        ValidateCodeVo validateCodeVo =  validateCodeService.generateValidateCode();

        return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);
    }

}
