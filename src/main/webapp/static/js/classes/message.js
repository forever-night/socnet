function Message() {
    this.id = 0;
    this.senderLogin = '';
    this.textContent = '';
    this.createdAt = null;
}

function Message(senderLogin, textContent, createdAt) {
    this.id = 0;
    this.senderLogin = senderLogin;
    this.textContent = textContent;
    this.createdAt = createdAt;
}

function Message(id, senderLogin, textContent, createdAt) {
    this.id = id;
    this.senderLogin = senderLogin;
    this.textContent = textContent;
    this.createdAt = createdAt;
}
