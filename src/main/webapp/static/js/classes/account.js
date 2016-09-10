function Account() {
    this.login = '';
    this.email = '';
    this.password = '';
    this.oldPassword = '';
}

function Account(email) {
    this.login = '';
    this.email = email;
    this.password = '';
    this.oldPassword = '';
}

function Account(login, email) {
    this.login = login;
    this.email = email;
    this.password = '';
    this.oldPassword = '';
}

function copy(account) {
    return new Account(account.login, account.email);
}
