package uk.ac.ed.bikerental;

/**
 * Represents a location which is made up of an address and a postcode.
 * 
 * @author Tobias & Michal
 *
 */

public class Location {
    private String postcode;
    private String address;

    /**
     * Creates an instance of Location class with strings that represent the
     * postcode and address of the Location.
     * 
     * @param postcode String that represents the postcode of the location.
     * @param address  String that represents the address of the location.
     */

    public Location(String postcode, String address) {
        assert postcode.length() >= 6;
        this.postcode = postcode;
        this.address = address;
    }

    /**
     * This method checks if some other Location passed to it as an argument is near
     * to to the Location on which it is invoked.The way it does this is by checking
     * if the first two characters of the postcode attribute of both classes is the
     * same. If it is then they are near each other and the method returns true.
     * Otherwise it returns false.
     * 
     * @param other The Location to which we are comparing to the Location that
     *              calls the equals() method.
     * @return boolean A boolean value representing whether or not the two addresses
     *         are near each other. True if they are, false if they are not.
     */

    public boolean isNearTo(Location other) {
        if (postcode.substring(0, 2).equals(other.postcode.substring(0, 2)))
            return true;
        return false;
    }

    /**
     * This method returns the postcode of Location on which the method is invoked.
     * 
     * @return postcode A String representing the postcode of the location.
     */

    public String getPostcode() {
        return postcode;
    }

    /**
     * This method returns the address of Location on which the method is invoked.
     * 
     * @return address A String representing the address of the location.
     */

    public String getAddress() {
        return address;
    }

    // You can add your own methods here
}
