package com.example.progmobkelompok9;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.progmobkelompok9.util.DrawerMenu;

public class NotLoginFragment extends Fragment {

    Button loginBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_not_login, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.not_login_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Baca In");
        ab.setDisplayHomeAsUpEnabled(true);

        DrawerMenu drawer = new DrawerMenu();
        drawer.createDrawer(getContext(), (AppCompatActivity) getActivity(), toolbar);

        loginBtn = rootView.findViewById(R.id.fragment_not_login_Button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
