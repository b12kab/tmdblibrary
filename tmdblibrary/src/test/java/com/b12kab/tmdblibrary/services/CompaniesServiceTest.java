package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.BaseResultsPage;
import com.b12kab.tmdblibrary.entities.Company;
import com.b12kab.tmdblibrary.entities.CompanyResultsPage;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import retrofit2.Call;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompaniesServiceTest extends BaseTestCase {

    @Test
    public void test_company_summary() throws IOException {
        Call<Company> call = this.getManager().companyService().summary(
                TestData.COMPANY_ID
        );

        Company company = call.execute().body();

        assertCompanyDataIntegrity(company);
    }

    @Test
    public void test_company_summary_append_movies() throws IOException {
        Call<Company> call = this.getManager().companyService().summary(
                TestData.COMPANY_ID,
                new AppendToResponse(
                        AppendToResponseItem.MOVIES
                )
        );

        Company company = call.execute().body();

        assertCompany(company);

        assertMovieResultsPage(company.movies);
    }

    @Test
    public void test_company_movies() throws IOException {
        Call<MovieResultsPage> call = this.getManager().companyService().movies(
                TestData.COMPANY_ID
        );

        MovieResultsPage movieResultsPage = call.execute().body();

        assertMovieResultsPage(movieResultsPage);
    }

    public static void assertCompanyResultsPage(CompanyResultsPage companyResultsPage) {
        assertBaseResultsPage(companyResultsPage);

        for (Company company : companyResultsPage.results) {
            assertBaseCompany(company, false);
        }
    }

    public static void assertBaseCompany(Company company, Boolean isLogoPresent) {
        assertNotNull(company);
        assertTrue(company.id > 0);
        assertNotNull(company.name);

        if (!isLogoPresent)
            return;

        assertNotNull(company.logo_path);
        assertTrue(!StringUtils.isBlank(company.logo_path));
    }

    public static void assertCompany(Company company) {
        assertBaseCompany(company, true);

        assertNotNull(company.headquarters);
        assertNotNull(company.homepage);
        assertNotNull(company.origin_country);
        if (company.parent_company != null) {
            assertBaseCompany(company, true);
        }
    }

    public static void assertCompanyDataIntegrity(Company company) {
        assertCompany(company);
        assertEquals(company.id, TestData.COMPANY_ID);
        assertNotNull(company.name);
        assertEquals(company.name, TestData.COMPANY_TITLE);
        assertNotNull(company.origin_country);
        assertEquals(company.origin_country, "US");
        assertNotNull(StringUtils.isBlank(company.description));
    }

    public static void assertBaseResultsPage(BaseResultsPage results) {
        assertNotNull(results);
        assertNotNull(results.page);
        assertNotNull(results.total_pages);
        assertNotNull(results.total_results);
    }

    public static void assertMovieResultsPage(MovieResultsPage movieResultsPage) {
        assertBaseResultsPage(movieResultsPage);
        for (MovieAbbreviated baseMovie : movieResultsPage.results) {
            assertBaseMovie(baseMovie);
        }
    }

    public static void assertBaseMovie(MovieAbbreviated movie) {
        assertNotNull(movie);
        assertNotNull(movie.getId());
        assertNotNull(movie.getTitle());
        assertNotNull(movie.getOriginal_language());
        assertNotNull(movie.getOverview());
        assertNotNull(movie.isAdult());
        assertNotNull(movie.getVote_average());
        assertTrue(movie.getVote_average()>= 0);
        assertNotNull(movie.getVote_count());
        assertTrue(movie.getVote_count() >= 0);

        assertNotNull(movie.getGenre_ids().get(0));
    }

}
