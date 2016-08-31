package socnet.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class ProfileDto implements Serializable {
    private String name;
    private String country;
    private String city;
    private String phone;
    private String info;
    private Date dateOfBirth;
    private String login;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getInfo() {
        return info;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDto that = (ProfileDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(info, that.info) &&
                Objects.equals(dateOfBirth, that.dateOfBirth);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, country, city, phone, info, dateOfBirth);
    }
    
    @Override
    public String toString() {
        return "ProfileDto {" +
                "name=" + name +
                ", country=" + country +
                ", city=" + city +
                ", phone=" + phone +
                "}";
    }
}
