function Account() {
    this.login = '';
    this.email = '';
    this.password = '';
}

function Account(email) {
    this.login = '';
    this.email = email;
    this.password = '';
}

function Account(login, email) {
    this.login = login;
    this.email = email;
    this.password = '';
}

function copy(account) {
    return new Account(account.login, account.email);
}
