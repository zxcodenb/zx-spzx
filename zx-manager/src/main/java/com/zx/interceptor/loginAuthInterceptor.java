package com.zx.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zx.AuthContextUtil;
import com.zx.spzx.model.entity.system.SysUser;
import com.zx.spzx.model.vo.common.Result;
import com.zx.spzx.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;


/**
 * 拦截器
 */
@Component
public class loginAuthInterceptor implements HandlerInterceptor {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //获取请求头中的token，查询redis中是否存在，如果存在则放行，否则拦截，查出的用户信息放入ThreadLocal中
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1 获取请求方式
        //如果请求方式是options 预检请求，直接放行
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }


        //获取请求头中的token
        String token = request.getHeader("token");

        if (StrUtil.isEmpty(token)) {

            //5 如果redis查询不到数据，返回错误提示
            responseNoLoginInfo(response);
            return false;
        }


        //如果token不为空，拿着token查询redis
        String userInfoString = redisTemplate.opsForValue().get(token);

        if (StrUtil.isEmpty(userInfoString)) {
            //5 如果redis查询不到数据，返回错误提示
            responseNoLoginInfo(response);
            return false;
        }

        // 如果redis查询到用户信息，把用户信息放到ThreadLocal里面
        SysUser sysUser = JSON.parseObject(userInfoString, SysUser.class);
        AuthContextUtil.set(sysUser);


        // 把redis用户信息数据更新过期时间
        redisTemplate.opsForValue().set(token, userInfoString, 30 * 60 * 1000);

        //放行
        return true;

    }

    //响应208状态码给前端
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }


    //请求结束后，删除ThreadLocal中的用户信息
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContextUtil.remove();
    }


}
