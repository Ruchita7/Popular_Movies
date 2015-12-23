package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Content Provider database class
 */
@Database(version = MoviesDatabase.VERSION, packageName = "com.example.android.popularmovies.provider")
public final class MoviesDatabase {

    public static final int VERSION = 5;


    private MoviesDatabase() {

    }


    @Table(FavouriteMovie.class)
    public static final String FAVOURITEMOVIE = "favourite_movie";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + FAVOURITEMOVIE;

}
