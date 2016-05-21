package socnet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by anna on 14/03/16.
 */
@Entity
@Table(name="community")
public class Community implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Profile owner;

    @ManyToMany
    @JoinTable(name = "community_admin",
                joinColumns = {@JoinColumn(name = "community_id")},
                inverseJoinColumns = {@JoinColumn(name = "admin_id")})
    private Set<Profile> admins;

    @ManyToMany
    @JoinTable(name = "community_follower",
                joinColumns = {@JoinColumn(name = "owner_id")},
                inverseJoinColumns = {@JoinColumn(name = "follower_id")})
    private Set<Profile> followers;

    @OneToMany(mappedBy = "sender")
    private List<CommunityMessage> messages;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @JsonIgnore
    @Version
    private Integer version;

    @PrePersist
    protected void prePersist() {
        createdAt = new Date();
        admins.add(owner);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public Set<Profile> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Profile> admins) {
        this.admins = admins;
    }

    public Set<Profile> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Profile> followers) {
        this.followers = followers;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommunityMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<CommunityMessage> messages) {
        this.messages = messages;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Community{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", owner=" + owner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        return Objects.equals(title, community.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
