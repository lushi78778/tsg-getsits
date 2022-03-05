package top.lushi78778.tsg.job;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;

import org.junit.Ignore;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import top.lushi78778.tsg.model.CookieModel;
import top.lushi78778.tsg.model.User;

import javax.annotation.Resource;
import java.util.*;

import static top.lushi78778.tsg.utils.CommonUtils.getUser;

/**
 * @ClassName SitTask
 * @Description 计划任务
 * @Author lushi
 * @Date 2022/3/5 16:52
 */

public class SitTask implements Job {

    private static final Logger logger = LoggerFactory.getLogger(SitTask.class);

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    WebDriver browser = new ChromeDriver();

    String cookies = "";

    String url = "https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335";

    @SneakyThrows
    public void execute(JobExecutionContext jobExecutionContext) {

        for (User user:getUser()){
            //开启线程
            ThreadUtil.execute(() -> {
//                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
//                Date beginTime = null;
//                Date endTime = null;
//                try {
//                    beginTime = df.parse("06:59:40");
//                    endTime = df.parse("23:00:59");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (belongCalendar(new Date(), beginTime, endTime))logger.info(user+"循环开始");
//                //循环时间限制
                while (true){
                    try {
                        logger.info(user.getPhone()+"登陆......");
                        if (get_login(user) == 1){
                            logger.info(user.getPhone()+"登陆成功，开始作业");
                            if (login_crouse(user) == 1){
                                logger.info(user.getPhone()+"预约成功");
                            }
                        }
                    } catch (InterruptedException e) {
                        logger.error(String.valueOf(e));
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
    public Integer get_login(User user) throws InterruptedException {
        try {
            cookies = stringRedisTemplate.opsForValue().get(user.getCookies_number());
            browser.get("https://office.chaoxing.com");
            //直接有cookies
            List<String> cookiesList= Collections.singletonList(cookies);
            List<CookieModel> cookiesModelList = BeanUtil.copyToList(cookiesList, CookieModel.class);
            for (CookieModel cookieModel:cookiesModelList){
                browser.manage().addCookie(
                        new Cookie(
                                cookieModel.getName(), cookieModel.getValue(),
                                cookieModel.getDomain(),cookieModel.getPath(),
                                cookieModel.getExpiry(),
                                cookieModel.isSecure(),cookieModel.isHttpOnly()));
            }
            browser.get(url);
            Wait<WebDriver> wait = new WebDriverWait(browser, 5);
            WebElement element= wait.until(visibilityOfElementLocated(By.className("lg-container")));
            if (element != null) return 1;
            return 0;
        }catch (Exception e){
            logger.info("第一次登陆，get cookie");
            //账号密码登录
            browser.get(url);
            Thread.sleep(3000);
            browser.findElement(By.className("ipt-tel")).clear();
            browser.findElement(By.className("ipt-pwd")).clear();
            browser.findElement(By.className("ipt-tel")).sendKeys(user.getPhone());
            browser.findElement(By.className("ipt-pwd")).sendKeys(user.getPassword());
            Thread.sleep(1000);
            //xpath有问题
            browser.findElement(By.xpath("/html/body/div/div[2]/div/form/div[3]/button")).click();
            Thread.sleep(1000);
            browser.findElement(By.className("sind_top_word"));
            Wait<WebDriver> wait = new WebDriverWait(browser, 5);
            WebElement element= wait.until(visibilityOfElementLocated(By.className("sind_top_word")));
            if (element != null){
                Set<Cookie> cookie = browser.manage().getCookies();
                logger.info(user.getPhone()+"登陆成功"+cookie.toString());
                stringRedisTemplate.opsForValue().set(user.getCookies_number(), String.valueOf(cookie));
            }
            get_login(user);
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
        Thread.sleep(300);
        String room="/html/body/div/ul[1]/li["+user.getRoom()+"]/span";
        String sit="/html/body/div/div[2]/ul/li["+user.getSit()+"]";
        browser.findElement(By.xpath(room)).click();
        Thread.sleep(200);
        Wait<WebDriver> wait = new WebDriverWait(browser, 5);
        WebElement element= wait.until(visibilityOfElementLocated(By.className("'time_sure'")));
        WebElement chart;
        if (element != null){
            browser.findElement(By.className("time_cell"));
//            chart[time_start].click()
//            chart[time_end].click()

            browser.findElement(By.className("time_sure")).click();
            browser.findElement(By.xpath(sit)).click();
            browser.findElement(By.className("order_bottom")).click();
            return 1;
        }
        return 0;
    }

    /**
     * "visibilityOfElementLocated"实现
     *
     * @param locator 元素名
     * @return ExpectedCondition<WebElement>
     */
    public ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement toReturn = driver.findElement(locator);
                if (toReturn.isDisplayed()) {
                    return toReturn;
                }
                return null;
            }
        };
    }

}
