package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;

public class VideoAttachment extends Attachment {
    private int height, width, lengthSeconds;

    public VideoAttachment(IDatabaseTable data) {
        super(data);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
        // TODO: Wydobyć pozostałe informacje z pliku - dotyczy też pozostałych załączników
    }

    public VideoAttachment() {
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
    }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public int getLengthSeconds() {
        return lengthSeconds;
    }
}
