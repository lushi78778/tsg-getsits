package top.lushi78778.tsg.model;

import cn.hutool.core.lang.Console;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName CookieModel
 * @Description
 * @Author lushi
 * @Date 2022/3/5 19:38
 */
@Data
public class CookieModel {

    private String domain;

    private Date expiry;

    {
        expiry = null;
    }

    private boolean httpOnly;

    {
        httpOnly = false;
    }

    private String name;

    private String path;

    private boolean secure;

    {
        secure = false;
    }

    private String value;

//    public static void main(String[] args) {
//        Console.log(new CookieModel());
//    }
}
