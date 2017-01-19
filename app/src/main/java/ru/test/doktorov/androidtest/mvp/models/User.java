package ru.test.doktorov.androidtest.mvp.models;

public class User {
    public int id;
    public String login;
    public String avatar_url;

    public int getId() {
        return id;
    }
    public String getName() {
        return login;
    }
    public String getAvatarUrl() {
        return avatar_url;
    }
}
