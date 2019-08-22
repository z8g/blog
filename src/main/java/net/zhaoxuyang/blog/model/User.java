package net.zhaoxuyang.blog.model;

/**
 *
 * @author zhaoxuyang
 */
public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String emailPassword;
    private String info;

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", nickname=" + nickname + ", email=" + email + ", emailPassword=" + emailPassword + ", info=" + info + '}';
    }
    
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }
    
}
