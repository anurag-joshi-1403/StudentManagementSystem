package com.anurag.sms.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.anurag.sms.TestData;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.entity.Student;

/**
 * #34: the dashboard's "Fees Collected" added up every fee, pending ones
 * included (₹120,000 shown for ₹60,000 paid).
 */
@DataJpaTest
class FeeTotalsTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private FeeRepository fees;

    @Test
    void collectedCountsOnlyPaidFeesAndOutstandingTheRest() {
        Course course = em.persist(TestData.course("BSC-PHY", "B.Sc Physics"));
        Student student = em.persist(TestData.student("Diya", "Patel", course));

        Fee paid = TestData.fee(student);
        paid.setPaymentStatus("Paid");
        em.persist(paid);
        em.persist(TestData.fee(student));
        em.flush();

        assertThat(fees.sumAmountByPaymentStatus("Paid")).isEqualByComparingTo("60000.00");
        assertThat(fees.sumAmountByPaymentStatusNot("Paid")).isEqualByComparingTo("60000.00");
    }

    @Test
    void anEmptyTableGivesZeroNotNull() {
        assertThat(fees.sumAmountByPaymentStatus("Paid")).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(fees.sumAmountByPaymentStatusNot("Paid")).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
