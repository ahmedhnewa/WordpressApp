package com.ahmedriyadh.wordpressapp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedriyadh.wordpressapp.R;
import com.ahmedriyadh.wordpressapp.models.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * By Ahmed Riyadh
 * Note : Please Don't Re-Post This Project Source Code on Social Media etc..
 * */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private static final String TAG = "PostAdapter";
    private OnItemClickListener onItemClickListener;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        postList = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post item = postList.get(position);

        String title = item.getTitle().getRendered();
        String description = item.getContent().getRendered();

        if (title != null && !title.isEmpty()) {
            holder.titleTv.setText(title);
        } else {
            holder.titleTv.setVisibility(View.GONE);
        }

        if (description != null && !description.isEmpty()) {
            holder.postDescriptionTv.setText(Html.fromHtml(description));
        } else {
            holder.postDescriptionTv.setVisibility(View.GONE);
        }

        String imageUrl = null;

        if (item.getEmbedded() != null && item.getEmbedded().getFeaturedMedia() != null && item.getEmbedded().getFeaturedMedia().get(0).getSourceUrl() != null) {
            imageUrl = item.getEmbedded().getFeaturedMedia().get(0).getSourceUrl();
        }


        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .into(holder.postIv);
        } else {
            holder.postIv.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv, postDescriptionTv;
        private ImageView postIv;

        public ViewHolder(View itemView,OnItemClickListener listener) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            postIv = itemView.findViewById(R.id.postIv);
            postDescriptionTv = itemView.findViewById(R.id.postDescriptionTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemCLick(v,position);
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemCLick(View view, int position);
    }
}
