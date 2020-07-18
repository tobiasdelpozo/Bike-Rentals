package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDateRange {
    private DateRange dateRange1, dateRange2, dateRange3, dateRange4, dateRange5, dateRange6, dateRange7, dateRange8;

    @BeforeEach
    void setUp() throws Exception {
        // Setup resources before each test
        this.dateRange1 = new DateRange(LocalDate.of(2019, 1, 7), LocalDate.of(2019, 1, 10));
        this.dateRange2 = new DateRange(LocalDate.of(2019, 1, 5), LocalDate.of(2019, 1, 23));
        this.dateRange3 = new DateRange(LocalDate.of(2015, 1, 7), LocalDate.of(2018, 1, 10));
        this.dateRange4 = new DateRange(LocalDate.of(1753, 5, 5), LocalDate.of(3000, 5, 7));
        this.dateRange5 = new DateRange(LocalDate.of(3001, 1, 1), LocalDate.of(3001, 2, 1));
        this.dateRange6 = new DateRange(LocalDate.of(3001, 2, 1), LocalDate.of(3001, 2, 2));
        this.dateRange7 = new DateRange(null, null);
        this.dateRange8 = null;

    }

    // Sample JUnit tests checking toYears works
    @Test
    void testToYears1() {
        assertEquals(0, this.dateRange1.toYears());
    }

    @Test
    void testToYears3() {
        assertEquals(3, this.dateRange3.toYears());
    }

    @Test
    void testOverlapsTrue() {
        assertEquals(true, this.dateRange1.overlaps(dateRange2));
        assertEquals(true, this.dateRange2.overlaps(dateRange1));
        assertEquals(true, this.dateRange1.overlaps(dateRange4));
        assertEquals(true, this.dateRange2.overlaps(dateRange4));
        assertEquals(true, this.dateRange3.overlaps(dateRange4));
        assertEquals(true, this.dateRange4.overlaps(dateRange1));
        assertEquals(true, this.dateRange4.overlaps(dateRange2));
        assertEquals(true, this.dateRange4.overlaps(dateRange3));
    }

    @Test
    void testOverlapsFalse() {
        assertEquals(false, this.dateRange1.overlaps(dateRange3));
        assertEquals(false, this.dateRange3.overlaps(dateRange1));
        assertEquals(false, this.dateRange2.overlaps(dateRange3));
        assertEquals(false, this.dateRange3.overlaps(dateRange2));
        assertEquals(false, this.dateRange4.overlaps(dateRange5));
        assertEquals(false, this.dateRange1.overlaps(dateRange5));
        assertEquals(false, this.dateRange2.overlaps(dateRange5));
        assertEquals(false, this.dateRange3.overlaps(dateRange5));
        assertEquals(false, this.dateRange4.overlaps(dateRange5));
        assertEquals(false, this.dateRange5.overlaps(dateRange1));
        assertEquals(false, this.dateRange5.overlaps(dateRange2));
        assertEquals(false, this.dateRange5.overlaps(dateRange3));
        assertEquals(false, this.dateRange5.overlaps(dateRange4));
    }

    @Test
    void testEdgeCases() {
        assertEquals(true, this.dateRange1.overlaps(dateRange1));
        assertEquals(true, this.dateRange5.overlaps(dateRange6));
        assertEquals(true, this.dateRange6.overlaps(dateRange5));
    }

    @Test
    void testNullDateRange() {
        assertThrows(NullPointerException.class, () -> dateRange7.toDays());
        assertThrows(NullPointerException.class, () -> dateRange8.toDays());
        assertThrows(NullPointerException.class, () -> dateRange7.toYears());
        assertThrows(NullPointerException.class, () -> dateRange8.toYears());
        assertThrows(NullPointerException.class, () -> dateRange7.overlaps(dateRange8));
        assertThrows(NullPointerException.class, () -> dateRange8.overlaps(dateRange7));
    }

    @Test
    void testNotNullToDays() {
        assertNotNull(this.dateRange1.toDays());
        assertNotNull(this.dateRange2.toDays());
        assertNotNull(this.dateRange3.toDays());
        assertNotNull(this.dateRange4.toDays());
        assertNotNull(this.dateRange5.toDays());
        assertNotNull(this.dateRange6.toDays());
    }

    @Test
    void testNotNullToYears() {
        assertNotNull(this.dateRange1.toYears());
        assertNotNull(this.dateRange2.toYears());
        assertNotNull(this.dateRange3.toYears());
        assertNotNull(this.dateRange4.toYears());
        assertNotNull(this.dateRange5.toYears());
        assertNotNull(this.dateRange6.toYears());
    }

    @Test
    void testToYears4() {
        assertEquals(1247, this.dateRange4.toYears());
    }

    @Test
    void testToDays() {
        assertEquals(3, this.dateRange1.toDays());
        assertEquals(18, this.dateRange2.toDays());
        assertEquals(1099, this.dateRange3.toDays());
        assertEquals(455459, this.dateRange4.toDays());
        // I used an online date range calculator for the longer days
    }

}
