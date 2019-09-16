package com.antonina.socialsynchro.gui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ChildPostContainerFactory;
import com.antonina.socialsynchro.content.IPost;
import com.antonina.socialsynchro.content.OnPublishedListener;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.content.attachments.ImageAttachment;
import com.antonina.socialsynchro.databinding.ActivityEditBinding;
import com.antonina.socialsynchro.gui.adapters.AttachmentAdapter;
import com.antonina.socialsynchro.gui.adapters.ChildEditAdapter;
import com.antonina.socialsynchro.gui.dialogs.ChooseAccountDialog;
import com.antonina.socialsynchro.gui.dialogs.ChooseAccountDialogListener;
import com.antonina.socialsynchro.gui.dialogs.ChooseAttachmentTypeDialog;
import com.antonina.socialsynchro.gui.dialogs.ChooseAttachmentTypeDialogListener;
import com.antonina.socialsynchro.gui.helpers.SerializableList;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private final static int ADD_IMAGES = 0;
    private final static int REQUEST_STORAGE_ACCESS = 0;

    private ParentPostContainer parent;
    private List<Account> selectedAccounts;
    private ChildEditAdapter childAdapter;
    private AttachmentAdapter parentAttachmentAdapter;
    private IPost activePostContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("activePostContainer"))
                activePostContainer = (IPost)savedInstanceState.getSerializable("activePostContainer");
        }

        if (getIntent().hasExtra("parent"))
            parent = (ParentPostContainer)getIntent().getSerializableExtra("parent");
        else
            parent = new ParentPostContainer();

        ActivityEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        binding.setParent(parent);

        RecyclerView childRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_children);
        childRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        childAdapter = new ChildEditAdapter(parent, this);

        RecyclerView parentAttachmentRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_parent_attachments);
        parentAttachmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAttachmentAdapter = new AttachmentAdapter(parent);

        binding.setChildAdapter(childAdapter);
        binding.setAttachmentAdapter(parentAttachmentAdapter);
        binding.executePendingBindings();

        EditText parentContent = (EditText)findViewById(R.id.edittext_parent_content);
        parentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                childAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("activePostContainer", activePostContainer);
        super.onSaveInstanceState(outState);
    }

    public void buttonAddChild_onClick(View view) {
        ChooseAccountDialog dialog = new ChooseAccountDialog(this, new ChooseAccountDialogListener() {
            @Override
            public void onAccountsSelected(List<Account> accounts) {
                selectedAccounts = accounts;
                for (Account account : selectedAccounts) {
                    ChildPostContainerFactory factory = ChildPostContainerFactory.getInstance();
                    ChildPostContainer child = factory.createNew(account);
                    childAdapter.addItem(child);
                }
                selectedAccounts = null;
            }
        });

        List<Account> usedAccounts = new ArrayList<Account>();
        for (ChildPostContainer child : parent.getChildren())
            usedAccounts.add(child.getAccount());

        dialog.setIgnoredData(usedAccounts);
        dialog.show();
    }

    public void buttonSave_onClick(View view) {
        exitActivity();
    }

    public void buttonPublish_onClick(View view) {
        final Activity context = this;
        parent.publish(new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost, String error) {
                int position = childAdapter.getItemPosition(publishedPost);
                childAdapter.notifyItemChanged(position);
                Toast toast = Toast.makeText(context, "Successfily published: " + publishedPost.getExternalID(), Toast.LENGTH_SHORT);
                toast.show();
                exitActivity();
            }
        });
        childAdapter.notifyDataSetChanged();
    }

    private void exitActivity() {
        Intent mainActivity = new Intent();
        mainActivity.putExtra("parent", parent);
        setResult(RESULT_OK, mainActivity);
        finish();
    }

    public void buttonParentAddAttachment_onClick(View view) {
        activePostContainer = parent;
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
        ChooseAttachmentTypeDialog dialog = new ChooseAttachmentTypeDialog(this, new ChooseAttachmentTypeDialogListener() {
            @Override
            public void onAttachmentTypeSelected(AttachmentType attachmentType) {
                switch (attachmentType.getID()) {
                    case Image:
                        Intent addImages = new Intent(EditActivity.this, ImageGalleryActivity.class);
                        startActivityForResult(addImages, ADD_IMAGES);
                        break;
                    case Audio:
                        break;
                    case Video:
                        break;
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_IMAGES:
                    SerializableList<ImageAttachment> serializableImages = (SerializableList<ImageAttachment>)data.getSerializableExtra("images");
                    List<ImageAttachment> images = serializableImages.getList();
                    for (ImageAttachment image : images) {
                        if (activePostContainer == parent)
                            parentAttachmentAdapter.addItem(image);
                    }
                    if (activePostContainer == parent)
                        childAdapter.notifyDataSetChanged();
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
                    Toast toast = Toast.makeText(this, "Can't access storage - permission denied.", Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }
}