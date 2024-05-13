package backend.domain;

import static backend.domain.PaymentTestSamples.*;
import static backend.domain.ReceiptTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceiptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Receipt.class);
        Receipt receipt1 = getReceiptSample1();
        Receipt receipt2 = new Receipt();
        assertThat(receipt1).isNotEqualTo(receipt2);

        receipt2.setId(receipt1.getId());
        assertThat(receipt1).isEqualTo(receipt2);

        receipt2 = getReceiptSample2();
        assertThat(receipt1).isNotEqualTo(receipt2);
    }

    @Test
    void paymentTest() throws Exception {
        Receipt receipt = getReceiptRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        receipt.setPayment(paymentBack);
        assertThat(receipt.getPayment()).isEqualTo(paymentBack);
        assertThat(paymentBack.getReceipt()).isEqualTo(receipt);

        receipt.payment(null);
        assertThat(receipt.getPayment()).isNull();
        assertThat(paymentBack.getReceipt()).isNull();
    }
}
