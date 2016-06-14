function Profile() {
    this.id = 0;
    this.name = "";
    this.dateOfBirth = "";
    this.phone = "";
    this.country = "";
    this.currentCity = "";
    this.info = "";
}

function Profile(id, name, dateOfBirth, phone, country, city, info) {
    this.id = id;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.phone = phone;
    this.country = country;
    this.currentCity = city;
    this.info = info;
}

function copy(profile) {
    if (profile == null)
        return null;
    
    return new Profile(
        profile.id, 
        profile.name, 
        profile.dateOfBirth, 
        profile.phone,
        profile.country,
        profile.currentCity,
        profile.info
    );
}

function copyTo(profileFrom, profileTo) {
    if (profileFrom == null)
        return null;
    
    profileTo.id = profileFrom.id;
    profileTo.name = profileFrom.name;
    profileTo.dateOfBirth = profileFrom.dateOfBirth;
    profileTo.phone = profileFrom.phone;
    profileTo.country = profileFrom.country;
    profileTo.currentCity = profileFrom.currentCity;
    profileTo.info = profileFrom.info;
    
    return profileTo;
}

function toString(profile) {
    return JSON.stringify(profile);
}
