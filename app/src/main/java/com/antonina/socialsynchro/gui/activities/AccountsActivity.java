package com.antonina.socialsynchro.gui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.databinding.ActivityAccountsBinding;
import com.antonina.socialsynchro.gui.adapters.AccountDisplayAdapter;
import com.antonina.socialsynchro.base.Account;

import java.util.List;

public class AccountsActivity extends AppCompatActivity {
    private final static int ADD = 0;

    private ActivityAccountsBinding binding;
    private RecyclerView recyclerView;
    private AccountDisplayAdapter adapter;
    private boolean accountsDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        accountsDeleted = false;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_accounts);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_accounts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountDisplayAdapter(this);

        binding.setAccountAdapter(adapter);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("accountsDeleted", accountsDeleted);
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void buttonAddAccount_onClick(View view) {
        Intent connectActivity = new Intent(AccountsActivity.this, ConnectActivity.class);
        startActivityForResult(connectActivity, ADD);
    }

    public void buttonRemoveAccount_onClick(View view) {
        for (Account account : adapter.getSelectedItems())
            account.deleteFromDatabase();
        adapter.removeSelected();
        accountsDeleted = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD:
                    Account account = (Account)data.getSerializableExtra("account");
                    adapter.addItem(account);
                    account.saveInDatabase();
                    break;
            }
        }
    }
}
