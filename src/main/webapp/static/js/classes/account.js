function Account() {
    this.id = 0;
    this.login = '';
    this.email = '';
    this.password = '';
}

function Account(id, email) {
    this.id = id;
    this.login = '';
    this.email = email;
    this.password = '';
}

function Account(id, login, email) {
    this.id = id;
    this.login = login;
    this.email = email;
}

function copy(account) {
    return new Account(account.id, account.login, account.email);
}
