package socnet.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "account")
//@JsonIgnoreProperties(value = {"version"}, ignoreUnknown = true)
public class Account implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    private String login;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "enabled", columnDefinition = "boolean not null default true")
    private Boolean isEnabled = Boolean.TRUE;

    @Version
    private Integer version;

    @PrePersist
    protected void prePersist() {
        createdAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public Boolean getEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", login=" + login +
                ", email=" + email +
                ", role=" + role.getRoleTitle() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(login, account.login) &&
                Objects.equals(email, account.email) &&
                Objects.equals(password, account.password) &&
                Objects.equals(role, account.role) &&
                Objects.equals(isEnabled, account.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, email, password, role, isEnabled);
    }
}
