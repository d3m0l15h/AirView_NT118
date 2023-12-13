package uit.com.airview.response;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("realm")
    private String realm;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("realmId")
    private String realmId;
    @SerializedName("email")
    private String email;
    @SerializedName("createdOn")
    private long createdOn;
    @SerializedName("serviceAccount")
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

    public long getCreatedOn() {
        return createdOn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
