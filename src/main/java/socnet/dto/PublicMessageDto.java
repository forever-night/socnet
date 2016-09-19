package socnet.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class PublicMessageDto implements Serializable {
    private Integer id;
    private String senderLogin;
    private String textContent;
    private Date createdAt;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getSenderLogin() {
        return senderLogin;
    }
    
    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicMessageDto that = (PublicMessageDto) o;
        return Objects.equals(senderLogin, that.senderLogin) &&
                Objects.equals(textContent, that.textContent);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(senderLogin, textContent);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PublicMessageDto{");
        sb.append("senderLogin='").append(senderLogin).append('\'');
        sb.append(", textContent='").append(textContent).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
