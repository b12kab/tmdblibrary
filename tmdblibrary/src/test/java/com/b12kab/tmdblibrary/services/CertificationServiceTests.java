package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.entities.Certifications;
import com.b12kab.tmdblibrary.entities.CreateNewTokenResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CertificationServiceTests extends BaseTestCase {

    @Test
    public void test_certification_movie() throws IOException {
        Call<Certifications> call = this.getManager().certificationService().movie();

        Certifications certifications = call.execute().body();

        assertCertifications(certifications);
    }

    @Test
    public void test_certification_tv() throws IOException {
        Call<Certifications> call = this.getManager().certificationService().tv();

        Certifications certifications = call.execute().body();

        assertCertifications(certifications);
    }

    public static void assertCertification(Certifications.Certification certification) {
        assertNotNull(certification);
        assertNotNull(certification.certification);
        assertNotNull(certification.meaning);
        assertNotNull(certification.order);
    }

    public static void assertCertifications(Certifications certifications) {
        for (Map.Entry<String, List<Certifications.Certification>> certificationSet : certifications.certifications.entrySet()) {
            assertNotNull(certificationSet.getKey());
            assertNotNull(certificationSet.getValue());
            assertNotNull(certificationSet.getValue());
            for (Certifications.Certification certification : certificationSet.getValue()) {
                assertCertification(certification);
            }
        }
    }
}
