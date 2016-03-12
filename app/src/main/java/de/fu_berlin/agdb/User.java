package de.fu_berlin.agdb;

public class User {

    String name, username, password;
    int age;

    public User(String name, int age, String username, String password) {
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this("", -1, username, password);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswort() {
        return this.password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }
}
