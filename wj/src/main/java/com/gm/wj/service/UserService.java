package com.gm.wj.service;

import com.gm.wj.dao.UserDAO;
import com.gm.wj.entity.AdminRole;
import com.gm.wj.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserRoleService adminUserRoleService;

    public List<User> list() {
        List<User> users =  userDAO.findAll();
        // 重置角色列表，以免循环引用或其他问题
        List<AdminRole> roles;
        for (User user : users) {
            roles = adminRoleService.listRolesByUser(user.getName());
            user.setRoles(roles);
        }
        return users;
    }

    public boolean isExist(String username) {
        User user = getByName(username);
        return null != user;
    }

    // 1. 给 LibraryController 用的查找方法
    public User getByName(String username) {
        return userDAO.findByUsername(username);
    }

    // 2. 给 LoginController 用的查找方法 (兼容旧代码)
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User get(String username, String password){
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public void add(User user) {
        user.setEnabled(true);
        userDAO.save(user);
    }

    public int register(User user) {
        String username = user.getUsername();
        String name = user.getName();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getPassword();

        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        user.setEnabled(true);

        if (username.equals("") || password.equals("")) {
            return 0;
        }

        boolean exist = isExist(username);

        if (exist) {
            return 2;
        }

        // 默认生成 16 位盐
        String salt = new java.security.SecureRandom().toString();
        int times = 2;
        String encodedPassword = new org.apache.shiro.crypto.hash.SimpleHash("md5", password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        userDAO.save(user);

        return 1;
    }

    public void updateUserStatus(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setEnabled(user.isEnabled());
        userDAO.save(userInDB);
    }

    public User resetPassword(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        String salt = new java.security.SecureRandom().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodedPassword = new org.apache.shiro.crypto.hash.SimpleHash("md5", "123", salt, times).toString();
        userInDB.setPassword(encodedPassword);
        return userDAO.save(userInDB);
    }

    public void editUser(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setName(user.getName());
        userInDB.setPhone(user.getPhone());
        userInDB.setEmail(user.getEmail());
        userDAO.save(userInDB);
        adminUserRoleService.saveRoleChanges(userInDB.getId(), user.getRoles());
    }

    public void deleteById(int id) {
        userDAO.deleteById(id);
    }

    // ↓↓↓ 3. 补上了这个缺失的方法！给 DashboardController 用 ↓↓↓
    public long count() {
        return userDAO.count();
    }
}