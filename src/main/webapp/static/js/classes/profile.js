function Profile() {
    this.name = "";
    this.dateOfBirth = "";
    this.phone = "";
    this.country = "";
    this.city = "";
    this.info = "";
}

function Profile(name, dateOfBirth, phone, country, city, info) {
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.phone = phone;
    this.country = country;
    this.city = city;
    this.info = info;
}

function copy(profile) {
    if (profile == null)
        return null;
    
    return new Profile(
        profile.name, 
        profile.dateOfBirth, 
        profile.phone,
        profile.country,
        profile.city,
        profile.info
    );
}

function copyTo(profileFrom, profileTo) {
    if (profileFrom == null)
        return null;

    profileTo.name = profileFrom.name;
    profileTo.dateOfBirth = profileFrom.dateOfBirth;
    profileTo.phone = profileFrom.phone;
    profileTo.country = profileFrom.country;
    profileTo.city = profileFrom.city;
    profileTo.info = profileFrom.info;
    
    return profileTo;
}

function toString(profile) {
    return JSON.stringify(profile);
}
