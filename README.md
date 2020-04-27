#通用Mapper介绍
##基本概念
##项目地址
#环境导入
##环境说明
平台：windows
jdk版本：1.8
IDE：IDEA
数据库：mysql
##项目创建
1. 新建一个Springboot项目
2. 添加通用mapper依赖
##数据准备
1. 建表语句
````
create table user
(
    id    int(10) auto_increment
        primary key,
    name  varchar(20) null,
    email varchar(50) null
);

````
#实体类创建
```
package com.panda.domain;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

```
#通用mapper接口创建
此处需注意导入的是tk.mybatis.mapper.common.Mapper而不是mybatis的Mapper
```
package com.panda.mapper;

import com.panda.domain.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserDao extends Mapper<User> {
}

```
#业务层
业务层接口：
```Java
package com.panda.service;

import com.panda.domain.User;

import java.util.List;

public interface UserService {
    User selectOne(String id);

    int addOne(User user);

    int deleteOne(int id);

    int updateOne(User user);

    List<User> selectOneNameLike(String name);

    List<User> selectAll();
}

```
 业务层实现：
 ```java
package com.panda.service.impl;

import com.panda.domain.User;
import com.panda.mapper.UserDao;
import com.panda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User selectOne(String id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int addOne(User user) {
        return userDao.insertSelective(user);
    }

    @Override
    public int deleteOne(int id) {
        return userDao.deleteByPrimaryKey(id);
    }

    @Override
    public int updateOne(User user) {
        return userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> selectOneNameLike(String name) {
        Example example = new Example(User.class);
        example.createCriteria().andLike("name","%"+name+"%");
        return userDao.selectByExample(example);
    }

    @Override
    public List<User> selectAll() {
        return userDao.selectAll();
    }
}
```
通用Mapper的常见方法解析：
1. 基本增删改查
2. 复杂条件查询
#显示层
```java
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
    @ResponseBody
    public int addOne(){
        User user = new User();
        user.setName("uzi");
        user.setEmail("uzi@rng.com");

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
    public int updateOne(@RequestBody  User user){
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
```
#测试
1. 测试查询
浏览器输入：http://localhost:8080/selectOne?id=1
2. 测试删除
3. 测试插入
4. 测试更新