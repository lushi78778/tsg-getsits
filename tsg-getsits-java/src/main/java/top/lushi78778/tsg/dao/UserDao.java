package top.lushi78778.tsg.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import top.lushi78778.tsg.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author lushi
 * @since 2022-03-06 08:37:23
 */
public class UserDao {

    /**
     * 获取所有有效用户
     *
     * @return List<User>
     */
    public List<User> getAll() throws SQLException {
        String sql = "SELECT * FROM `user` WHERE state = 1;";
        return BeanUtil.copyToList(Db.use().query(sql), User.class);
    }

    /**
     * get 更新 cookie
     *
     * @param user id
     * @return User
     */
    public User getOne(User user) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE id = ?;";
        Entity newUser = Db.use().queryOne(sql,user.getId() );
        if (newUser == null){
            return new User();
        }
        return newUser.toBean(User.class);
    }

    /**
     * 更新cookie
     *
     * @param user 用户对象
     * @return 主键
     */
    public int updateCookie(User user) throws SQLException {
        return Db.use().update(
                Entity.create("user")
                        .set("cookies", user.getCookies()),
                Entity.create("user").set("phone", user.getPhone()) //where条件
        );
    }


//    public static void main(String[] args) throws SQLException {
//        Console.log(new UserDao().getAll());
//
//    }
}

