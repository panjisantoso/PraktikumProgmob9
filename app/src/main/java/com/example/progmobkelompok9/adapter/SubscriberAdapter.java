package com.example.progmobkelompok9.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.BuildConfig;
import com.example.progmobkelompok9.R;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.ResponseMessage;
import com.example.progmobkelompok9.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriberAdapter extends RecyclerView.Adapter<SubscriberAdapter.MyViewHolder>{

    private List<User> userList;
    private Context context;
    private LayoutInflater inflater;
    private User user;

    public SubscriberAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.item_subscriber, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        user = userList.get(position);
        Glide.with(context)
        .load(BuildConfig.BASE_URL_IMAGE+user.getImage())
        .centerCrop()
        .placeholder(R.drawable.image_placeholder)
        .into(holder.mImage);

        holder.mNamaUser.setText(user.getNamaUser());

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?").setMessage("Unsubscribe this person").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(userList.get(position).getIdSubscriber(), position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

    }

    private void delete(String idSubscriber, final int position){
        ApiClient.getClient()
                .create(ApiService.class)
                .deleteSubscribe(idSubscriber)
                .enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        userList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, userList.size());
                        Toast.makeText(context,"You no longer subscribe to that person",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return getUserList().size();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mNamaUser;
        private ImageView mImage,mDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mNamaUser = itemView.findViewById(R.id.item_subscriber_nama);
            mImage = itemView.findViewById(R.id.item_subscriber_image);
            mDelete = itemView.findViewById(R.id.item_subscriber_delete);

        }
    }
}
