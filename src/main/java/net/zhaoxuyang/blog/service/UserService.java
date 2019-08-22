package net.zhaoxuyang.blog.service;

import java.util.Map;
import net.zhaoxuyang.blog.model.Conf;
import net.zhaoxuyang.blog.model.User;

/**
 *
 * @author zhaoxuyang
 */
public interface UserService {
    public User login(User user);
    
    public Map<String,Conf> getConfMap();
    
    public boolean setConf(Conf conf);
}
