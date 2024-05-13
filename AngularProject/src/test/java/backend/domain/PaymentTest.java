package backend.domain;

import static backend.domain.CartTestSamples.*;
import static backend.domain.PaymentTestSamples.*;
import static backend.domain.ReceiptTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void receiptTest() throws Exception {
        Payment payment = getPaymentRandomSampleGenerator();
        Receipt receiptBack = getReceiptRandomSampleGenerator();

        payment.setReceipt(receiptBack);
        assertThat(payment.getReceipt()).isEqualTo(receiptBack);

        payment.receipt(null);
        assertThat(payment.getReceipt()).isNull();
    }

    @Test
    void cartTest() throws Exception {
        Payment payment = getPaymentRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        payment.setCart(cartBack);
        assertThat(payment.getCart()).isEqualTo(cartBack);
        assertThat(cartBack.getPayment()).isEqualTo(payment);

        payment.cart(null);
        assertThat(payment.getCart()).isNull();
        assertThat(cartBack.getPayment()).isNull();
    }
}
