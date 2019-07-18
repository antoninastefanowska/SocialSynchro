package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;
import com.antonina.socialsynchro.database.tables.ITable;

import java.util.Date;
import java.util.List;

public abstract class ChildPostContainer implements IPostContainer, IPost, IDatabaseEntity {
    private long id;
    private String serviceExternalIdentifier;
    private Post post;
    private boolean locked;
    private Account account;
    private Date synchronizationDate;

    protected ParentPostContainer parent;

    // TODO: dla każdej funkcji modyfikującej sprawdzić ograniczenia

    public ChildPostContainer(ITable data) {
        createFromData(data);
    }

    public ChildPostContainer(Account account) {
        this.account = account;
    }

    public ChildPostContainer(ParentPostContainer parent) {
        this.parent = parent;
        parent.addChild(this);
        lock();
    }

    @Override
    public long getID() { return id; }

    @Override
    public String getTitle() {
        if (locked)
            return parent.getPost().getTitle();
        else
            return post.getTitle();
    }

    @Override
    public void setTitle(String title) {
        if (!locked)
            post.setTitle(title);
    }

    @Override
    public String getContent() {
        if (locked)
            return parent.getContent();
        else
            return post.getContent();
    }

    @Override
    public void setContent(String description) {
        if (!locked)
            post.setContent(description);
    }

    @Override
    public List<Attachment> getAttachments() {
        if (locked)
            return parent.getPost().getAttachments();
        else
            return post.getAttachments();
    }

    @Override
    public void addAttachment(Attachment attachment) {
        if (!locked)
            post.addAttachment(attachment);
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (!locked)
            post.removeAttachment(attachment);
    }

    @Override
    public Post getPost() {
        if (locked)
            return parent.getPost();
        else
            return post;
    }

    @Override
    public abstract void publish();

    @Override
    public abstract void remove();

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
        post = null;
    }


    public void unlock() {
        IPost parentPost = parent.getPost();
        locked = false;
        post = new Post();
        setTitle(parentPost.getTitle());
        setContent(parentPost.getContent());
        for (Attachment attachment : parentPost.getAttachments()) {
            addAttachment(attachment);
        }
        // kopia głęboka postu z parenta
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) { this.account = account; }

    public String getServiceExternalIdentifier() { return serviceExternalIdentifier; }

    public void setServiceExternalIdentifier(String serviceExternalIdentifier) { this.serviceExternalIdentifier = serviceExternalIdentifier; }

    public Date getSynchronizationDate() { return synchronizationDate; }

    public void setSynchronizationDate(Date date) { this.synchronizationDate = synchronizationDate; }

    public ParentPostContainer getParent() { return parent; }

    public void setParent(ParentPostContainer parent) { this.parent = parent; }

    @Override
    public void createFromData(ITable data) {
        ChildPostContainerTable childPostContainerData = (ChildPostContainerTable)data;
        this.id = childPostContainerData.id;
        this.serviceExternalIdentifier = childPostContainerData.serviceExternalIdentifier;
        this.locked = childPostContainerData.locked;
        this.synchronizationDate = childPostContainerData.synchronizationDate;

        //TODO: Wydobyć z bazy post i account.
    }
}