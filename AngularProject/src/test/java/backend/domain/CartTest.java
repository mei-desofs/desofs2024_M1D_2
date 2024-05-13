package backend.domain;

import static backend.domain.CartTestSamples.*;
import static backend.domain.PaymentTestSamples.*;
import static backend.domain.PhotoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cart.class);
        Cart cart1 = getCartSample1();
        Cart cart2 = new Cart();
        assertThat(cart1).isNotEqualTo(cart2);

        cart2.setId(cart1.getId());
        assertThat(cart1).isEqualTo(cart2);

        cart2 = getCartSample2();
        assertThat(cart1).isNotEqualTo(cart2);
    }

    @Test
    void paymentTest() throws Exception {
        Cart cart = getCartRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        cart.setPayment(paymentBack);
        assertThat(cart.getPayment()).isEqualTo(paymentBack);

        cart.payment(null);
        assertThat(cart.getPayment()).isNull();
    }

    @Test
    void photosTest() throws Exception {
        Cart cart = getCartRandomSampleGenerator();
        Photo photoBack = getPhotoRandomSampleGenerator();

        cart.addPhotos(photoBack);
        assertThat(cart.getPhotos()).containsOnly(photoBack);
        assertThat(photoBack.getCart()).isEqualTo(cart);

        cart.removePhotos(photoBack);
        assertThat(cart.getPhotos()).doesNotContain(photoBack);
        assertThat(photoBack.getCart()).isNull();

        cart.photos(new HashSet<>(Set.of(photoBack)));
        assertThat(cart.getPhotos()).containsOnly(photoBack);
        assertThat(photoBack.getCart()).isEqualTo(cart);

        cart.setPhotos(new HashSet<>());
        assertThat(cart.getPhotos()).doesNotContain(photoBack);
        assertThat(photoBack.getCart()).isNull();
    }
}
