package de.fu_berlin.agdb.data;

/**
 * Created by Riva on 14.06.2016.
 */
public class TokenData {

    private String token;
    private String userEmail;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public TokenData(String token, String userEmail) {
        this.token = token;
        this.userEmail = userEmail;

    }
}
