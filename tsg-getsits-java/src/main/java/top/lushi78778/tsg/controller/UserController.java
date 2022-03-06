package top.lushi78778.tsg.controller;

import org.springframework.web.bind.annotation.*;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2022-03-06 08:37:24
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {


    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public String getStudentInfo(){

        return "result";
    }
}
