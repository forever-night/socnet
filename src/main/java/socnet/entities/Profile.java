package socnet.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "profile")
@JsonIgnoreProperties({"version", "sentPrivate", "receivedPrivate", "sentPublic", "receivedPublic"})
public class Profile implements Serializable {
    @Id @Column(name = "account_id")
    private Integer id;

    private String name;
    private String country;
    private String phone;
    private String info;

    @Column(name = "current_city")
    private String currentCity;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date dateOfBirth;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "profile_follower",
                joinColumns = {@JoinColumn(name = "owner_id")},
                inverseJoinColumns = {@JoinColumn(name = "follower_id")})
    private Set<Profile> followers;

    @OneToMany(mappedBy = "sender")
    private List<PrivateMessage> sentPrivate;

    @OneToMany(mappedBy = "receiver")
    private List<PrivateMessage> receivedPrivate;

    @OneToMany(mappedBy = "sender")
    private List<PublicMessage> sentPublic;

    @OneToMany(mappedBy = "receiver")
    private List<PublicMessage> receivedPublic;

    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Profile> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Profile> followers) {
        this.followers = followers;
    }

    public List<PrivateMessage> getSentPrivate() {
        return sentPrivate;
    }

    public void setSentPrivate(List<PrivateMessage> sentPrivate) {
        this.sentPrivate = sentPrivate;
    }

    public List<PrivateMessage> getReceivedPrivate() {
        return receivedPrivate;
    }

    public void setReceivedPrivate(List<PrivateMessage> receivedPrivate) {
        this.receivedPrivate = receivedPrivate;
    }

    public List<PublicMessage> getSentPublic() {
        return sentPublic;
    }

    public void setSentPublic(List<PublicMessage> sentPublic) {
        this.sentPublic = sentPublic;
    }

    public List<PublicMessage> getReceivedPublic() {
        return receivedPublic;
    }

    public void setReceivedPublic(List<PublicMessage> receivedPublic) {
        this.receivedPublic = receivedPublic;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + dateOfBirth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())return false;
        Profile profile = (Profile) o;
        return Objects.equals(name, profile.name) &&
                Objects.equals(country, profile.country) &&
                Objects.equals(phone, profile.phone) &&
                Objects.equals(info, profile.info) &&
                Objects.equals(currentCity, profile.currentCity) &&
                Objects.equals(dateOfBirth, profile.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, phone, info, currentCity, dateOfBirth);
    }
}
