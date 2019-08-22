package net.zhaoxuyang.blog.model;
/**
 *
 * @author zhaoxuyang
 */
public class Conf {
    private String key;
    private String value;
    private String info;

    @Override
    public String toString() {
        return "Conf{" + "key=" + key + ", value=" + value + ", info=" + info + '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    
}
