package jwtcommon.security;

public class CustomPrincipal {
    private final String userId;
    private final String username;

    public CustomPrincipal(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}

