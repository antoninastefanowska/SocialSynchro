package com.antonina.socialsynchro.content.attachments;

public class VideoAttachment implements IAttachment {
    private Object video;

    @Override
    public int getSizeKb() {
        return 0;
    }

    public int getLengthSeconds() {
        return 0;
    }

    @Override
    public AttachmentID getType() {
        return null;
    }
}
