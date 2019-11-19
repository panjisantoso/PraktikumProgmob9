package com.example.progmobkelompok9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progmobkelompok9.adapter.MyDocumentAdapter;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Document;
import com.example.progmobkelompok9.util.DrawerMenu;
import com.example.progmobkelompok9.util.StringFixed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDocumentFragment extends Fragment {

    View rootView;
    FloatingActionButton mAdd;

    SharedPreferences sharedPreferences;

    RecyclerView recyclerView;
    MyDocumentAdapter adapter;
    List<Document> documentList = new ArrayList<Document>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mydocument, container, false);

        sharedPreferences = getActivity().getSharedPreferences(StringFixed.KEY_LOGIN, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.mydocument_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setTitle("My Document");
        ab.setDisplayHomeAsUpEnabled(true);

        DrawerMenu drawer = new DrawerMenu();
        drawer.createDrawer(getContext(), (AppCompatActivity) getActivity(), toolbar);

        mAdd = rootView.findViewById(R.id.mydocument_floatingActionButtonadd);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddDocumentActivity.class);
                startActivity(intent);
            }
        });
        getDocument();
        return rootView;
    }

    private void setRecycler(){

        recyclerView = rootView.findViewById(R.id.mydocument_recyclerview);
//        emptyTxt = rootView.findViewById(R.id.home_empty);
        Log.e("sdf",documentList.toString());
        if (documentList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
//            emptyTxt.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
//            emptyTxt.setVisibility(View.GONE);
            adapter = new MyDocumentAdapter(getContext(), documentList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            recyclerView.setAdapter(adapter);
        }
    }

    private void getDocument(){

        ApiClient.getClient()
                .create(ApiService.class)
                .getMyDocument(sharedPreferences.getString(StringFixed.KEY_ID_USER,""))
                .enqueue(new Callback<List<Document>>() {
                    @Override
                    public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
//                        Log.e("error bro",response.body().get(0).getDeskripsi().toString());
                        documentList.clear();
                        documentList.addAll(response.body());
                        setRecycler();
                    }

                    @Override
                    public void onFailure(Call<List<Document>> call, Throwable t) {
                        Log.e("error bro",t.toString());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDocument();
    }
}