package com.example.android.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.BLOB;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Interface for content provider for Favourite Movies Table
 */
public interface FavouriteMovie {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(INTEGER)
    @NotNull
    String MOVIE_ID = "movie_id";

    @DataType(TEXT)
    @NotNull
    String TITLE = "original_title";

    @DataType(TEXT)
    @NotNull
    String MOVIE_DESC = "overview";

    @DataType(TEXT)
    @NotNull
    String MOVIE_RELEASE_DATE = "release_date";

    @DataType(TEXT)
    @NotNull
    String POSTER_PATH = "poster_path";

    @DataType(BLOB)
    @NotNull
    String POSTER_IMAGE = "poster_image";

    @DataType(TEXT)
    @NotNull
    String BACKGROUND_IMAGE = "background_image";

    @DataType(TEXT)
    @NotNull
    String VOTE_AVERAGE = "avg_votes";

    @DataType(INTEGER)
    @NotNull
    String VOTE_COUNT = "vote_count";
}
