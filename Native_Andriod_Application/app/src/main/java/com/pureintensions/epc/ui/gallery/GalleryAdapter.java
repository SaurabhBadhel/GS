package com.pureintensions.epc.ui.gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pureintensions.epc.R;
import com.pureintensions.epc.data.model.GalleryFolder;
import com.pureintensions.epc.ui.GlideApp;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {

    private Context context;
    private List<GalleryFolder> folders;

    public GalleryAdapter(Context context, List<GalleryFolder> folders) {
        this.context = context;
        this.folders = folders;
    }

    public class GalleryHolder extends RecyclerView.ViewHolder {

        public RecyclerView recyclerView;
        public TextView date;

        public GalleryHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.galleryGrid);
            date = view.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_fragment, parent, false);

        return new GalleryAdapter.GalleryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryHolder holder, int position) {

        final GalleryFolder  galleryFolder = folders.get(position);

        /* Set Date */
        holder.date.setText(galleryFolder.getDate());

        /* Build view for images */
        LinearLayoutManager layoutManager= new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(10);
        holder.recyclerView.setLayoutManager(layoutManager);

        GalleryImageAdapter imageAdapter = new GalleryImageAdapter(context, galleryFolder);
        holder.recyclerView.setAdapter(imageAdapter);

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
}
