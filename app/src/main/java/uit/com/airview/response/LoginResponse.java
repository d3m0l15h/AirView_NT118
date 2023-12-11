package uit.com.airview.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("expires_in")
    private String expires_in;
    @SerializedName("refresh_token")
    private String refresh_token;
    public String getAccess_token() {
        return access_token;
    }
    public String getExpires_in() {
        return expires_in;
    }
    public String getRefresh_token() {
        return refresh_token;
    }
}
