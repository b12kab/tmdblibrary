package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.entities.Genre;
import com.b12kab.tmdblibrary.entities.GenreResults;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;

import retrofit2.Call;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenresServiceTest extends BaseTestCase {

    @BeforeEach
    void init() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            System.out.println(e);
        }
    }

    @Test
    public void test_movie() throws IOException {
        Call<GenreResults> call = getManager().genresService().movie(
                null
        );

        GenreResults results = call.execute().body();
        assertGenres(results);
    }

    @Test
    public void test_tv() throws IOException {
        Call<GenreResults> call = getManager().genresService().tv(
                null
        );
        GenreResults results = call.execute().body();
        assertGenres(results);
    }

    public static void assertGenres(GenreResults results) {
        assertNotNull(results);

        assertNotNull(results.genres);
        assertTrue(results.genres.size() > 0);

        for (Genre genre : results.genres) {
            assertGenre(genre);
        }
    }

    public static void assertGenre(Genre genre) {
        assertNotNull(genre);
        assertNotNull(genre.name);
        assertTrue(StringUtils.isNotBlank(genre.name));
        assertNotNull(genre.id);
        assertTrue(genre.id > 0);
    }
}
