package uit.com.airview.util;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import uit.com.airview.model.LoginResponse;

public interface APIInterface {
    @POST("auth/realms/master/protocol/openid-connect/token")
    @FormUrlEncoded
    Call<LoginResponse> login(
            @Field("client_id") String clientId,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );
    //@GET("api/master/asset/{assetID}")
    //Call<Asset> getAsset(@Path("assetID") String assetID, @Header("Authorization") String auth);
}
