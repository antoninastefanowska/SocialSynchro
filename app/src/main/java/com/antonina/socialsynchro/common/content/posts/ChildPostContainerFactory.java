package com.antonina.socialsynchro.common.content.posts;

import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.database.IDatabaseEntityFactory;
import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.common.database.tables.ChildPostContainerTable;
import com.antonina.socialsynchro.services.twitter.content.TwitterAccount;
import com.antonina.socialsynchro.services.twitter.content.TwitterPostContainer;

public class ChildPostContainerFactory implements IDatabaseEntityFactory {
    private static ChildPostContainerFactory instance;

    public static ChildPostContainerFactory getInstance() {
        if (instance == null)
            instance = new ChildPostContainerFactory();
        return instance;
    }

    @Override
    public ChildPostContainer createFromData(IDatabaseTable data) {
        ChildPostContainerTable childPostContainerData = (ChildPostContainerTable)data;
        ServiceID serviceID = ServiceID.values()[childPostContainerData.serviceID];

        switch(serviceID) {
            case Twitter:
                return new TwitterPostContainer(data);
            default:
                return null;
        }
    }

    public ChildPostContainer createNew(Account account) {
        ServiceID serviceID = account.getService().getID();

        switch(serviceID) {
            case Twitter:
                TwitterAccount twitterAccount = (TwitterAccount)account;
                return new TwitterPostContainer(twitterAccount);
            default:
                return null;
        }
    }
}
