package uit.com.airview.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    //private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE2OTYyMTE4NDEsImlhdCI6MTY5NjEyNzgyMCwiYXV0aF90aW1lIjoxNjk2MTI1NDQxLCJqdGkiOiIyZGRlMzNkZC02OTI4LTRmNWQtYTVlMi0yMGY5NzFmMDg3YTYiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiN2E5ODYwMTctY2YzOS00OWUwLWI4N2QtNzA0NzFiYjBlN2MyIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI3YTk4NjAxNy1jZjM5LTQ5ZTAtYjg3ZC03MDQ3MWJiMGU3YzIiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyIFRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IlVzZXIiLCJmYW1pbHlfbmFtZSI6IlRlc3QiLCJlbWFpbCI6InVzZXJAaXh4Yy5kZXYifQ.jm-XMekdZiz3oWOdHGeWDyWfAfpE0TAC_ZAAsjl1SzKL2hPwyy0dukZ6t8iRrjD-frxkP0_nVEANjvhN_pd1WHoqiqbONQHqmWPFGuNkoP-sWXG7JAoHBu-yUDVVEOdzbvWymjhs8LRknb_Vle6IOvfBhw9bKvWH4GAKdEaUtEu5JHYVxw42ajNP-CEF5NyBNYwofrDd1RsFsbtlKlXPwmNbFhz2EEUcbFY0nvzZvjt7VQX96n_xTwmWB0h59_dEi0Om2QcIQvHNvTFb7d0jBWXhHpsBx02pmR6g71f7O1qGJJG3Y-0Kky9G8nPgOeb2EpII7orhY_-vfHtx60eCNg";

    private static OkHttpClient getUnsafeOkHttpClient(Context context){
        try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("CustomX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //Log
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);

            SharedPreferences sharedPreferences = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
            builder.addInterceptor(new TokenInterceptor(sharedPreferences));
            //Bear token
//            builder.addInterceptor(chain -> {
//                Request newRequest  = chain.request().newBuilder()
//                        .addHeader("Authorization", "Bearer " + token)
//                        .build();
//                return chain.proceed(newRequest);
//            });


            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Retrofit getClient(Context context) {
        if(retrofit == null) {
            OkHttpClient client = getUnsafeOkHttpClient(context);

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://uiot.ixxc.dev/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
