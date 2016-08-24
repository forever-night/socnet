package socnet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;


public class AccountDto implements Serializable {
    private String login;
    private String email;
    private String password;
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(login, email, password);
    }
    
    @Override
    public String toString() {
        return "AccountDto {" +
                "login=" + login +
                ", email=" + email +
                ", password=" + password +
                "}";
    }
}
