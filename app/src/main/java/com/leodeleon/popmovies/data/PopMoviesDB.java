package com.leodeleon.popmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.leodeleon.popmovies.model.MovieDetail;

@Database(entities = {MovieDetail.class}, version = 1)
public abstract class PopMoviesDB extends RoomDatabase {
  public abstract MovieDao movieDao();
}