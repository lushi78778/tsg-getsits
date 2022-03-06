package top.lushi78778.tsg;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.lushi78778.tsg.job.SitTask;

@SpringBootApplication
public class TsgGetsitsJavaApplication {

    public static void main(String[] args) throws SchedulerException {
        SpringApplication.run(TsgGetsitsJavaApplication.class, args);
        doScheduler();
    }

    public static void doScheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(SitTask.class)
                .withIdentity("job1", "group1").build();
//        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule("0 58 6 * * ? ");
        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule("00 20 13 * * ? ");
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger", "triggerGroup")
                .startNow()
                .withSchedule(builder).build();
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();
    }
}
