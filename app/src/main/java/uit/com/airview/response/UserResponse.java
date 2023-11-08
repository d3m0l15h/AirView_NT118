package uit.com.airview.response;

public class UserResponse {
    private String id;
    private String realm;
    private String realmId;
    private String email;
    private long createdOn;
    private boolean serviceAccount;
    public String username;
    public String getId() {
        return id;
    }

    public String getRealm() {
        return realm;
    }

    public String getRealmId() {
        return realmId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
