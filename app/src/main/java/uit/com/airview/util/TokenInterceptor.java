package uit.com.airview.util;

import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import uit.com.airview.response.LoginResponse;

public class TokenInterceptor implements Interceptor {
    private SharedPreferences sharedPreferences;
    private String token;

    public TokenInterceptor(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        // Get the current time
        long currentTime = System.currentTimeMillis();

        //Get the token type
        String tokenType = sharedPreferences.getString("token_type", null);
        if(tokenType == null) {
            // If the token type is null, proceed with the original request
            return chain.proceed(originalRequest);
        }
        // Check if the token is null
        assert tokenType != null;
        if(tokenType.equals("admin")) {
            // If the admin token is null, proceed with the original request
            if(sharedPreferences.getString("admin_token", null) == null) {return chain.proceed(originalRequest);}

            // Check and refresh admin token if necessary
            long adminTokenExpirationTime = sharedPreferences.getLong("admin_token_expiration_time", 0);
            if(currentTime > adminTokenExpirationTime) {
                refreshAdminToken();
                sharedPreferences.edit().putLong("admin_token_expiration_time", currentTime + 86400).apply();
            }

            token = sharedPreferences.getString("admin_token", null);
        } else if(tokenType.equals("user")) {
            // If the user token is null, proceed with the original request
            if(sharedPreferences.getString("user_token", null) == null) {return chain.proceed(originalRequest);}

            // Check and refresh user token if necessary
            long userTokenExpirationTime = sharedPreferences.getLong("user_token_expiration_time", 0);
            if (currentTime > userTokenExpirationTime) {
                refreshUserToken();
                sharedPreferences.edit().putLong("user_token_expiration_time", currentTime + 86400).apply();
            }

            token = sharedPreferences.getString("user_token", null);
        }

        // Add the tokens to the request header
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(newRequest);
    }
    public void refreshAdminToken() {
        // Get the API service
        APIInterface apiInterface = APIClient.getClient(null).create(APIInterface.class);
        // Make a synchronous request to refresh the token
        try {
            Call<LoginResponse> call = apiInterface.login("openremote", "admin", "1", "password");
            retrofit2.Response<LoginResponse> response = call.execute();
            // If the request is successful, save the new token and return it
            if (response.isSuccessful() && response.body() != null) {
                sharedPreferences.edit().putString("admin_token", response.body().getAccess_token()).apply();
            } else {
                // Handle the case when the request is not successful
                // This will depend on your application's logic
            }
        } catch (IOException e) {
            // Handle the exception
            // This will depend on your application's logic
        }
    }

    public void refreshUserToken() {
        // Get the API service
        APIInterface apiInterface = APIClient.getClient(null).create(APIInterface.class);
        // Make a synchronous request to refresh the token
        try {
            Call<LoginResponse> call = apiInterface.login("openremote", sharedPreferences.getString("username","user"), sharedPreferences.getString("password","123"), "password");
            retrofit2.Response<LoginResponse> response = call.execute();
            // If the request is successful, save the new token and return it
            if (response.isSuccessful() && response.body() != null) {
                sharedPreferences.edit().putString("user_token", response.body().getAccess_token()).apply();
            } else {
                // Handle the case when the request is not successful
                // This will depend on your application's logic
            }
        } catch (IOException e) {
            // Handle the exception
            // This will depend on your application's logic
        }
    }
}
