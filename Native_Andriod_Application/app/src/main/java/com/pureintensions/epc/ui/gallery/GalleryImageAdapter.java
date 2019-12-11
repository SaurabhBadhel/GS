package com.pureintensions.epc.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.GalleryFolder;
import com.pureintensions.epc.data.model.repository.LoginRepository;
import com.pureintensions.epc.task.CreateTaskActivity;
import com.pureintensions.epc.ui.GlideApp;

import java.util.ArrayList;
import java.util.List;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.GalleryImageHolder> {

    private Context context;
    private GalleryFolder folder;

    public GalleryImageAdapter(Context context, GalleryFolder folder) {
        this.context = context;
        this.folder = folder;
    }

    public class GalleryImageHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ProgressBar progressBar;
        public Uri downloadUrl;

        public GalleryImageHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.galleryImage);
            progressBar = view.findViewById(R.id.galleryImageLoading);
        }
    }


    @NonNull
    @Override
    public GalleryImageAdapter.GalleryImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_image, parent, false);
        return new GalleryImageAdapter.GalleryImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryImageAdapter.GalleryImageHolder holder, int position) {

        String fileName = folder.getFileName().get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(LoginRepository.getInstance().getSelectedProject().getProjectId())
                .child(folder.getFolderName()).child(fileName);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(final Uri downloadUrl)
            {
                GlideApp.with(context).load(downloadUrl).diskCacheStrategy(DiskCacheStrategy.DATA).apply(RequestOptions.fitCenterTransform()).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.imageView.setImageDrawable(resource);
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

                holder.downloadUrl = downloadUrl;

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), GalleryImageViewActivity.class);
                        intent.putExtra("uri", downloadUrl);
                        context.startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return folder != null && folder.getFileName() != null ? folder.getFileName().size() : 0;
    }
}
