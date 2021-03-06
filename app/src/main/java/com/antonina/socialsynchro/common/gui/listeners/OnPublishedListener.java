package com.antonina.socialsynchro.common.gui.listeners;

import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;

public interface OnPublishedListener {
    void onPublished(ChildPostContainer publishedPost);
    void onError(ChildPostContainer post, String error);
}
