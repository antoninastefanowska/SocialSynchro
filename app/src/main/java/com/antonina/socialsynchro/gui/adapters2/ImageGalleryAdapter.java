package com.antonina.socialsynchro.gui.adapters2;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.attachments.ImageAttachment;
import com.antonina.socialsynchro.databinding.ImageGalleryItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ImageGalleryAdapter extends BaseAdapter<ImageAttachment, ImageGalleryAdapter.ImageViewHolder> {

    public static class ImageViewHolder extends BaseAdapter.BaseViewHolder<ImageGalleryItemBinding> {
        public ImageView imageView;

        public ImageViewHolder(@NonNull View view) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.imageview_image);
        }

        @Override
        protected ImageGalleryItemBinding getBinding(View view) {
            return ImageGalleryItemBinding.bind(view);
        }
    }

    public ImageGalleryAdapter(AppCompatActivity context) {
        super(context);
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.image_gallery_item;
    }

    @Override
    protected void setItemBinding(ImageViewHolder viewHolder, ImageAttachment item) {
        viewHolder.binding.setImage(item);
        RequestOptions options = new RequestOptions().override(50, 50).fitCenter();
        Glide.with(context)
                .load(item.getFile())
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.imageView);
    }

    @Override
    protected ImageViewHolder createViewHolder(View view) {
        return new ImageViewHolder(view);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        ImageViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        setSelectable(viewHolder);
        return viewHolder;
    }

    @Override
    public void loadData() {
        Runnable loadImages = new Runnable() {
            @Override
            public void run() {
                loadImages();
            }
        };
        Thread thread = new Thread(loadImages);
        thread.run();
    }

    private void loadImages() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        String sortOrder = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";
        Cursor cursor = null;

        String[] directories = null;
        if (uri != null)
            cursor = context.managedQuery(uri, projection, null, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            SortedSet<String> sortedDirectories = new TreeSet<String>();
            do {
                String directory = cursor.getString(0);
                directory = directory.substring(0, directory.lastIndexOf("/"));
                sortedDirectories.add(directory);
            } while (cursor.moveToNext());
            directories = new String[sortedDirectories.size()];
            sortedDirectories.toArray(directories);
        }

        SortedSet<ImageAttachment> sortedImages = new TreeSet<ImageAttachment>(new Comparator<ImageAttachment>() {
            @Override
            public int compare(ImageAttachment o1, ImageAttachment o2) {
                if (o1.getFile().lastModified() > o2.getFile().lastModified())
                    return -1;
                else if (o1.getFile().lastModified() < o2.getFile().lastModified())
                    return 1;
                else
                    return 0;
            }
        });
        for (String directoryPath : directories) {
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            if (files == null)
                continue;
            for (File file : files) {
                if (file.isDirectory())
                    files = file.listFiles();
                if (isImage(file)) {
                    ImageAttachment image = new ImageAttachment(file);
                    sortedImages.add(image);
                }
            }
        }
        items = new ArrayList<ImageAttachment>(sortedImages);
        notifyDataSetChanged();
    }

    private static boolean isImage(File file) {
        final String[] extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        String filename = file.getName().toLowerCase();
        for (String extension : extensions) {
            if (filename.contains(extension))
                return true;
        }
        return false;
    }
}