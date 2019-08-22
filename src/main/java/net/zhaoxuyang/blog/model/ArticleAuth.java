package net.zhaoxuyang.blog.model;

/**
 *
 * @author zhaoxuyang
 */
public enum ArticleAuth {
    PUBLIC, PRIVATE;

    public int getValue() {
        return isPublic() ? 0 : 1;
    }

    public boolean isPublic() {
        return this.equals(PUBLIC);
    }   
    
    public boolean isPrivate() {
        return this.equals(PRIVATE);
    }
}
