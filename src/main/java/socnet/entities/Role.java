package socnet.entities;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");
    
    
    String role;
    
    Role(String role) {
        this.role = role;
    }
    
    public String getRoleTitle() {
        return role;
    }
}
