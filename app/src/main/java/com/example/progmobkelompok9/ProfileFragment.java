package com.example.progmobkelompok9;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Auth;
import com.example.progmobkelompok9.model.User;
import com.example.progmobkelompok9.util.DateFormat;
import com.example.progmobkelompok9.util.DrawerMenu;
import com.example.progmobkelompok9.util.StringFixed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    TextView mUsername, mTipe;
    EditText mName, mAlamat, mTglLahir;
    FloatingActionButton mEdit, mSave, mCancel;
    CircleImageView mImage;
    ImageView mEditImage;

    String username,name,image,tglLahir,alamat,tipeUser;

    ProgressDialog progressDoalog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    MultipartBody.Part mBimage;
    private static int RESULT_LOAD_IMAGE = 1;

    View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(StringFixed.KEY_LOGIN, Context.MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();

        username = sharedPreferences.getString(StringFixed.KEY_USERNAME,"");
        name = sharedPreferences.getString(StringFixed.KEY_NAME,"");
        image = sharedPreferences.getString(StringFixed.KEY_IMAGE,"");
        alamat = sharedPreferences.getString(StringFixed.KEY_ALAMAT,"");
        tglLahir = sharedPreferences.getString(StringFixed.KEY_TGL_LAHIR,"");
        tipeUser = sharedPreferences.getString(StringFixed.KEY_TIPE_USER,"");

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Profile");
        ab.setDisplayHomeAsUpEnabled(true);

        DrawerMenu drawer = new DrawerMenu();
        drawer.createDrawer(getContext(), (AppCompatActivity) getActivity(), toolbar);

        setting();

        return rootView;
    }

    private void setting(){
        mUsername = rootView.findViewById(R.id.profile_username);
        mTipe = rootView.findViewById(R.id.profile_tipeUser);
        mName = rootView.findViewById(R.id.profile_name);
        mAlamat = rootView.findViewById(R.id.profile_alamat);
        mTglLahir = rootView.findViewById(R.id.profile_tglLahir);
        mImage = rootView.findViewById(R.id.profile_image);
        mEditImage = rootView.findViewById(R.id.profile_editImage);

        mSave = rootView.findViewById(R.id.profile_floatingActionButtonSave);
        mCancel = rootView.findViewById(R.id.profile_floatingActionButtonCancel);
        mEdit = rootView.findViewById(R.id.profile_floatingActionButton);

        mUsername.setText(username);
        mTipe.setText(tipeUser);
        mName.setText(name);
        mAlamat.setText(alamat);
        mTglLahir.setText(tglLahir);

        mSave.hide();
        mCancel.hide();
        mEditImage.setVisibility(View.GONE);
        mEdit.show();

        mName.setEnabled(false);
        mAlamat.setEnabled(false);
        mTglLahir.setClickable(false);
        mTglLahir.setEnabled(false);

        Glide.with(getActivity().getApplicationContext())
                .load(BuildConfig.BASE_URL_IMAGE+image)
                .centerCrop()
                .placeholder(R.drawable.avatar)
                .into(mImage);

        mTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSave.show();
                mCancel.show();
                mEditImage.setVisibility(View.VISIBLE);
                mEdit.hide();
                mName.setEnabled(true);
                mAlamat.setEnabled(true);
                mTglLahir.setClickable(true);
                mTglLahir.setEnabled(true);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSave.hide();
                mCancel.hide();
                mEditImage.setVisibility(View.GONE);
                mEdit.show();
                mName.setText(name);
                mAlamat.setText(alamat);
                mTglLahir.setText(sharedPreferences.getString(StringFixed.KEY_TGL_LAHIR,""));
                Glide.with(getActivity().getApplicationContext())
                        .load(BuildConfig.BASE_URL_IMAGE+image)
                        .centerCrop()
                        .into(mImage);

                mName.setEnabled(false);
                mAlamat.setEnabled(false);
                mTglLahir.setClickable(false);
                mTglLahir.setEnabled(false);
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDoalog = new ProgressDialog(getContext());
                progressDoalog.setMessage("Saving....");
                progressDoalog.show();

                mSave.hide();
                mCancel.hide();
                mEditImage.setVisibility(View.GONE);
                mEdit.show();
                mName.setEnabled(false);
                mAlamat.setEnabled(false);
                mTglLahir.setClickable(false);
                mTglLahir.setEnabled(false);
                save();
            }
        });

        mEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    private void getDate(){
        final Calendar myCalendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            myCalendar.setTime(sdf.parse(tglLahir));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final DateFormat dateFormat = new DateFormat();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.add(Calendar.DATE, 1);
                mTglLahir.setText(dateFormat.IDN(myCalendar));
                tglLahir = dateFormat.IDN(myCalendar);
            }
        };

        new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void save(){
        HashMap<String, RequestBody> map = new HashMap<>();
        Log.e("id",sharedPreferences.getString(StringFixed.KEY_ID_USER,""));
        map.put(StringFixed.KEY_ID_USER, RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferences.getString(StringFixed.KEY_ID_USER,"")));
        map.put(StringFixed.KEY_NAME, RequestBody.create(okhttp3.MultipartBody.FORM, mName.getText().toString()));
        map.put(StringFixed.KEY_ALAMAT, RequestBody.create(okhttp3.MultipartBody.FORM, mAlamat.getText().toString()));
        map.put(StringFixed.KEY_TGL_LAHIR, RequestBody.create(okhttp3.MultipartBody.FORM, tglLahir));
//        map.put(StringFixed.KEY_USERNAME, RequestBody.create(okhttp3.MultipartBody.FORM, username));

        ApiClient.getClient()
                .create(ApiService.class)
                .editProfile(mBimage,map)
                .enqueue(new Callback<Auth>() {
                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response) {
                        Log.e("get",response.message());
                        try {

                            User user = response.body().getUser();
                            editorPreferences.putString(StringFixed.KEY_NAME,user.getNamaUser());
                            editorPreferences.putString(StringFixed.KEY_ALAMAT,user.getAlamat());
                            editorPreferences.putString(StringFixed.KEY_TGL_LAHIR,user.getTglLahir());
                            editorPreferences.putString(StringFixed.KEY_USERNAME,user.getUsername());
                            editorPreferences.putString(StringFixed.KEY_IMAGE,user.getImage());
                            editorPreferences.putString(StringFixed.KEY_TIPE_USER,user.getTipeUser());
                            editorPreferences.putString(StringFixed.KEY_STATUS,user.getStatusAktif());
                            editorPreferences.apply();

                            progressDoalog.dismiss();

//                            Snackbar.make(buttonRegister, "Register Success, and you will automatically Loged", Snackbar.LENGTH_LONG).show();


                        }catch (Exception e){
                            progressDoalog.dismiss();
//                            Snackbar.make(buttonRegister, "Something went wrong",Snackbar.LENGTH_LONG).show();
                            Log.e("error",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t) {
                        progressDoalog.dismiss();
//                        Snackbar.make(buttonRegister, "Something went wrong, please try again", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmapFoto = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                bitmapFoto = scaleDown(bitmapFoto,true);
                mImage.setImageBitmap(bitmapFoto);

                File file = createTempFile(bitmapFoto);
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                mBimage = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
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
        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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