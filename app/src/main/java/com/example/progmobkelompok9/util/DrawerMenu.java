package com.example.progmobkelompok9.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.BuildConfig;
import com.example.progmobkelompok9.MainActivity;
import com.example.progmobkelompok9.MyHistoryActivity;
import com.example.progmobkelompok9.LoginActivity;
import com.example.progmobkelompok9.MySubscribeActivity;
import com.example.progmobkelompok9.OfflineDocumentActivity;
import com.example.progmobkelompok9.R;
import com.example.progmobkelompok9.RegisterActivity;
import com.example.progmobkelompok9.TentangAplikasiActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

public class DrawerMenu {

    String password, username, nama, sub_title, foto,id,email;
    boolean session;
    SharedPreferences sharedpreferences;

    public DrawerMenu(){

    }

    public void createDrawer(final Context context, final AppCompatActivity activity, final Toolbar mToolbar){

        // Create the AccountHeader
        sharedpreferences = activity.getSharedPreferences(StringFixed.KEY_LOGIN,context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(StringFixed.KEY_SESSION, false);

        if (session){
            password = sharedpreferences.getString(StringFixed.KEY_PASSWORD, null);
            username = sharedpreferences.getString(StringFixed.KEY_USERNAME, null);
            nama = sharedpreferences.getString(StringFixed.KEY_NAME, null);
            sub_title = username;
            foto = sharedpreferences.getString(StringFixed.KEY_IMAGE,null);
        }
        else{
            nama = "Guest";
            sub_title = "Silahkan Login";
        }
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
//                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                Glide.with(imageView.getContext())
                        .load(uri)
                        .centerCrop()
                        .placeholder(placeholder)
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(imageView.getContext())
                        .load(R.drawable.material_background)
                        .centerCrop()
                        .into(imageView);
//                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.material_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(nama).withEmail(sub_title).withIcon(BuildConfig.BASE_URL_IMAGE+foto)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        if (session){
            buildDrawerLoggedIn(context,activity,mToolbar, headerResult);
        }
        else{
            buildDrawerNotLoggedIn(context,activity,mToolbar, headerResult);
        }


    }

    private void buildDrawerNotLoggedIn(Context context, AppCompatActivity activity, Toolbar mToolbar, AccountHeader headerResult){
        final Context contextFinal = context;
        final AppCompatActivity activityFinal = activity;
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Login").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Register").withIcon(GoogleMaterial.Icon.gmd_info);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Offline Document").withIcon(GoogleMaterial.Icon.gmd_info);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Tentang Aplikasi").withIcon(GoogleMaterial.Icon.gmd_info);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        item1,
                        item2,
//                        new DividerDrawerItem(),
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1){
                            Intent intent = new Intent(contextFinal, LoginActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2){
                            Intent intent = new Intent(contextFinal, RegisterActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3){
                            Intent intent = new Intent(contextFinal, OfflineDocumentActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 4){
                            Intent intent = new Intent(contextFinal, TentangAplikasiActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        return false;
                    }
                })
                .build();

        if(mToolbar != null)
            result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void buildDrawerLoggedIn(Context context, AppCompatActivity activity, Toolbar mToolbar, AccountHeader headerResult){
        final Context contextFinal = context;
        final AppCompatActivity activityFinal = activity;
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("My Subscriber").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("My History").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Document Offline").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Tentang Aplikasi").withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("Log Out").withIcon(GoogleMaterial.Icon.gmd_exit_to_app);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        new DividerDrawerItem(),
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1){
                            Intent intent = new Intent(contextFinal, MySubscribeActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2){
                            Intent intent = new Intent(contextFinal, MyHistoryActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3){
                            Intent intent = new Intent(contextFinal, OfflineDocumentActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 4){
                            Intent intent = new Intent(contextFinal, TentangAplikasiActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 5){
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(contextFinal, MainActivity.class);
                            contextFinal.startActivity(intent);
                            activityFinal.finish();
                        }
                        return false;
                    }
                })
                .build();

        if(mToolbar != null)
            result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }


}
