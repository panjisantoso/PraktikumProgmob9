package com.example.progmobkelompok9.offlineSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sqlite_bacain";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_DOCUMENT_OFFLINE = "document";

    public static final String COLUMN_ID = "id_document";
    public static final String COLUMN_JUDUL = "judul";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PENULIS = "penulis";
    public static final String COLUMN_PENERBIT = "penerbit";
    public static final String COLUMN_TAHUN_TERBIT = "tahun_terbit";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_NAMA_USER = "nama_user";
    public static final String COLUMN_ID_USER = "id_user";

    private Context context;
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
//        try {
//            db.execSQL(
//                    "CREATE TABLE IF NOT EXISTS " + TABLE_DOCUMENT_OFFLINE + " (" +
//                            COLUMN_ID +" integer primary key," +
//                            COLUMN_JUDUL +" text NOT NULL," +
//                            COLUMN_CATEGORY +" text NOT NULL," +
//                            COLUMN_PENULIS +" text NOT NULL," +
//                            COLUMN_PENERBIT +" text NOT NULL,"+
//                            COLUMN_TAHUN_TERBIT +" text NOT NULL,"+
//                            COLUMN_FOTO +" text NOT NULL,"+
//                            COLUMN_NAMA_USER +" text NOT NULL,"+
//                            COLUMN_ID_USER +" text NOT NULL"+
//                            ");"
//            );
//
//            Log.i("INFO", "Tabel "+TABLE_DOCUMENT_OFFLINE+" was Created");
//            Toast.makeText(context,"Tabel "+TABLE_DOCUMENT_OFFLINE+" was Created",Toast.LENGTH_LONG);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        String sql = "CREATE TABLE document (" +
                "id_document INT PRIMARY KEY," +
                "judul TEXT," +
                "category TEXT," +
                "penulis TEXT," +
                "penerbit TEXT," +
                "tahun_terbit TEXT," +
                "foto TEXT," +
                "nama_user TEXT," +
                "file_type TEXT,"+
                "deskripsi TEXT," +
                "path TEXT," +
                "tgl_upload TEXT," +
                "id_user TEXT," +
                "status_aktif INT" +
                ");";
        db.execSQL(sql);


    }

    @Override
    public  void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        if (oldVersion < newVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS document");
            onCreate(sqLiteDatabase);
        }
    }
}
