package top.lushi78778.tsg.entity;

import lombok.Data;
import org.openqa.selenium.Cookie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * (User)实体类
 *
 * @author lushi
 * @since 2022-03-06 08:37:21
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -74554204128642445L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户名
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * 房间号
     */
    private Integer room;
    /**
     * 座位号
     */
    private Integer sit;
    /**
     * 开始时间
     */
    private Integer startTime;
    /**
     * 结束时间
     */
    private Integer stopTime;
    /**
     * cookies
     */
    private String cookies;
    /**
     * 状态 1 启用
     */
    private Integer state;

}
