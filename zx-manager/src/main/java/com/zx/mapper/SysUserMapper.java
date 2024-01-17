package com.zx.mapper;

import com.zx.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {

    /**
     * 根据用户名查询用户数据
     * @param userName
     * @return
     */
    SysUser selectByUserName(String userName) ;

}