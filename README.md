# 通用Mapper介绍

## 产生背景

> 使用Mybatis的开发者大多会因为繁多的XML映射配置而头痛不已😔，即使使用注解，大量的SQL语句也是不可避免的，当数据库表结构发生变动时，所有对应的sql和实体类都得修改，基于此，通用Mapper出现了😊

## 基本概念

> 通用Mapper是一款用于单表增删改查的Mybatis插件，开发人员可以省去编写sql语句和在DAO层编写任何方法，就能轻松实现单表的常用操作。

## 项目地址

> 支持国产，从我做起。👍通用Mapper的作者是一位国人。项目地址：
> 
> -   码云：[https://gitee.com/free/Mapper](https://gitee.com/free/Mapper)
> -   GitHub：[https://github.com/abel533/Mybatis-Spring](https://github.com/abel533/Mybatis-Spring)

# 环境导入

## 环境说明

平台：windows  
jdk版本：1.8  
IDE：IDEA  
数据库：mysql(8)

## 项目创建

1.  新建一个Springboot项目
    
    新建项目就没啥好说了，使用IDEA自带的Spring Initializer，一直下一步就完事了，省心
    
2.  添加通用mapper依赖
    
    ```html
    <dependency>
         <groupId>tk.mybatis</groupId>
         <artifactId>mapper-spring-boot-starter</artifactId>
         <version>2.1.5</version>
    </dependency>
    ```
    
3.  SpringBoot配置文件
    
    ```properties
    #JDBC配置
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    spring.datasource.username=root
    spring.datasource.password=root
    #mybatis配置
    mybatis.configuration.map-underscore-to-camel-case=true
    
    logging.level.root = debug
    ```
    

## 数据准备

1.  建表语句

```sql
create table user(
     id int(10) auto_increment primary key,  
     name varchar(20) null,  
     email varchar(50) null 
   );
```

## 实体类编写

```java
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

**说明**

1.  表名默认使用类名，驼峰会转换为下划线，如UserInfo对应的表名为user_info.当不满足上述对应关系时，类名上使用@Table注解，使用name属性指定tableName；
    
2.  类的属性名默认会采用驼峰法转换为表字段名，如userName转化为user_name;不满足上述对应关系时，字段名上使用@Column注解，使用name属性指定表字段名；
    
3.  表字段没有的属性，可以使用@Transient注解。
    
4.  至少有一个字段使用@Id标识主键，多个@Id注解生成联合主键。
    
5.  @GeneratedValue指定主键生成策略。
    

## Dao层
Dao层使用通用Mapper接口，只需要让我们的接口实现Mapper<T>即可。

**此处需注意导入的是tk.mybatis.mapper.common.Mapper而不是mybatis的Mapper**

继承该Mapper后，会获取父接口的常用方法。

```java
package com.panda.mapper;  
import com.panda.domain.User;  
import tk.mybatis.mapper.common.Mapper;  
public interface UserDao extends Mapper {  
}  
```

## 业务层

业务层接口：

```java
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
 UserDao userDao; //此处如果报错为误报，忽略即可
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

-   基本增删改查
    
    1.  查询
        
        select(T t) :根据传入对象属性查询，返回实体类元素集合。
        
        selectOne(T t)) :根据传入的对象属性查询，存在多个返回值时抛出异常。
        
        selectAll() :返回实体类元素集合。
        
        selectCount(T t)) :根据传入的对象属性查询，返回总记录数。
        
    2.  删除
        
        delete(T t)) 根据传入对象属性，删除所有查找到的记录。
        
        deleteByPrimaryKey(Object key) 根据传入的主键，删除对应记录。
        
    3.  更新
        
        updateByPrimaryKey(Object o) 根据传入对象属性，更新所有字段 。
        
        updateByPrimaryKeySelective(T t) 只更新对象属性非空字段 。
        
    4.  插入
        
        insert(T t)) 根据传入对象属性，插入所有属性对应的字段。
        
        insertSelective(T t)) 根据传入对象属性，只插入对象属性非空字段
        
-   复杂条件查询
    
    通用Mapper复杂条件查询使用的是Example，示例语法如下：
    
    ```java
     Example example = new Example(User.class);
     Criteria criteria = example.createCriteria()
     criteria.andLike("name","%"+name+"%");
     userDao.selectByExample(example);
    ```
    
    其中，Criteria对象可使用的方法很多，基本可以满足常用的条件查询：
    ![所有方法](https://images.cnblogs.com/cnblogs_com/LearnAndGet/1371984/o_200427012115method.png "all methods")
    上面的方法基本从方法名就可以看出其作用，使用起来也比较方便。
    

## 显示层

controller实现：

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
 public int updateOne(@RequestBody User user){
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

## 测试

1.  测试插入  
    浏览器输入：[http://localhost:8080/addOne],可以看到数据库成功插入一个，当然正常情况应该是前端请求的     时候传入一个json对象给我们来插入数据      库，这里偷懒使用了自己创建的User对象，不过无伤大雅。
2.  测试更新
    浏览器输入：[http://localhost:8080/updateOne],通用使用我们自己创建User对象来更新数据库记录。
3.  测试查询：[http://localhost:8080/selectOne?id=1]，成功查询到id=1的用户
4.  测试删除：[http://localhost:8080/deleteOne?id=1]，成功删除id=1的用户

# 通用Mapper的原理简介

通用Mapper通过让dao层接口继承Mapper<T.class> 接口i，在运行期通过反射获取实体类T的信息，并构造出对应的SQL，dao层接口只需要直接调用父接口提供的方法，便可完成对应SQL的调用和数据库查询。

更加详细执行过程在此🙅‍赘述，有感兴趣的朋友可以访问：

[MyBatis 通用 Mapper 实现原理]([https://blog.csdn.net/chenyao1994/article/details/79254941](https://blog.csdn.net/chenyao1994/article/details/79254941)
