package com.antonina.socialsynchro.services.deviantart.content;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.common.content.accounts.LoginFlow;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;

public class DeviantArtService extends Service {
    private static DeviantArtService instance;

    private DeviantArtService() { }

    public static DeviantArtService getInstance() {
        if (instance == null)
            instance = new DeviantArtService();
        return instance;
    }

    @Override
    public ServiceID getID() {
        return ServiceID.DeviantArt;
    }

    @Override
    public String getName() {
        return "DeviantArt";
    }

    @Override
    public int getIconID() {
        return R.drawable.deviantart_icon;
    }

    @Override
    public Drawable getBanner() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.deviantart_banner);
    }

    @Override
    public int getColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorDeviantArt);
    }

    @Override
    public int getFontColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorFontDeviantArt);
    }

    @Override
    public Drawable getPanelBackground() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.background_deviantart_panel);
    }

    @Override
    public int getPanelBackgroundID() {
        return R.drawable.background_deviantart_panel;
    }

    @Override
    public Drawable getBackground() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.background_deviantart);
    }

    @Override
    public LoginFlow createLoginFlow(LoginActivity context) {
        return new DeviantArtLoginFlow(context);
    }
}