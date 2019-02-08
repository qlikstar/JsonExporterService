package com.decision.engines.exporter.api;

public enum ParamErrors {

    MISSING_USERNAME_AND_EMAIL("accountName", "Missing both user name and email address"),
    USER_NAME("userName", "Invalid user name"),
    EMAIL_ADDRESS("email", "Invalid email address"),
    PASSWORD("passwordHash", "Invalid password hash"),
    PHONE_NUMBER("phone", "Invalid phone number"),
    FIRST_NAME("firstName", "Invalid first name"),
    LAST_NAME("lastName", "Invalid last name"),
    TOKEN_EXPIRE_DURATION("tokenExpireDuration", "Invalid token expiry duration"),
    REDIRECT_URL("redirectUrl", "Invalid redirect URL");

    private final String name;
    private final String desc;

    private ParamErrors(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
