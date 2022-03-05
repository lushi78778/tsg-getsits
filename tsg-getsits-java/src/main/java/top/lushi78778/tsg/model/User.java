package top.lushi78778.tsg.model;

import cn.hutool.core.lang.Console;
import lombok.Data;

import static cn.hutool.core.util.IdUtil.randomUUID;

/**
 * @ClassName User
 * @Description 用户对象
 * @Author lushi
 * @Date 2022/3/5 16:24
 */
@Data
public class User {

    private String id;

    private String phone;

    private String password;

    private String room;

    private String sit;

    private String start_time;

    private String stop_time;

    private String cookies_number;

    {
        cookies_number = randomUUID();
    }

//    public static void main(String[] args) {
//        User user = new User();
//        Console.log(user);
//    }
}
