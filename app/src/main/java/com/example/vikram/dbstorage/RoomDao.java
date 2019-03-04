package com.example.vikram.dbstorage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface RoomDao {
    @Insert
    void insertEntity(RoomEntity entity);

    @Query("Select * from RoomEntity Where entityKey= :entityKey")
    RoomEntity fetchEntity(String entityKey);

    @Query("Delete from RoomEntity Where entityKey= :entityKey")
    void deleteEntity(String entityKey);

    @Query("Update RoomEntity set entityValue = :entityValue Where entityKey= :entityKey")
    void updateEntity(String entityKey,String entityValue);
}
