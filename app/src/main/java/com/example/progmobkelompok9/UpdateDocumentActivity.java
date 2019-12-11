package com.example.progmobkelompok9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Category;
import com.example.progmobkelompok9.model.ResponseMessage;
import com.example.progmobkelompok9.util.DateFormat;
import com.example.progmobkelompok9.util.FileHelper;
import com.example.progmobkelompok9.util.PermissionHelper;
import com.example.progmobkelompok9.util.StringFixed;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDocumentActivity extends AppCompatActivity {

    Button mBrowseBtn, mSaveBtn;
    Spinner mCategory, mTypeFile;
    EditText mNama, mPenulis, mPenerbit, mThnTerbit, mDeskripsi, mDocument;
    ImageView mImage;

    SharedPreferences sharedPreferences;
    String idUser,nama,penulis,penerbit,deskripsi,category,typeFile;
    String idDocument;
    String thnTerbit,path,image;
    String idUserDocument, namaCategory, namaUser, imageUser, fileType, totalView, totalDownload, tglUpload, statusAktif;
    MultipartBody.Part imageMB,fileMB;
    private static int RESULT_LOAD_IMAGE = 1, RESULT_LOAD_FILE = 100;

    private List<Category> categoryList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_document);

        typeList.add("PPT");
        typeList.add("PDF");
        typeList.add("DOC");
        typeList.add("XLS");

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Update Document");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        PermissionHelper.verifyStoragePermissions(this);

        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN, MODE_PRIVATE);
        idUser = sharedPreferences.getString(StringFixed.KEY_ID_USER,"");

        mBrowseBtn = findViewById(R.id.add_document_browser);
        mSaveBtn = findViewById(R.id.add_document_save);

        mCategory = findViewById(R.id.add_document_category);
        mTypeFile = findViewById(R.id.add_document_typeFile);
        mNama = findViewById(R.id.add_document_namaDocument);
        mPenulis = findViewById(R.id.add_document_penulis);
        mPenerbit = findViewById(R.id.add_document_penerbit);
        mThnTerbit = findViewById(R.id.add_document_tahunTerbit);
        mDeskripsi = findViewById(R.id.add_document_deskripsi);
        mDocument = findViewById(R.id.add_document_document);

        mImage = findViewById(R.id.add_document_image);

        settingData();

        mDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        mBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mThnTerbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(UpdateDocumentActivity.this);
                progressDialog.setMessage("saving....");
                progressDialog.show();
                save();
            }
        });

        getCategory();
        spinnerTypeFile();

    }

    private void save(){
        nama = mNama.getText().toString();
        penulis = mPenulis.getText().toString();
        penerbit = mPenerbit.getText().toString();
        deskripsi = mDeskripsi.getText().toString();
        String thn = String.valueOf(thnTerbit);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(StringFixed.KEY_ID_DOCUMENT, RequestBody.create(okhttp3.MultipartBody.FORM, idDocument));
        map.put(StringFixed.KEY_NAMA_DOCUMENT, RequestBody.create(okhttp3.MultipartBody.FORM, nama));
        map.put(StringFixed.KEY_ID_USER, RequestBody.create(okhttp3.MultipartBody.FORM, idUser));
        map.put(StringFixed.KEY_ID_CATEGORY, RequestBody.create(okhttp3.MultipartBody.FORM, category));
        map.put(StringFixed.KEY_FILE_TYPE, RequestBody.create(okhttp3.MultipartBody.FORM, typeFile));
        map.put(StringFixed.KEY_PENULIS, RequestBody.create(okhttp3.MultipartBody.FORM, penulis));
        map.put(StringFixed.KEY_PENERBIT, RequestBody.create(okhttp3.MultipartBody.FORM, penerbit));
        map.put(StringFixed.KEY_TAHUN_TERBIT, RequestBody.create(okhttp3.MultipartBody.FORM, thn));
        map.put(StringFixed.KEY_DESKRIPSI, RequestBody.create(okhttp3.MultipartBody.FORM, deskripsi));

        ApiClient.getClient()
                .create(ApiService.class)
                .editDocument(imageMB,fileMB,map)
                .enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateDocumentActivity.this,"Document Berhasil diUpdate",Toast.LENGTH_SHORT).show();
                        sendData();

                    }
                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("error",t.getMessage());
                    }
                });
    }

    private void spinnerTypeFile(){
        ArrayList<String> label = new ArrayList<>();
        int posisi = 0;
        for (int i =0;i<typeList.size();i++){
            label.add(typeList.get(i));
            if (typeFile.equals(typeList.get(i))){
                posisi = i;
            }
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, label);

        mTypeFile.setAdapter(adapter);
        typeFile = adapter.getItem(posisi);
        mTypeFile.setSelection(posisi, true);
        View v = mTypeFile.getSelectedView();
        ((TextView)v).setTextColor(getResources().getColor(R.color.md_black_1000));
        // mengeset listener untuk mengetahui saat item dipilih
        mTypeFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeFile = adapter.getItem(i);
                ((TextView) view).setTextColor(getResources().getColor(R.color.md_black_1000));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void spinnerCategory(){
        ArrayList<String> label = new ArrayList<>();
        int posisi = 0;
        for (int i =0;i<categoryList.size();i++){
            label.add(categoryList.get(i).getCategoryName());
            if (category.equals(categoryList.get(i).getCategoryName())){
                posisi = i;
            }
        }
        category = categoryList.get(posisi).getIdCategory();
        namaCategory = categoryList.get(posisi).getCategoryName();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, label);

        mCategory.setAdapter(adapter);
        mCategory.setSelection(posisi, true);
        View v = mCategory.getSelectedView();
        ((TextView)v).setTextColor(getResources().getColor(R.color.md_black_1000));
        // mengeset listener untuk mengetahui saat item dipilih
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categoryList.get(i).getIdCategory();
                namaCategory = categoryList.get(i).getCategoryName();
                ((TextView) view).setTextColor(getResources().getColor(R.color.md_black_1000));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    private void selectFile(){
        if (typeFile.equals("")){
            Toast.makeText(this, "Please select type file first",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent();
            intent.setType("application/"+typeFile);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(
                        Intent.createChooser(intent, "Select a File to Upload"),
                        RESULT_LOAD_FILE);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCategory(){
        ApiClient.getClient()
                .create(ApiService.class)
                .getCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        categoryList.addAll(response.body());
                        spinnerCategory();
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        Log.e("error",t.getMessage());
                    }
                });

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
                imageMB = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_OK && null != data){

            File file = new File(FileHelper.getPathFromURI(this,data.getData()));
            mDocument.setText(file.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileMB = MultipartBody.Part.createFormData("path",file.getName(),requestFile);

        }
    }

    public static Bitmap scaleDown(Bitmap realImage,
                                   boolean filter) {
        if (realImage.getWidth() > 1500 || realImage.getHeight() > 1500){
            Bitmap newbitmap = Bitmap.createScaledBitmap(realImage, 600,
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

    private void getDate(){
        final Calendar today = Calendar.getInstance();

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(UpdateDocumentActivity.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        // on date set }
                    }

                },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

        builder.setMinYear(1990)
                .setActivatedYear(Integer.parseInt(thnTerbit))
                .setMaxYear(2099)
                .showYearOnly()
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) { // on year selected
                        thnTerbit = Integer.toString(selectedYear);
                        mThnTerbit.setText(String.valueOf(selectedYear));
                    }
                })
                .build()
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void settingData(){
        Intent intent = getIntent();
        //penting
        idDocument = intent.getStringExtra(StringFixed.KEY_ID_DOCUMENT);
        nama = intent.getStringExtra(StringFixed.KEY_NAMA_DOCUMENT);
        penulis = intent.getStringExtra(StringFixed.KEY_PENULIS);
        penerbit = intent.getStringExtra(StringFixed.KEY_PENERBIT);
        thnTerbit = intent.getStringExtra(StringFixed.KEY_TAHUN_TERBIT);
        category = intent.getStringExtra(StringFixed.KEY_NAMA_CATEGORY);
        deskripsi = intent.getStringExtra(StringFixed.KEY_DESKRIPSI);
        image = intent.getStringExtra(StringFixed.KEY_IMAGE);
        path = intent.getStringExtra(StringFixed.KEY_PATH);
        typeFile = intent.getStringExtra(StringFixed.KEY_FILE_TYPE);

//        Tambahan
        namaUser = intent.getStringExtra(StringFixed.KEY_NAMA_USER);
        imageUser = intent.getStringExtra(StringFixed.KEY_IMAGE_USER);
        fileType = intent.getStringExtra(StringFixed.KEY_FILE_TYPE);
        idUserDocument = intent.getStringExtra(StringFixed.KEY_ID_USER);
        totalDownload = intent.getStringExtra(StringFixed.KEY_TOTAL_DOWNLOAD);
        totalView = intent.getStringExtra(StringFixed.KEY_TOTAL_VIEW);
        tglUpload = intent.getStringExtra(StringFixed.KEY_TGL_UPLOAD);
        statusAktif = intent.getStringExtra(StringFixed.KEY_STATUS_AKTIF);

        mNama.setText(nama);
        mPenulis.setText(penulis);
        mPenerbit.setText(penerbit);
        mThnTerbit.setText(thnTerbit);
        mDeskripsi.setText(deskripsi);
        mDocument.setText(path);

        Glide.with(this)
                .load(BuildConfig.BASE_URL_IMAGE+image)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(mImage);

    }

    private void sendData(){
        Intent intent = new Intent(UpdateDocumentActivity.this,DocumentDetailActivity.class);

        //tambahan
        intent.putExtra(StringFixed.KEY_ID_USER,idUserDocument);
        intent.putExtra(StringFixed.KEY_NAMA_USER,namaUser);
        intent.putExtra(StringFixed.KEY_TOTAL_VIEW,totalView);
        intent.putExtra(StringFixed.KEY_TOTAL_DOWNLOAD,totalDownload);
        intent.putExtra(StringFixed.KEY_TGL_UPLOAD,tglUpload);
        intent.putExtra(StringFixed.KEY_STATUS_AKTIF,statusAktif);
        intent.putExtra(StringFixed.KEY_IMAGE_USER,imageUser);

        //penting
        intent.putExtra(StringFixed.KEY_ID_DOCUMENT,idDocument);
        intent.putExtra(StringFixed.KEY_NAMA_DOCUMENT,nama);
        intent.putExtra(StringFixed.KEY_FILE_TYPE,fileType);
        intent.putExtra(StringFixed.KEY_NAMA_CATEGORY,namaCategory);
        intent.putExtra(StringFixed.KEY_PATH,path);
        intent.putExtra(StringFixed.KEY_IMAGE,image);
        intent.putExtra(StringFixed.KEY_PENULIS,penulis);
        intent.putExtra(StringFixed.KEY_PENERBIT,penerbit);
        intent.putExtra(StringFixed.KEY_TAHUN_TERBIT,thnTerbit);
        intent.putExtra(StringFixed.KEY_DESKRIPSI,deskripsi);
        startActivity(intent);
        finish();
    }

}
