package socnet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import socnet.entities.Account;


public class PasswordChangeDto {
    Account account;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String newPassword;

    public PasswordChangeDto() {}

    public PasswordChangeDto(Account account, String newPassword) {
        this.account = account;
        this.newPassword = newPassword;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return account.toString() + " old p: " + account.getPassword() + " new p: " + newPassword;
    }
}
