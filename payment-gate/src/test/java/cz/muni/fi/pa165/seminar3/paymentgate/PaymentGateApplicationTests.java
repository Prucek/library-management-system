package cz.muni.fi.pa165.seminar3.paymentgate;

import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(TransactionController.class)
class PaymentGateApplicationTests {
    @Test
    void contextLoads() {
    }
}
