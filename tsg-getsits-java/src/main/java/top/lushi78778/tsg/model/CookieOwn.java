package top.lushi78778.tsg.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName CookieOwn
 * @Description Cookie 模型
 * @Author lushi
 * @Date 2022/3/6 10:07
 */
@Data
public class CookieOwn implements Serializable {
    private static final long serialVersionUID = 4115876353625612383L;
    private String name;
    private String value;
    private String path;
    private String domain;
    private Date expiry;
    private boolean isSecure;
    private boolean isHttpOnly;
}
