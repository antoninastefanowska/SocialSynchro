package com.antonina.socialsynchro.services.twitter.model;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.model.accounts.LoginFlow;
import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.model.posts.PostOptions;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;
import com.antonina.socialsynchro.common.model.services.Service;
import com.antonina.socialsynchro.common.model.services.ServiceID;

public class TwitterService extends Service {
    private static TwitterService instance;

    private TwitterService() { }

    public static TwitterService getInstance() {
        if (instance == null)
            instance = new TwitterService();
        return instance;
    }

    @Override
    public ServiceID getID() {
        return ServiceID.Twitter;
    }

    @Override
    public String getName() {
        return "Twitter";
    }

    @Override
    public int getIconID() {
        return R.drawable.twitter_icon;
    }

    @Override
    public Drawable getBanner() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.twitter_banner);
    }

    @Override
    public int getColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorTwitter);
    }

    @Override
    public int getFontColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorFontTwitter);
    }

    @Override
    public Drawable getPanelBackground() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.background_twitter_panel);
    }

    public int getPanelBackgroundID() {
        return R.drawable.background_twitter_panel;
    }

    @Override
    public Drawable getBackground() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.background_twitter);
    }

    @Override
    public Account createAccount(IDatabaseRow data) {
        return new TwitterAccount(data);
    }

    @Override
    public ChildPostContainer createPostContainer(IDatabaseRow data) {
        return new TwitterPostContainer(data);
    }

    @Override
    public ChildPostContainer createNewPostContainer(Account account) {
        return new TwitterPostContainer((TwitterAccount)account);
    }

    @Override
    public LoginFlow createLoginFlow(LoginActivity context) {
        return new TwitterLoginFlow(context);
    }

    @Override
    public boolean isOptionsEnabled() {
        return true;
    }

    @Override
    public PostOptions createNewPostOptions(ChildPostContainer post) {
        return new TwitterPostOptions((TwitterPostContainer)post);
    }
}
