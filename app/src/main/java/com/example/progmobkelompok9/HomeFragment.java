package com.example.progmobkelompok9;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progmobkelompok9.adapter.HomeAdapter;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Auth;
import com.example.progmobkelompok9.model.Document;
import com.example.progmobkelompok9.util.DrawerMenu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    TextView emptyTxt;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    List<Document> documentList = new ArrayList<Document>();
    View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.home_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Baca In");
        ab.setDisplayHomeAsUpEnabled(true);

        DrawerMenu drawer = new DrawerMenu();
        drawer.createDrawer(getContext(), (AppCompatActivity) getActivity(), toolbar);
        getDocument();

        return rootView;
    }

    private void setRecycler(){

        recyclerView = rootView.findViewById(R.id.home_recyclerView);
        emptyTxt = rootView.findViewById(R.id.home_empty);
        Log.e("sdf",documentList.toString());
        if (documentList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyTxt.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyTxt.setVisibility(View.GONE);
            adapter = new HomeAdapter(getContext(), documentList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDocument();
    }

    private void getDocument(){
        ApiClient.getClient()
                .create(ApiService.class)
                .getDocument()
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

}
