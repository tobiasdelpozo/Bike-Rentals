package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MockValuationPolicy implements ValuationPolicy {
    // based on replacement value of the bike and deposit rate of bike provider

    @Override
    public BigDecimal calculateValue(Bike bike, LocalDate date) {
        BigDecimal returnOne = new BigDecimal(1);
        return returnOne;
    }

}
