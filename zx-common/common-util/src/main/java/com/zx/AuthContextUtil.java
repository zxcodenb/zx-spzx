package com.zx;


import com.zx.spzx.model.entity.system.SysUser;
import org.springframework.stereotype.Component;

public class AuthContextUtil {


    private static  final ThreadLocal<SysUser> t = new ThreadLocal<>();





    public static void set(SysUser sysUser){

        t.set(sysUser);

    }

    public static void remove(){
        t.remove();
    }

    public static SysUser get (){


        return t.get();
    }










}
