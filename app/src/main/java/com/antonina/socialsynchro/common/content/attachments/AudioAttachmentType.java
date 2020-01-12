package com.antonina.socialsynchro.common.content.attachments;

import android.support.v7.app.AppCompatActivity;

public class AudioAttachmentType extends AttachmentType {
    private static AudioAttachmentType instance;

    public static AudioAttachmentType getInstance() {
        if (instance == null)
            instance = new AudioAttachmentType();
        return instance;
    }

    private AudioAttachmentType() { }

    @Override
    public AttachmentTypeID getID() {
        return null;
    }

    @Override
    public String getName() {
        return "Audio";
    }

    @Override
    public int getIconID() {
        return null;
    }

    @Override
    public Class<? extends AppCompatActivity> getGalleryActivityClass() {
        return null;
    }
}
