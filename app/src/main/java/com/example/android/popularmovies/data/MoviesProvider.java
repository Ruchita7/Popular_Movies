package com.example.android.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Content provider for favourite movies
 */

@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MoviesDatabase.class,
        packageName = "com.example.android.popularmovies.provider")


public final class MoviesProvider {

    private MoviesProvider() {

    }

    public static final String AUTHORITY = "com.example.android.popularmovies.MoviesProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FAVOURITE_MOVIE = "favourite_movie";
    }

    /**
     *
     * @param paths
     * @return
     */
    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();

        for (String path : paths) {
            builder.appendPath(path);
        }

        return builder.build();
    }


    @TableEndpoint(table = MoviesDatabase.FAVOURITEMOVIE)
    public static class Movies {

        @ContentUri(
                path = Path.FAVOURITE_MOVIE,
                type = "vnd.android.cursor.dir/favMovie",
                defaultSort = FavouriteMovie.MOVIE_ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FAVOURITE_MOVIE);


    }
}

