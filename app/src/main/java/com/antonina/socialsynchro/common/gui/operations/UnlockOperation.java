package com.antonina.socialsynchro.common.gui.operations;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;

public class UnlockOperation extends Operation {
    @Override
    public OperationID getID() {
        return OperationID.UNLOCK;
    }

    @Override
    public String getName() {
        return SocialSynchro.getInstance().getResources().getString(R.string.unlock);
    }

    @Override
    public Drawable getIcon() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.icon_lock_open);
    }

    @Override
    public int getIconID() {
        return R.drawable.icon_lock_open;
    }
}
