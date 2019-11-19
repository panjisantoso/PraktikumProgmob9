package com.example.progmobkelompok9;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Auth;
import com.example.progmobkelompok9.model.ResponseMessage;
import com.example.progmobkelompok9.model.User;
import com.example.progmobkelompok9.util.DateFormat;
import com.example.progmobkelompok9.util.SqliteHelper;
import com.example.progmobkelompok9.util.StringFixed;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {

    //Declaration EditTexts
    EditText mUsername,mPassword,mAlamat,mName,mTglLahir;
    ImageView mImage;

    //Declaration TextInputLayout
    TextInputLayout mLayoutUsername,mLayoutPassword,mLayoutAlamat,mLayoutName,mLayoutTglLahir;

    String username,password,alamat,name,tglLahir;
    //Declaration Button
    Button buttonRegister,browserBtn;
    MultipartBody.Part image;
    private static int RESULT_LOAD_IMAGE = 1;

    ProgressDialog progressDoalog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    //Declaration SqliteHelper
//    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        sqliteHelper = new SqliteHelper(this);
        initTextViewLogin();
        initViews();
    }

    //this method used to set Login TextView click event
    private void initTextViewLogin() {
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {

        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN, Context.MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();

        mPassword =  findViewById(R.id.register_password);
        mUsername =  findViewById(R.id.register_username);
        mName =  findViewById(R.id.register_nama);
        mTglLahir =  findViewById(R.id.register_tglLahir);
        mAlamat =  findViewById(R.id.register_alamat);
        mImage =  findViewById(R.id.register_image);

        mLayoutPassword = (TextInputLayout) findViewById(R.id.register_inputLayoutPassword);
        mLayoutUsername = (TextInputLayout) findViewById(R.id.register_inputLayoutUsername);
        mLayoutName = (TextInputLayout) findViewById(R.id.register_inputLayoutNama);
        mLayoutTglLahir = (TextInputLayout) findViewById(R.id.register_inputLayoutTglLahir);
        mLayoutAlamat = (TextInputLayout) findViewById(R.id.register_inputLayoutAlamat);

        buttonRegister = (Button) findViewById(R.id.register_button);
        browserBtn = findViewById(R.id.register_browser);

        browserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    save();
                }

            }
        });
    }

    private void getDate(){
        final Calendar myCalendar = Calendar.getInstance();
        final DateFormat dateFormat = new DateFormat();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mTglLahir.setText(dateFormat.IDN(myCalendar));
                tglLahir = dateFormat.IDN(myCalendar);
            }
        };

        new DatePickerDialog(RegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void save(){
        progressDoalog = new ProgressDialog(RegisterActivity.this);
        progressDoalog.setMessage("Register & Login....");
        progressDoalog.show();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(StringFixed.KEY_NAME, RequestBody.create(okhttp3.MultipartBody.FORM, name));
        map.put(StringFixed.KEY_ALAMAT, RequestBody.create(okhttp3.MultipartBody.FORM, alamat));
        map.put(StringFixed.KEY_TGL_LAHIR, RequestBody.create(okhttp3.MultipartBody.FORM, tglLahir));
        map.put(StringFixed.KEY_USERNAME, RequestBody.create(okhttp3.MultipartBody.FORM, username));
        map.put(StringFixed.KEY_PASSWORD, RequestBody.create(okhttp3.MultipartBody.FORM, password));

        ApiClient.getClient()
                .create(ApiService.class)
                .register(image,map)
                .enqueue(new Callback<Auth>() {
                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response) {
                        Log.e("get",response.message());
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
                            Snackbar.make(buttonRegister, "Register Success, and you will automatically Loged", Snackbar.LENGTH_LONG).show();

                            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            progressDoalog.dismiss();
                            Snackbar.make(buttonRegister, "Something went wrong",Snackbar.LENGTH_LONG).show();
                            Log.e("error",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t) {
                        progressDoalog.dismiss();
                        Snackbar.make(buttonRegister, "Something went wrong, please try again", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = true;

        //Get values from EditText fields
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
        name = mName.getText().toString();
        alamat = mAlamat.getText().toString();

        //Handling validation for UserName field
//        if (username.isEmpty()) {
//            valid = false;
//            mLayoutUsername.setError("Please enter valid username!");
//        } else {
//            if (UserName.length() > 5) {
//                valid = true;
//                textInputLayoutUserName.setError(null);
//            } else {
//                valid = false;
//                textInputLayoutUserName.setError("Username is to short!");
//            }
//        }
//
//        //Handling validation for Email field
//        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
//            valid = false;
//            textInputLayoutEmail.setError("Please enter valid email!");
//        } else {
//            valid = true;
//            textInputLayoutEmail.setError(null);
//        }
//
//        //Handling validation for Password field
//        if (Password.isEmpty()) {
//            valid = false;
//            textInputLayoutPassword.setError("Please enter valid password!");
//        } else {
//            if (Password.length() > 5) {
//                valid = true;
//                textInputLayoutPassword.setError(null);
//            } else {
//                valid = false;
//                textInputLayoutPassword.setError("Password is to short!");
//            }
//        }


        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmapFoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmapFoto = scaleDown(bitmapFoto,true);
                mImage.setImageBitmap(bitmapFoto);

                File file = createTempFile(bitmapFoto);
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap scaleDown(Bitmap realImage,
                                   boolean filter) {
        if (realImage.getWidth() > 1500 || realImage.getHeight() > 1500){
            Bitmap newbitmap = Bitmap.createScaledBitmap(realImage, 800,
                    800, filter);
            return newbitmap;
        }
        else{
            return realImage;
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , "image.PNG");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}