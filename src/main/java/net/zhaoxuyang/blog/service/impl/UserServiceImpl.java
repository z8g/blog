/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.zhaoxuyang.blog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.zhaoxuyang.blog.mapper.ConfMapper;
import net.zhaoxuyang.blog.model.Conf;
import net.zhaoxuyang.blog.model.User;
import net.zhaoxuyang.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ConfMapper confMapper;

    @Override
    public User login(User user) {
        return loginCheck(user) ? createAuthor() : null;
    }

    private boolean loginCheck(User user) {
        Map<String, Conf> confMap = confMapper.getMap();
        String username = confMap.get("user.username").getValue();
        String password = confMap.get("user.password").getValue();
        return Objects.equals(user.getUsername(), username)
                && Objects.equals(user.getPassword(), password);
    }

    private User createAuthor() {
        User result = new User();

        Map<String, Conf> confMap = confMapper.getMap();
        result.setUsername(confMap.get("user.username").getValue());
        result.setPassword(confMap.get("user.password").getValue());
        result.setNickname(confMap.get("user.nickname").getValue());
        result.setEmail(confMap.get("user.email").getValue());
        result.setEmailPassword(confMap.get("user.emailPassword").getValue());
        result.setInfo(confMap.get("user.info").getValue());
        return result;
    }

    @Override
    public Map<String, Conf> getConfMap() {
        return confMapper.getMap();
    }

    @Override
    public boolean setConf(Conf conf) {
        return confMapper.update(conf) > 0;
    }
    
    

}
