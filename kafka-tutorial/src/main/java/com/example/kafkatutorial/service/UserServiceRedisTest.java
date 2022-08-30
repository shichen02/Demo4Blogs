package com.example.kafkatutorial.service;

public interface UserServiceRedisTest {

    User saveOrUpdate(User user);

    User get(Long id);

    void delete(Long id);
}
