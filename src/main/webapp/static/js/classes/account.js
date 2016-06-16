function Account() {
    this.id = 0;
    this.email = '';
    this.password = '';
}

function Account(id, email) {
    this.id = id;
    this.email = email;
    this.password = '';
}

function copy(account) {
    return new Account(account.id, account.email);
}
