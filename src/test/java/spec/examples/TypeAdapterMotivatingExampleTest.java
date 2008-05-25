package spec.examples;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

@RunWith(ConcordionRunner.class)
public class TypeAdapterMotivatingExampleTest {


    // The method I would like to use, but can't because I get the error
    // "Unable to convert type java.lang.String of 0.01 to type of spec.examples.TypeAdapterMotivatingExampleTest$Money"
    public Money deliveryChargeForA(Money orderTotal) {
        return new DeliveryChargeCalculator().chargeFor(orderTotal);
    }

    // This fails to work, because concordion is using the toString() method
    // of the money object and then comparing strings, rather than converting the
    // expected value to money and using .equals()
    public Money deliveryChargeForB(String orderTotal) {
        return new DeliveryChargeCalculator().chargeFor(new Money(orderTotal));
    }

    // This works, but onyl after I added a "Money.format(String)" method so that
    // I could force the display to match the amount in the test.
    public String deliveryChargeFor(String orderTotal) {
        final Money charge = new DeliveryChargeCalculator().chargeFor(new Money(orderTotal));
        return charge.format("%.2f");

    }

    public static class DeliveryChargeCalculator {

        public Money chargeFor(Money orderTotal) {
            return new Money("10");
        }
    }

    public static class Money {
        private final BigDecimal amount;

        public Money(String amount) {
            if (amount == null) throw new IllegalArgumentException("Amount must be non-null");
            this.amount = new BigDecimal(amount);
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Money money = (Money) o;

            return amount.compareTo(money.amount) == 0;

        }

        public int hashCode() {
            return amount.hashCode();
        }

        public String toString() {
            return amount.toString();
        }

        public String format(String formatString) {
            return String.format(formatString, amount);
        }
    }

}