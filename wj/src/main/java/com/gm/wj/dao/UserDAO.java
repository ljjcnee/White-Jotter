package com.gm.wj.dao;

import com.gm.wj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User,Integer> {
    // 关键是这一句，Spring Data JPA 会自动根据方法名生成 SQL
    User findByUsername(String username);

    // 这一句可能你原来就有，也保留着
    User getByUsernameAndPassword(String username,String password);
}