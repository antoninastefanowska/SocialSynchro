package com.antonina.socialsynchro.common.gui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.adapters.AttachmentEditAdapter;
import com.antonina.socialsynchro.common.gui.adapters.PostTabAdapter;
import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.model.attachments.Attachment;
import com.antonina.socialsynchro.common.model.posts.PostContainer;
import com.antonina.socialsynchro.common.model.services.Service;
import com.antonina.socialsynchro.common.gui.dialogs.WarningDialog;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.model.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.model.attachments.AttachmentType;
import com.antonina.socialsynchro.common.gui.listeners.OnUnlockedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.gui.dialogs.ChooseAccountDialog;
import com.antonina.socialsynchro.common.gui.listeners.OnAccountsSelectedListener;
import com.antonina.socialsynchro.common.gui.dialogs.ChooseAttachmentTypeDialog;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentTypeSelectedListener;
import com.antonina.socialsynchro.common.gui.other.SerializableList;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class EditActivity extends AppCompatActivity {
    private final static int ADD_ATTACHMENTS = 0;
    private final static int REQUEST_STORAGE_ACCESS = 0;

    private List<Account> selectedAccounts;

    private PostTabAdapter postAdapter;
    private AttachmentEditAdapter activeAttachmentAdapter;
    private ParentPostContainer parent;

    private OnUnlockedListener unlockedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (getIntent().hasExtra("parent")) {
            parent = (ParentPostContainer) getIntent().getSerializableExtra("parent");
            parent.show();
        }
        else
            parent = new ParentPostContainer();

        TabLayout tabs = findViewById(R.id.tab_post);
        ViewPager viewPager = findViewById(R.id.view_pager);

        postAdapter = new PostTabAdapter(getSupportFragmentManager(), parent, tabs);
        viewPager.setAdapter(postAdapter);
        tabs.setupWithViewPager(viewPager);
        postAdapter.loadIcons();

        final AppCompatActivity context = this;
        unlockedListener = new OnUnlockedListener() {
            @Override
            public void onUnlocked(ChildPostContainer unlocked) {
                WarningDialog warningDialog = new WarningDialog(context, getResources().getString(R.string.warning_unlocked, unlocked.getService().getName(), unlocked.getAccount().getName()));
                warningDialog.show();
            }
        };
    }

    public void addChild() {
        ChooseAccountDialog dialog = new ChooseAccountDialog(this, new OnAccountsSelectedListener() {
            @Override
            public void onAccountsSelected(List<Account> accounts) {
                selectedAccounts = accounts;
                for (Account account : selectedAccounts) {
                    Service service = account.getService();
                    ChildPostContainer child = service.createNewPostContainer(account);
                    child.setUnlockedListener(unlockedListener);
                    postAdapter.addItem(child);
                }
                selectedAccounts = null;
            }
        });

        List<Account> usedAccounts = new ArrayList<>();
        for (ChildPostContainer child : parent.getChildren())
            usedAccounts.add(child.getAccount());

        dialog.setIgnoredData(usedAccounts);
        dialog.show();
    }

    public void removeChild(ChildPostContainer child) {
        postAdapter.removeItem(child);
    }

    public void publishPost(final PostContainer postContainer) {
        final Context context = this;
        final View layout = findViewById(R.id.layout_main);
        postContainer.publish(new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost) {
                Snackbar snackbar = Snackbar.make(layout, "Succesfully published: " + publishedPost.getExternalID(), Snackbar.LENGTH_LONG);
                snackbar.show();
                if (postContainer.isParent() && ((ParentPostContainer)postContainer).finishedPublishing())
                    exitAndSave((ParentPostContainer)postContainer);
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                Toast toast = Toast.makeText(context, "Failed to publish. Error: " + error, Toast.LENGTH_LONG);
                toast.show();
            }
        }, new OnAttachmentUploadedListener() {
            @Override
            public void onInitialized(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment initialized: " + attachment.getFile().getName(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onProgress(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment upload progress: " + attachment.getUploadProgress() + "%", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFinished(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment upload finished: " + attachment.getExternalID(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(Attachment attachment, String error) {
                Toast toast = Toast.makeText(context, "Attachment upload failed: " + attachment.getFile().getName(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void unpublishPost(final PostContainer postContainer) {
        final View layout = findViewById(R.id.layout_main);
        postContainer.unpublish(new OnUnpublishedListener() {
            @Override
            public void onUnpublished(ChildPostContainer unpublishedPost) {
                Snackbar snackbar = Snackbar.make(layout, "Succesfully unpublished.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                Snackbar snackbar = Snackbar.make(layout, "Failed to publish. Error: " + error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    public void exitAndSave(ParentPostContainer parent) {
        Intent mainActivity = new Intent();
        mainActivity.putExtra("parent", parent);
        setResult(RESULT_OK, mainActivity);
        finish();
    }

    public void addAttachment(AttachmentEditAdapter activeAttachmentAdapter) {
        this.activeAttachmentAdapter = activeAttachmentAdapter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, REQUEST_STORAGE_ACCESS);
            }
            else
                chooseAttachmentType();
        }
        else
            chooseAttachmentType();
    }

    private void chooseAttachmentType() {
        ChooseAttachmentTypeDialog dialog = new ChooseAttachmentTypeDialog(this, new OnAttachmentTypeSelectedListener() {
            @Override
            public void onAttachmentTypeSelected(AttachmentType attachmentType) {
                Intent addAttachments = new Intent(EditActivity.this, attachmentType.getGalleryActivityClass());
                startActivityForResult(addAttachments, ADD_ATTACHMENTS);
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_ATTACHMENTS:
                    SerializableList<Attachment> serializableAttachments = (SerializableList<Attachment>)data.getSerializableExtra("attachments");
                    List<Attachment> attachments = serializableAttachments.getList();

                    for (Attachment attachment : attachments)
                        activeAttachmentAdapter.addItem(attachment);

                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    chooseAttachmentType();
                else {
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.error_storage_permissions), Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }
}
