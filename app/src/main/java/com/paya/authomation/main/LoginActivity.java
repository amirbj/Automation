package com.paya.authomation.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paya.authomation.connections.FirstConnection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    static final String uri = "http://pendar.demo.payasaas.ir/";
    AppCompatEditText txtUsername;
    AppCompatEditText txtPassword;
    AppCompatButton btnLogin;
    ImageView imgLogo;
    String Username;
    String Password;
    String Pass;
    String Usern;
    String Response;
    ProgressDialog progress, pro;
    List<Letters> items = new ArrayList<>();
    ArrayList<String> arr = new ArrayList<>();
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;
    Letters letter = new Letters();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUsername = (AppCompatEditText) findViewById(R.id.input_email);
        txtPassword = (AppCompatEditText) findViewById(R.id.input_pass);
        imgLogo = (ImageView) findViewById(R.id.ivLogo);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtUsername.setText(loginPreferences.getString("username", ""));
            txtPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:


                if (isOnline()) {
                    if (validate()) {
                        rememberUser();
                        Usertask task = new Usertask();
                        task.execute();
                    }
                }


        }
    }

    private void rememberUser() {
        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", Username);
            loginPrefsEditor.putString("password", Password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }


    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Toast.makeText(this, "اینترنت شما وصل نیست", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    public void onLoginSuccess() {

        finish();
        Intent i = new Intent(this, AfterLoginActivity.class);
        i.putExtra("username", Username);

     //   i.putStringArrayListExtra("items", arr);


        startActivity(i);
    }

    public boolean validate() {

        boolean valid = true;
        Username = txtUsername.getText().toString();
        Password = txtPassword.getText().toString();
        if (Username.isEmpty()) {
            txtUsername.setError("نام کاربری وارد نشده است");
            valid = false;
        }

        if (Password.isEmpty()) {
            txtPassword.setError("رمز عبور وارد نشده است");
            valid = false;

        }
        if (Password.length() > 100) {
            txtPassword.setError("رمز عبور نا معتبر است");
            valid = false;
        }
        return valid;

    }



    private class Usertask extends AsyncTask<Void, String, String> {


        @Override
        protected void onPreExecute() {

            progress = ProgressDialog.show(LoginActivity.this, "در خواست ورود", "لطفا منتظر بمانید", true);
        }

        @Override
        protected String doInBackground(Void... strings) {
            FirstConnection conect = new FirstConnection();
            try {


                Response = conect.getContent(Username, Password, getApplicationContext());

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return Response;
        }

        @Override
        protected void onPostExecute(String res) {

            if (res != null) {
                FirstConnection conect = new FirstConnection();
                boolean test = conect.Authenticate(res);

                if (conect.Authenticate(res)) {

                    onLoginSuccess();



                } else {

                    Toast.makeText(getApplicationContext(), "ورود نا موفق...", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "از سرور جوابی دریافت نشد",Toast.LENGTH_LONG).show();
            }
            progress.dismiss();

        }

    }


}
