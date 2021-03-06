package com.antonina.socialsynchro.common.gui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.attachments.Attachment;
import com.antonina.socialsynchro.common.model.attachments.ImageAttachment;
import com.antonina.socialsynchro.databinding.ActivityImageGalleryBinding;
import com.antonina.socialsynchro.common.gui.adapters.ImageGalleryAdapter;
import com.antonina.socialsynchro.common.gui.other.SerializableList;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CollectionAddAllCanBeReplacedWithConstructor")
public class ImageGalleryActivity extends AppCompatActivity {
    private ImageGalleryAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        ActivityImageGalleryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_image_gallery);
        imageAdapter = new ImageGalleryAdapter(this);
        RecyclerView imageRecyclerView = findViewById(R.id.recyclerview_images);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        binding.setImageAdapter(imageAdapter);
        binding.executePendingBindings();
    }

    public void confirm(View view) {
        List<ImageAttachment> images = imageAdapter.getSelectedItems();
        List<Attachment> attachments = new ArrayList<>();
        attachments.addAll(images);

        SerializableList<Attachment> serializableAttachments = new SerializableList<>(attachments);
        Intent editActivity = new Intent();
        editActivity.putExtra("attachments", serializableAttachments);
        setResult(RESULT_OK, editActivity);
        finish();
    }
}
