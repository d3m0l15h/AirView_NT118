package uit.com.airview.util;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uit.com.airview.model.Asset.Asset;
import uit.com.airview.model.Asset2.Asset2;
import uit.com.airview.model.OpenWeather.OpenWeather;
import uit.com.airview.model.PM.PM;
import uit.com.airview.model.RegisterUserBody;
import uit.com.airview.model.ResetPasswordBody;
import uit.com.airview.response.LoginResponse;
import uit.com.airview.response.UserResponse;

public interface APIInterface {
    @POST("auth/realms/master/protocol/openid-connect/token")
    @FormUrlEncoded
    Call<LoginResponse> login(
            @Field("client_id") String clientId,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );
    @PUT("api/master/user/master/reset-password/{userId}")
    Call<Void> resetPassword(
            @Header("Authorization") String authorization,
            @Header("Accept") String accept,
            @Header("Content-Type") String contentType,
            @Path("userId") String userID,
            @Body ResetPasswordBody body
    );
    @GET("api/master/user/user")
    Call<UserResponse> getUser(
            @Header("Authorization") String authToken
    );
    @PUT("api/master/user/master/userRoles/{userId}")
    Call<Void> registerUserRole(
            @Header("Authorization") String authorization,
            @Header("Accept") String accept,
            @Header("Content-Type") String contentType,
            @Path("userId") String userID,
            @Body List<RegisterUserBody> body
    );
    @PUT("api/master/user/master/userRealmRoles/{userId}")
    Call<Void> registerUserRealmRole(
            @Header("Authorization") String authorization,
            @Header("Accept") String accept,
            @Header("Content-Type") String contentType,
            @Path("userId") String userID,
            @Body List<RegisterUserBody> body
    );
    @GET("api/master/asset/user/current")
    Call<List<Asset2>> getCurrentAsset(
            @Header("Authorization") String authorization,
            @Header("Accept") String accept
    );
    @GET("api/master/asset/5zI6XqkQVSfdgOrZ1MyWEf")
    Call<Asset2> getAsset2(@Header("Authorization") String auth);
    @GET("api/master/asset/6Wo9Lv1Oa1zQleuRVfADP4")
    Call<Asset> getAsset(@Header("Authorization") String auth);
    @GET("api/master/asset/4lt7fyHy3SZMgUsECxiOgQ")
    Call<PM> getPM(@Header("Authorization") String auth);

    @GET("data/2.5/weather?appid=a32d0e3fb716aac8a290124d70758e9d")
    Call<OpenWeather> getWeatherData(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units
    );
}
