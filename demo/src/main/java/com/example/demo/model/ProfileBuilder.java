package com.example.demo.model;

public class ProfileBuilder {

    private final Profile profile;

    public ProfileBuilder() {
        this.profile = new Profile();
    }

    public ProfileBuilder fullName(String fullName) {
        profile.setFullName(fullName);
        return this;
    }

    public ProfileBuilder email(String email) {
        profile.setEmail(email);
        return this;
    }

    public ProfileBuilder phone(String phone) {
        profile.setPhone(phone);
        return this;
    }

    public ProfileBuilder department(String department) {
        profile.setDepartment(department);
        return this;
    }

    public ProfileBuilder type(ProfileType type) {
        profile.setType(type);
        return this;
    }

    public Profile build() {
        return profile;
    }
}