package de.fu_berlin.agdb.data;

public class User {

    private String email, username, password;

    public User() {

    }


    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public void setEmail(String name) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
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

    public String getPassword() {
        return this.password;
    }


}
