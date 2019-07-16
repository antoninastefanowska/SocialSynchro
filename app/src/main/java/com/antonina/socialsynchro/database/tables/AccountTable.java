package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.base.IConvertedData;
import com.antonina.socialsynchro.base.ITable;
import com.antonina.socialsynchro.base.Service;

@Entity(tableName = "account", foreignKeys = @ForeignKey(entity = Service.class, parentColumns = "id", childColumns = "service_id"))
public class AccountTable implements ITable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "service_external_identifier")
    public String serviceExternalIdentifier;

    @ColumnInfo(name = "profile_picture_url")
    public String profilePictureUrl;

    @ColumnInfo(name = "access_token")
    public String accessToken;

    @ColumnInfo(name = "secret_token")
    public String secretToken;

    @ColumnInfo(name = "service_id")
    public long serviceId;

    @Override
    public void convertFromData(IConvertedData convertedData) {
        Account account = (Account)convertedData;
        this.name = account.getName();
        this.serviceExternalIdentifier = account.getServiceExternalIdentifier();
        this.profilePictureUrl = account.getServiceExternalIdentifier();
        this.accessToken = account.getAccessToken();
        this.secretToken = account.getSecretToken();
        // TODO: Trzeba wydobyć service ID
    }
}
