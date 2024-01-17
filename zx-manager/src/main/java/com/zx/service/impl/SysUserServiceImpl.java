package com.zx.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zx.exception.ZxException;
import com.zx.mapper.SysUserMapper;
import com.zx.service.SysUserService;
import com.zx.spzx.model.dto.system.LoginDto;
import com.zx.spzx.model.entity.system.SysUser;
import com.zx.spzx.model.vo.common.ResultCodeEnum;
import com.zx.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;


@Service
public class SysUserServiceImpl implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper ;


    @Autowired
    private RedisTemplate<String , String> redisTemplate ;


    @Override
    public LoginVo login(LoginDto loginDto) {


        String codeKey = loginDto.getCodeKey();
        String captcha = loginDto.getCaptcha();

        //从redis获取
        String redisCodekey = redisTemplate.opsForValue().get("user:login:validatecode:" + codeKey);
        //判断是否为空，并比较
        if(StrUtil.isEmpty(redisCodekey) || !StrUtil.equalsIgnoreCase(redisCodekey , captcha)) {
            throw new ZxException(ResultCodeEnum.VALIDATECODE_ERROR) ;
        }
        // 验证通过删除redis中的验证码
        redisTemplate.delete("user:login:validatecode:" + codeKey) ;


        // 根据用户名查询用户
        SysUser sysUser = sysUserMapper.selectByUserName(loginDto.getUserName());

        if(sysUser == null) {
            throw new ZxException(ResultCodeEnum.LOGIN_ERROR);
        }


        // 验证密码是否正确
        String inputPassword = loginDto.getPassword();
        String md5InputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if(!md5InputPassword.equals(sysUser.getPassword())) {
            throw new ZxException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 生成令牌，保存数据到Redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("user:login:" + token , JSON.toJSONString(sysUser) , 30 , TimeUnit.MINUTES);

        // 构建响应结果对象
        LoginVo loginVo = new LoginVo() ;
        loginVo.setToken(token);
        loginVo.setRefresh_token("");

        // 返回
        return loginVo;
    }
}
