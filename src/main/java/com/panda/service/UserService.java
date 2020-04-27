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
