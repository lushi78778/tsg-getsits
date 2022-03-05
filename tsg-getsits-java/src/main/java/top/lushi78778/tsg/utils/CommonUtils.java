package top.lushi78778.tsg.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import lombok.Data;
import top.lushi78778.tsg.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CommonUtils
 * @Description 常用工具类
 * @Author lushi
 * @Date 2022/3/5 16:37
 */

public class CommonUtils {

    /**
     * 获取用户数据
     *
     * @return List<User>
     */
    public static List<User> getUser(){
        final CsvReader reader = CsvUtil.getReader();
        final List<User> result = reader.read(
                ResourceUtil.getUtf8Reader("data.csv"), User.class);
        return result;
    }

//    /**
//     * 判断时间是否在时间段内
//     *
//     * @param nowTime 当前时间
//     * @param beginTime 开始
//     * @param endTime 结束
//     * @return bool
//     */
//    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
//        Calendar date = Calendar.getInstance();
//        date.setTime(nowTime);
//        Calendar begin = Calendar.getInstance();
//        begin.setTime(beginTime);
//        Calendar end = Calendar.getInstance();
//        end.setTime(endTime);
//        return date.after(begin) && date.before(end);
//    }

//    public static void main(String[] args) throws ParseException {
//        for (User user:getUser()){
//            Console.log(user);
//        }
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
//        Date now = new Date();
//        Date beginTime = df.parse("06:59:40");
//        Date endTime = df.parse("07:00:59");
//        Boolean flag = belongCalendar(now, beginTime, endTime);
//        System.out.println(flag);
//    }
}
