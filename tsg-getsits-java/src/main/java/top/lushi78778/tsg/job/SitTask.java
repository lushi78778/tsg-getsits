package top.lushi78778.tsg.job;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;

import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lushi78778.tsg.dao.UserDao;
import top.lushi78778.tsg.entity.User;
import top.lushi78778.tsg.model.CookieOwn;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;


/**
 * @ClassName SitTask
 * @Description 计划任务
 * @Author lushi
 * @Date 2022/3/5 16:52
 */

public class SitTask implements Job {

    private static final Logger logger = LoggerFactory.getLogger(SitTask.class);

    @Resource
    WebDriver browser = new ChromeDriver();

    UserDao userDao = new UserDao();

    String url = "https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335";

    @SneakyThrows
    public void execute(JobExecutionContext jobExecutionContext) {

        for (User user: userDao.getAll()){
            //开启线程
            ThreadUtil.execute(() -> {
                while (true) {
                    try {
                        logger.info(user.getPhone() + "登陆......");
                        if (get_login(user) == 1) {
                            logger.info(user.getPhone() + "登陆成功，开始作业");
                            if (login_crouse(user) == 1) {
                                logger.info(user.getPhone() + "预约成功");
                            }else {
                                break;
                            }
                        }
                        //获取刚刚更新的cookies
                        userDao.getOne(user);
                    } catch (Exception e) {
                        logger.error(String.valueOf(e.getMessage()));
                    }
                }
            });
        }
    }

    /**
     * 登录
     *
     * @param user 用户信息
     * @return bool
     */
    public Integer get_login(User user) throws InterruptedException, SQLException {
        if (user.getCookies() != null) {
            //debug：cookie无效的作用域，以下均无效
            browser.get(url);
            //直接有cookies
            JSONArray array = JSONUtil.parseArray(user.getCookies());
            List<CookieOwn> cookieList = JSONUtil.toList(array, CookieOwn.class);
            logger.info("获取cookie登录:"+cookieList);
            for (CookieOwn cookie : cookieList) {
                browser.manage().addCookie(
                        new Cookie(
                                cookie.getName(), cookie.getValue(),
                                cookie.getDomain(), cookie.getPath(),
                                cookie.getExpiry(),
                                cookie.isSecure(), cookie.isHttpOnly()));
            }
            browser.get(url);
            Wait<WebDriver> wait = new WebDriverWait(browser, 5);
            WebElement element = wait.until(visibilityOfElementLocated(By.className("lg-container")));
            if (element != null) return 1;
            return 0;
        } else {
            logger.info("第一次登陆，get cookie");
            //账号密码登录
            browser.get(url);
            browser.manage().deleteAllCookies();
            browser.get(url);
            Thread.sleep(3000);
            browser.findElement(By.className("ipt-tel")).clear();
            browser.findElement(By.className("ipt-pwd")).clear();
            browser.findElement(By.className("ipt-tel")).sendKeys(user.getPhone());
            browser.findElement(By.className("ipt-pwd")).sendKeys(user.getPassword());
            Thread.sleep(1000);
            //xpath有问题
            browser.findElement(By.xpath("//*[@id=\"loginBtn\"]")).click();
            Thread.sleep(1000);
            browser.findElement(By.className("sind_top_word"));
            Wait<WebDriver> wait = new WebDriverWait(browser, 5);
            WebElement element = wait.until(visibilityOfElementLocated(By.className("sind_top_word")));
            if (element != null) {
                Set<Cookie> cookies = browser.manage().getCookies();
                JSONArray cookie = new JSONArray();
                for (Cookie value : cookies) {
                    cookie.add(value.toJson());
                }
                user.setCookies(String.valueOf(cookie));
                logger.info(user.getPhone() + "cookies获取成功" + cookie);
//                userDao.updateCookie(user);
                return 1;
            }
            return 0;
        }
    }

    /**
     * 抢座
     *
     * @param user 用户对象
     * @return bool
     */
    public int login_crouse(User user) throws InterruptedException {
        browser.get(url);
        Thread.sleep(3000);
        //弹窗
        browser.findElement(By.className("rp_sure")).click();
        Thread.sleep(3000);
        //预约选座
        browser.findElement(By.className("sind_top_word")).click();
        Thread.sleep(3000);
        //选房间
        browser.findElement(By.xpath("/html/body/div/ul[1]/li["+user.getRoom()+"]/span")).click();
        //选时间，正常顺序
        WebElement chart = browser.findElement(By.className("time_cell"));
//        chart[2].click();
        browser.findElement(By.xpath("/html/body/div/div[5]/ul/li["+user.getStartTime()+"]")).click();
        browser.findElement(By.xpath("/html/body/div/div[5]/ul/li["+user.getStopTime()+"]")).click();
        Thread.sleep(3000);
        //确认时间
        browser.findElement(By.className("time_sure"));
        //选座位，正常顺序
        browser.findElement(By.xpath("/html/body/div/div[2]/ul/li["+user.getSit()+"]/span")).click();
        Thread.sleep(3000);
        //确认座位
        browser.findElement(By.className("order_bottom")).click();
        return 1;
    }

    /**
     * "visibilityOfElementLocated"实现
     *
     * @param locator 元素名
     * @return ExpectedCondition<WebElement>
     */
    public ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
        return driver -> {
            WebElement toReturn = driver.findElement(locator);
            if (toReturn.isDisplayed()) {
                return toReturn;
            }
            return null;
        };
    }

}
