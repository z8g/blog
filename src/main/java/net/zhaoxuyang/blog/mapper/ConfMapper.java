package net.zhaoxuyang.blog.mapper;

import net.zhaoxuyang.blog.model.Conf;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zhaoxuyang
 */
@Repository

public interface ConfMapper {

    @MapKey("key")
    public Map<String, Conf> getMap();

    public String get(String key);

    public int update(Conf conf);

}
