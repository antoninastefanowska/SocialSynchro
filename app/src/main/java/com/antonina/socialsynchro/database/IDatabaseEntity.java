package com.antonina.socialsynchro.database;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;

public interface IDatabaseEntity {
    void createFromData(IDatabaseTable data);
    long getInternalID();
    void saveInDatabase();
    void updateInDatabase();
    void deleteFromDatabase();
}
