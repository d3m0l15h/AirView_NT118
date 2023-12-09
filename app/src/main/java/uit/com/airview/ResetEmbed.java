package uit.com.airview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class ResetEmbed extends AppCompatActivity {
    private static final int CHECK_INTERVAL_MS = 500; // Every half a second
    private static final int TIMEOUT_MS = 10000; // 10 seconds

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_embed);
        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);

        //Get data
        String username = getIntent().getStringExtra("username");
        String oldPassword = getIntent().getStringExtra("oldPassword");
        String newPassword = getIntent().getStringExtra("newPassword");
        String confirmPassword = getIntent().getStringExtra("confirmPassword");

        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().removeSessionCookies(null);

        //Configure webview setting
        WebView webview = findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);

        //Load url
        webview = findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("swagger")) {
                    clickButtonWhenAvailable(view);
                }

                if (url.contains("openid-connect/auth") || url.contains("login-actions/authenticate")) {
                    String userScript = "document.getElementById('username').value = '" + username + "';";
                    String pwdScript = "document.getElementById('password').value = '" + oldPassword + "';";

                    view.evaluateJavascript(userScript, null);
                    view.evaluateJavascript(pwdScript, null);
                    view.evaluateJavascript("document.querySelector('button[name=\"login\"]').click();", null);
                }

                if (url.contains("execution=UPDATE_PASSWORD")) {
                    String newPwdScript = "document.getElementById('password-new').value = '" + newPassword + "';";
                    String confirmPwdScript = "document.getElementById('password-confirm').value = '" + confirmPassword + "';";

                    view.evaluateJavascript(newPwdScript, null);
                    view.evaluateJavascript(confirmPwdScript, null);

                    view.evaluateJavascript("document.querySelector('.btn.waves-effect.waves-light').click();", null);
                    Toast.makeText(ResetEmbed.this, R.string.resetSucc, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        webview.loadUrl("https://uiot.ixxc.dev/swagger/#/");
    }

    private void clickButtonWhenAvailable(WebView webView) {
        final long startTime = System.currentTimeMillis();

        final Runnable checkInitialButtonExistence = new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript(
                        "(function(selector) { return !!document.querySelector(selector); })('" + ".btn.authorize.unlocked" + "');",
                        value -> {
                            // If button exists or we've reached timeout, try clicking or exit
                            if ("true".equals(value) || System.currentTimeMillis() - startTime > TIMEOUT_MS) {
                                webView.evaluateJavascript("document.querySelector('" + ".btn.authorize.unlocked" + "').click();", null);
                                webView.evaluateJavascript("document.querySelector('.btn.modal-btn.auth.authorize.button').click();", null);
                            } else {
                                // Otherwise, keep checking
                                new Handler().postDelayed(this, CHECK_INTERVAL_MS);
                            }
                        }
                );
            }
        };
        // Start the checking
        new Handler().postDelayed(checkInitialButtonExistence, CHECK_INTERVAL_MS);
    }
}
