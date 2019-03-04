package com.example.vikram.dbstorage;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RoomEntity {
    @NonNull
    @PrimaryKey
    public String entityKey;

    @ColumnInfo(name="entityValue")
    public String entityValue;

    public RoomEntity(String entityKey, String entityValue) {
        this.entityKey = entityKey;
        this.entityValue = entityValue;
    }
}
