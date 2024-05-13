package backend.domain;

import static backend.domain.CartTestSamples.*;
import static backend.domain.PhotoTestSamples.*;
import static backend.domain.PortfolioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Photo.class);
        Photo photo1 = getPhotoSample1();
        Photo photo2 = new Photo();
        assertThat(photo1).isNotEqualTo(photo2);

        photo2.setId(photo1.getId());
        assertThat(photo1).isEqualTo(photo2);

        photo2 = getPhotoSample2();
        assertThat(photo1).isNotEqualTo(photo2);
    }

    @Test
    void portfolioTest() throws Exception {
        Photo photo = getPhotoRandomSampleGenerator();
        Portfolio portfolioBack = getPortfolioRandomSampleGenerator();

        photo.setPortfolio(portfolioBack);
        assertThat(photo.getPortfolio()).isEqualTo(portfolioBack);

        photo.portfolio(null);
        assertThat(photo.getPortfolio()).isNull();
    }

    @Test
    void cartTest() throws Exception {
        Photo photo = getPhotoRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        photo.setCart(cartBack);
        assertThat(photo.getCart()).isEqualTo(cartBack);

        photo.cart(null);
        assertThat(photo.getCart()).isNull();
    }
}
