package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.*;

public class PricingPolicyTests {

    private BigDecimal high, medium, low;
    private BikeType type1, type2, type3;
    private Bike bike1, bike2, bike3, bike4, bike5;
    private MultidayDiscountsPolicy policy1, policy2;
    private Collection<Bike> bikes;
    private LocalDate manufactureDate;

    @BeforeEach
    void setUp() throws Exception {
        this.high = new BigDecimal(1500);
        this.medium = new BigDecimal(300);
        this.low = new BigDecimal("50.12");
        this.manufactureDate = LocalDate.of(2010, 11, 1);

        type1 = new BikeType("type1", high);
        type2 = new BikeType("type1", medium);
        type3 = new BikeType("type1", low);
        bike1 = new Bike(1, type1, manufactureDate);
        bike2 = new Bike(2, type1, manufactureDate);
        bike3 = new Bike(3, type2, manufactureDate);
        bike4 = new Bike(4, type1, manufactureDate);
        bike5 = new Bike(5, type3, manufactureDate);
        bikes = new HashSet<Bike>();
        bikes.add(bike1);
        bikes.add(bike2);
        bikes.add(bike3);
        bikes.add(bike4);
        bikes.add(bike5);

        /*
         * policy1: 0-2 days 0% 3-4 days 5% 5+ days 10%
         */

        policy1 = new MultidayDiscountsPolicy();
        policy1.setDiscount(3, "0.05");
        policy1.setDiscount(5, "0.1");
        policy1.setDailyRentalPrice(type1, high);
        policy1.setDailyRentalPrice(type2, medium);
        policy1.setDailyRentalPrice(type3, low);

        /*
         * policy2: 0-2 days 0% 3-4 days 5% 5-10 days 2% 11+ days 11%
         */

        policy2 = new MultidayDiscountsPolicy();
        policy2.setDiscount(3, "0.05");
        policy2.setDiscount(5, "0.02");
        policy2.setDiscount(11, "0.11");
        policy2.setDailyRentalPrice(type1, high);
        policy2.setDailyRentalPrice(type2, medium);
        policy2.setDailyRentalPrice(type3, low);
    }

    /*
     * policy1
     *
     * testing strategy: partition number of days 0, [1,2], [3,4], 5+ Exhaustive
     * coverage of partitions.
     */

    @Test
    void test1() {
        // 4 days
        LocalDate date1 = LocalDate.of(2019, 11, 1);
        LocalDate date2 = LocalDate.of(2019, 11, 4);
        DateRange dateRange = new DateRange(date1, date2);
        // should be (3*1500+300+50.12)*0.95 = 4607.614 a day
        // overall
        assertEquals(policy1.calculatePrice(bikes, dateRange).compareTo(new BigDecimal("18430.456")), 0);
    }

    @Test
    void test2() {
        // 1 day
        LocalDate date1 = LocalDate.of(2019, 11, 1);
        LocalDate date2 = LocalDate.of(2019, 11, 1);
        DateRange dateRange = new DateRange(date1, date2);
        // should be (3*1500+300+50.12) = 4850.12
        assertEquals(policy1.calculatePrice(bikes, dateRange).compareTo(new BigDecimal("4850.12")), 0);
    }

    @Test
    void test3() {
        // 5 days
        LocalDate date1 = LocalDate.of(2019, 11, 1);
        LocalDate date2 = LocalDate.of(2019, 11, 5);
        DateRange dateRange = new DateRange(date1, date2);
        // should be (3*1500+300+50.12)*0.9 = 4365.108 a day
        // overall 21825.54
        assertEquals(policy1.calculatePrice(bikes, dateRange).compareTo(new BigDecimal("21825.54")), 0);
    }

    @Test
    void test4() {
        // 6 days
        LocalDate date1 = LocalDate.of(2019, 11, 1);
        LocalDate date2 = LocalDate.of(2019, 11, 6);
        DateRange dateRange = new DateRange(date1, date2);
        // should be (3*1500+300+50.12)*0.9 = 4365.108 a day
        // overall 26190.648
        assertEquals(policy1.calculatePrice(bikes, dateRange).compareTo(new BigDecimal("26190.648")), 0);
    }

    /*
     * policy 2 policy2Test 1 & 2 checks if implementation support if discounts are
     * not growing with number of days
     */

    @Test
    void policy2Test1() {
        // 11 days with policy2
        LocalDate date1 = LocalDate.of(2019, 11, 1);
        LocalDate date2 = LocalDate.of(2019, 11, 11);
        DateRange dateRange = new DateRange(date1, date2);
        // should be (3*1500+300+50.12)*0.89 = 4316.6068 a day
        // overall 47482.6748
        assertEquals(policy2.calculatePrice(bikes, dateRange).compareTo(new BigDecimal("47482.6748")), 0);
    }

    @Test
    void policy2Test2() {
        // 9 days with policy2
        LocalDate date1 = LocalDate.of(2019, 11, 2);
        LocalDate date2 = LocalDate.of(2019, 11, 10);
        DateRange dateRange = new DateRange(date1, date2);
        // should be (3*1500+300+50.12)*0.98 = 4753.1176
        // overall 42778.0584;
        assertEquals(policy2.calculatePrice(bikes, dateRange).compareTo(new BigDecimal("42778.0584")), 0);
    }

    // test support of reseting (all previous test were develop before
    // implementation, this is glass box)
    @Test
    void resetTest() {
        // 6 days after reseting discounts
        LocalDate date1 = LocalDate.of(2019, 11, 1);
        LocalDate date2 = LocalDate.of(2019, 11, 6);
        DateRange dateRange = new DateRange(date1, date2);
        policy1.resetDiscounts();
        // should be (3*1500+300+50.12)*1 = 4850.12 a day
        // overall 29100.72
        assertEquals(policy1.calculatePrice(bikes, dateRange).compareTo(new BigDecimal("29100.72")), 0);
    }

}