package com.example.progmobkelompok9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.progmobkelompok9.offlineSQLite.DBHelper;
import com.example.progmobkelompok9.util.StringFixed;

public class OfflineDocumentActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Cursor cursor;
    String[] daftar;
    android.widget.ListView ListView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_document);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Offline Document");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN,MODE_PRIVATE);
//        idUser = sharedPreferences.getString(StringFixed.KEY_ID_USER,"");
//        session = sharedPreferences.getBoolean(StringFixed.KEY_SESSION,false);
        List();
    }
    public void List(){
        SQLiteDatabase db = dbHelper.getReadableDatabase(); //memanggil db untuk dilihat isinya
        cursor = db.rawQuery("SELECT * FROM document", null);
        daftar = new String[cursor.getCount()];

        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1);
        }

        ListView = (ListView) findViewById(R.id.listViewUser);
        ListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));//file layout yang mendefinisikan layout untuk setiap data yang ditampilkan, dimana simple_list_item_1 adalah file xml bawaan dari Android.
        ListView.setSelected(true);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) { //AdapterView: The AdapterView where the click happened. View: The view within the AdapterView that was clicked (this will be a view provided by the adapter), int: The position of the view in the adapter., long: The row id of the item that was clicked.
                final String selection = daftar[arg2];
                final CharSequence[] dialogitem = {"Lihat","Hapus"};
                AlertDialog.Builder builder = new AlertDialog.Builder(OfflineDocumentActivity.this);//pop up
                builder.setTitle("Opsi");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                Intent intent1 = new Intent(getApplicationContext(), OfflineDocumentDetailActivity.class);
                                intent1.putExtra("judul", selection);
                                startActivity(intent1);
                                break;
                            case 1:
                                SQLiteDatabase db = dbHelper.getWritableDatabase();//memanggil database agar bisa dimanipulasi
                                db.execSQL("DELETE FROM document WHERE judul = '"+selection+"'");
                                List();
                                break;
                        }
                    }

                });
                builder.create().show();//liatin pop up atau dialog
            }
        });
        ((ArrayAdapter)ListView.getAdapter()).notifyDataSetInvalidated();//ngasi tau kalo data source nya sudah tidak berlaku atau ada perubahan
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
