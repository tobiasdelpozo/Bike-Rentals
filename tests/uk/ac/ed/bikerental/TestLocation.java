package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TestLocation {

    private Location location1, location2, location3, location4, location5, location6;

    @BeforeEach
    void setUp() throws Exception {
        this.location1 = new Location("EH85XX", "5/2 Bristo Square");
        this.location2 = new Location("NW19AJ", "Aldenham Street");
        this.location3 = new Location("NW00564", "Upper West Side");
        this.location4 = new Location("EH165AY", "Pollock Halls");
        this.location5 = new Location("EH1XAYZ", null);
        this.location6 = null;
    }

    @Test
    void postcodeTooShort() {
        assertThrows(AssertionError.class, () -> {
            new Location("FAIL", "Fail City");
        }, "Postcode is less than 6 chars so throws AssertionError");

        assertThrows(AssertionError.class, () -> {
            new Location("", "Fail City");
        }, "Postcode is less than 6 chars so throws AssertionError");

        assertThrows(AssertionError.class, () -> {
            new Location("", "");
        }, "Postcode is less than 6 chars so throws AssertionError");
    }

    @Test
    void testIsNearTo() {
        assertEquals(true, this.location1.isNearTo(location4), "First two chars of postcode are equal so are near");
        assertEquals(false, this.location1.isNearTo(location2),
                "First two chars of postcode are NOT equal so are NOT near");
        assertEquals(false, this.location3.isNearTo(location4),
                "First two chars of postcode are NOT equal so are NOT near");
        assertEquals(true, this.location2.isNearTo(location3), "First two chars of postcode are equal so are near");
    }

    @Test
    void testGetPostCode() {
        assertEquals("EH85XX", this.location1.getPostcode());
        assertEquals("NW19AJ", this.location2.getPostcode());
        assertEquals("NW00564", this.location3.getPostcode());
        assertEquals("EH165AY", this.location4.getPostcode());
    }

    @Test
    void testGetAddress() {
        assertEquals("5/2 Bristo Square", this.location1.getAddress());
        assertEquals("Aldenham Street", this.location2.getAddress());
        assertEquals("Upper West Side", this.location3.getAddress());
        assertEquals("Pollock Halls", this.location4.getAddress());
    }

    @Test
    void testNulls() {
        assertNull(location5.getAddress());
        assertThrows(NullPointerException.class, () -> location6.getAddress());
        assertThrows(NullPointerException.class, () -> location6.getPostcode());
        assertThrows(NullPointerException.class, () -> location6.isNearTo(location1));
    }

}
