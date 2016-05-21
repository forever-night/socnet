package socnet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import socnet.entities.interfaces.Message;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by anna on 14/03/16.
 */
@Entity
@Table(name = "public_message")
public class PublicMessage implements Serializable, Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Profile sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Profile receiver;

    @Column(name = "text_content")
    private String textContent;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @JsonIgnore
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

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public Profile getReceiver() {
        return receiver;
    }

    public void setReceiver(Profile location) {
        this.receiver = location;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PublicMessage{" +
                "id=" + id +
                ", sender=" + sender +
                ", location=" + receiver +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicMessage that = (PublicMessage) o;
        return Objects.equals(sender, that.sender) &&
                Objects.equals(receiver, that.receiver) &&
                Objects.equals(textContent, that.textContent) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, textContent, createdAt);
    }
}
