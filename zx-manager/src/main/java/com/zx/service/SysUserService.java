package com.zx.service;

import com.zx.spzx.model.dto.system.LoginDto;
import com.zx.spzx.model.vo.system.LoginVo;

public interface SysUserService {

    /**
     * 根据用户名查询用户数据
     * @return
     */
    LoginVo login(LoginDto loginDto) ;

}