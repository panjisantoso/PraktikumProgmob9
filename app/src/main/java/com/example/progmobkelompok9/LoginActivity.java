package com.example.progmobkelompok9;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Auth;
import com.example.progmobkelompok9.model.User;
import com.example.progmobkelompok9.util.SqliteHelper;
import com.example.progmobkelompok9.util.StringFixed;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//import android.support.design.widget.Snackbar;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    //Declaration EditTexts
    EditText editTextEmail;
    EditText editTextPassword;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    //Declaration Button
    Button buttonLogin;

    //Declaration SqliteHelper
//    SqliteHelper sqliteHelper;

    String username,password;
    ProgressDialog progressDoalog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN, Context.MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();

//        sqliteHelper = new SqliteHelper(this);
        initCreateAccountTextView();
        initViews();

        //set click event of login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check user input is correct or not
                if (validate()) {

                    //Get values from EditText fields
                    username = editTextEmail.getText().toString();
                    password = editTextPassword.getText().toString();
                    login();
                }
            }
        });


    }

    private void login(){
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMessage("Login....");
        progressDoalog.show();

        ApiClient.getClient()
                .create(ApiService.class)
                .login(username,password)
                .enqueue(new Callback<Auth>() {
                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response) {
                        try {
                            User user = response.body().getUser();
                            editorPreferences.putString(StringFixed.KEY_ID_USER,user.getIdUser());
                            editorPreferences.putString(StringFixed.KEY_NAME,user.getNamaUser());
                            editorPreferences.putString(StringFixed.KEY_ALAMAT,user.getAlamat());
                            editorPreferences.putString(StringFixed.KEY_TGL_LAHIR,user.getTglLahir());
                            editorPreferences.putString(StringFixed.KEY_USERNAME,user.getUsername());
                            editorPreferences.putString(StringFixed.KEY_PASSWORD,user.getPassword());
                            editorPreferences.putString(StringFixed.KEY_IMAGE,user.getImage());
                            editorPreferences.putString(StringFixed.KEY_FCM_TOKEN,user.getFcmToken());
                            editorPreferences.putString(StringFixed.KEY_TIPE_USER,user.getTipeUser());
                            editorPreferences.putString(StringFixed.KEY_STATUS,user.getStatusAktif());
                            editorPreferences.putBoolean(StringFixed.KEY_SESSION,true);
                            editorPreferences.apply();

                            progressDoalog.dismiss();
                            Snackbar.make(buttonLogin, "Login Success", Snackbar.LENGTH_LONG).show();

                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        catch (Exception e){
                            Log.e("error",e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t) {
                        progressDoalog.dismiss();
                        Snackbar.make(buttonLogin, "Failed to log in , please try again", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    //this method used to set Create account TextView text and click event( maltipal colors
    // for TextView yet not supported in Xml so i have done it programmatically)
    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#0c0099'>create one</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

    }

    //This method is for handling fromHtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid password!");
        }

        return valid;
    }


}