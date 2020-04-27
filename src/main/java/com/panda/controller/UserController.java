package com.panda.controller;

import com.panda.domain.User;
import com.panda.mapper.UserDao;
import com.panda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class UserController {


    @Autowired
    UserService userService;

    //增加一个
    @RequestMapping("/addOne")
    public int addOne(){
        User user = new User();
        user.setName("xiaohu");
        user.setEmail("xiaohu@rng.com");
        return userService.addOne(user);
    }

    //删除一个
    @RequestMapping("/deleteOne")
    @ResponseBody
    public int deleteOne(int id){
        return userService.deleteOne(id);
    }

    //更新一个
    @RequestMapping("/updateOne")
    @ResponseBody
    public int updateOne(){
        User user = new User();
        user.setId(4);
        user.setName("littleTiger");
        return userService.updateOne(user);
    }

    //查询一个
    @RequestMapping("/selectOne")
    @ResponseBody
    public User selectOne(String id){
        return userService.selectOne(id);
    }

    //查询所有
    @RequestMapping("/selectAll")
    @ResponseBody
    public List<User> selectAll(){
        return userService.selectAll();
    }

    //name模糊查询
    @RequestMapping("/selectOneNameLike")
    @ResponseBody
    public List<User> selectOneNameLike(String name){
        return userService.selectOneNameLike(name);
    }
}
