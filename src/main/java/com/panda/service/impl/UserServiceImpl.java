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

    @Autowired(required = false)  //此处是为消除报错
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
        example.createCriteria().andLike("name",name);
        return userDao.selectByExample(example);
    }

    @Override
    public List<User> selectAll() {
        return userDao.selectAll();
    }
}
