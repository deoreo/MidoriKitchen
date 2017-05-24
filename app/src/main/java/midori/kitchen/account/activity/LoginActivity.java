package midori.kitchen.account.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.account.model.ModelUser;
import midori.kitchen.content.activity.HomeActivity;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.ConfigManager;
import midori.kitchen.manager.JSONControl;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;

/**
 * Created by M. Asrof Bayhaqqi on 3/10/2017.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Activity mActivity;
    //facebook
    private CallbackManager callbackManager;
    private static final int facebookSignUp = 64206;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //google
    private GoogleApiClient mGoogleApiClient;
    private static final int googleSignUp = 9001;

    private String id, fullname="-", email="-", photo="-", phone="-",password="-", address = "-";
    private AppPrefManager appPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFirebaseAuth();

        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplicationContext());

        //google
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appPrefManager = new AppPrefManager(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mActivity = this;
        // check is logged in
        if (appPrefManager.getIsLoggedIn()) {
            launchHome();
        }



    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @OnClick({R.id.btn_facebook, R.id.btn_google})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook:
                loginFacebook();
                break;
            case R.id.btn_google:
                loginGoogle();
                break;
        }
    }

    private void loginFacebook() {
        facebookSignOut();

        List<String> permissionNeeds = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");
        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                facebookSignOut();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                facebookSignOut();
            }
        });
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            GraphRequest request = GraphRequest.newMeRequest(
                                    token,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.d("facebookResponse", response.toString());
                                            try {
                                                if (object.has("id")) {
                                                    id = object.getString("id");
                                                } else {
                                                    id = "";
                                                }
                                                if (object.has("name")) {
                                                    fullname = object.getString("name");
                                                } else {
                                                    fullname = "";
                                                }
                                                if (object.has("email")) {
                                                    email = object.getString("email");
                                                } else {
                                                    email = "";
                                                }
                                                photo = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                                Log.d("facebookAccount", id + "/" + fullname + "/" + email + "/" + photo);
                                                appPrefManager.setIsLoggedIn(true);
                                                appPrefManager.setUser(id, fullname, email, photo);

                                                launchHome();
                                            } catch (Exception e) {
                                                Log.d("facebookResponseError", e.getMessage());
                                            }

                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id, name, email, gender, picture, birthday");

                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                    }
                });
    }

    private void facebookSignOut() {
        LoginManager.getInstance().logOut();
    }

    private void loginGoogle() {
        googleSignOut();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, googleSignUp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == googleSignUp) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                googleSignOut();
            }
        } else if (requestCode == facebookSignUp) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            id = acct.getId();
                            fullname = acct.getDisplayName();
                            email = acct.getEmail();
                            photo = acct.getPhotoUrl().toString();
                            Log.d("googleAccount", id + "/" + fullname + "/" + email + "/" + photo);
                            appPrefManager.setIsLoggedIn(true);
                            appPrefManager.setUser(id, fullname, email, photo);
                            launchHome();
                        }
                    }
                });
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        googleRevokeAccess();
                        Log.d("Google", "signOut");

                    }
                });
    }

    private void googleRevokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("Google", "revokeAccess");
                    }
                });
    }

    private void launchHome() {
        new DoRegister(mActivity).execute(
                fullname,
                email,
                password,
                phone,
                address
        );
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    private class DoRegister extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;

        public DoRegister(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String name = params[0];
                String email = params[1];
                String password = params[2];
                String phone = params[3];
                String address = params[4];

                if(password.isEmpty()){
                    Random r = new Random();
                    int pass = r.nextInt(999999);
                    password = ""+pass;
                }

                JSONControl jsControl = new JSONControl();
                JSONObject responseRegister = jsControl.postRegister(name, email, password, phone, address);
                Log.d("json responseRegister", responseRegister.toString());

                if (!responseRegister.toString().contains("error")) {
                    ModelUser user = new ModelUser();
                    user.setNama(name);
                    user.setPonsel(phone);
                    user.setEmail(email);
                    user.setApi_key(responseRegister.getString("user_api_key"));
                    appPrefManager.setUserApiKey(user.getApi_key());


                    return "OK";
                } else if(responseRegister.toString().contains("email already existed")){
                    JSONObject responseLogin = jsControl.postLogin(email, password);
                    if (!responseLogin.toString().contains("error")) {
                        ModelUser user = new ModelUser();
                        user.setNama(responseLogin.getString("name"));
                        user.setPonsel(responseLogin.getString("telepon"));
                        user.setEmail(responseLogin.getString("email"));
                        user.setApi_key(responseLogin.getString("apiKey"));
                        appPrefManager.setUserApiKey(user.getApi_key());
                    }
                    return "OK";
                }

                else {
                    return "FAIL";
                }




            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    break;
                case "OK":
                    break;
            }
        }
    }
}
