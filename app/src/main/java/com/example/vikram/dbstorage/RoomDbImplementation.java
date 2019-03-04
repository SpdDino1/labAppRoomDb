package com.example.vikram.dbstorage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RoomEntity.class},version = 1,exportSchema = false)
public abstract class RoomDbImplementation extends RoomDatabase {
    public abstract RoomDao daoAccess();
}
