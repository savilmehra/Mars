package com.in.mars.ui;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAOMars {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNew(Url url);

    @Query("SELECT * FROM urls ")
   LiveData<List<Url>>  getUrlList();
}
