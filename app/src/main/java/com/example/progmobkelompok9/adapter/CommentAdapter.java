package com.example.progmobkelompok9.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.BuildConfig;
import com.example.progmobkelompok9.R;
import com.example.progmobkelompok9.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{

    private List<Comment> commentList;
    private Context context;
    private LayoutInflater inflater;
    Comment comment;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        comment = commentList.get(position);
        Glide.with(context)
                .load(BuildConfig.BASE_URL_IMAGE+comment.getImageUser())
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.mImage);

        holder.mNamaUser.setText(comment.getNamaUser());
        holder.mComment.setText(comment.getComment());

    }

    @Override
    public int getItemCount() {
        return getCommentList().size();
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mNamaUser, mComment;
        private ImageView mImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mNamaUser = itemView.findViewById(R.id.item_comment_namaUser);
            mComment = itemView.findViewById(R.id.item_comment_comment);
            mImage = itemView.findViewById(R.id.item_comment_imageUser);

        }
    }
}
