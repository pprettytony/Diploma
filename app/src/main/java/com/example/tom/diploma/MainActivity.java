package com.example.tom.diploma;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "MainActivity";

    private SignInButton signIn;
    private TextView textView;
    public static final int REQ_CODE = 9001;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.sign_in_google);
        textView = findViewById(R.id.text_view);
        signIn.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            Toast.makeText(this, "Activity result is incorrect", Toast.LENGTH_LONG).show();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    public void onClick(View v) {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            textView.setText(account.getEmail());
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.putExtra("email", account.getEmail());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Can't update UI", Toast.LENGTH_SHORT).show();
        }
    }
}
