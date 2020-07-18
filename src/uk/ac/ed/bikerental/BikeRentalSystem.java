package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

public class BikeRentalSystem {
    private Collection<Customer> customers;
    private Collection<BikeProvider> bikeProviders;
    private Collection<BikeType> bikeTypes;
    private Collection<Long> bookingIds;
    private long maxId = 0L;
    // Singleton class
    private static BikeRentalSystem instance = new BikeRentalSystem();

    public static BikeRentalSystem getInstance() {
        return instance;
    }

    private BikeRentalSystem() {
        customers = new HashSet<Customer>();
        bikeProviders = new HashSet<BikeProvider>();
        bikeTypes = new HashSet<BikeType>();
        bookingIds = new HashSet<Long>();
    }

    public boolean addBikeProvider(BikeProvider bikeProvider) {
        return bikeProviders.add(bikeProvider);
    }

    public boolean addCusomter(Customer customer) {
        return customers.add(customer);
    }

    public boolean addBikeType(BikeType bikeType) {
        return bikeTypes.add(bikeType);
    }

    public boolean addBikeType(String name, BigDecimal replacementValue) {
        return bikeTypes.add(new BikeType(name, replacementValue));
    }

    public boolean addBookingId(long bookingId) {
        return bookingIds.add(bookingId);
    }

    public Optional<BikeType> findBikeType(String name) {
        for (BikeType type : bikeTypes) {
            if (type.getName().equals(name)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public Collection<Quote> getQuotes(Map<BikeType, Integer> bikes, DateRange dateRange, Location location) {
        Collection<Quote> result = new HashSet<Quote>();
        for (BikeProvider bikeProvider : bikeProviders) {
            if (bikeProvider.getLocation().isNearTo(location)) {
                Optional<Quote> potentialOffer = bikeProvider.getQuote(bikes, dateRange);
                if (potentialOffer.isPresent()) {
                    result.add(potentialOffer.get());
                }
            }
        }
        return result;
    }

    public Booking bookQuote(Quote quote, String firstName, String lastName, String address, String postCode,
            String phoneNumber, String email, String password, boolean storeCollection) {
        return this.bookQuote(quote, new Customer(firstName, lastName, address, postCode, phoneNumber, email, password),
                storeCollection);
    }

    public Booking bookQuote(Quote quote, Customer customer, boolean storeCollection) {
        long bookingId = generateBookingId(quote, customer);
        String orderSummary = "Order Summary:\nCustomer: " + customer.getFirstName() + " " + customer.getLastName()
                + "\n";
        orderSummary += "Booking ID: " + bookingId + "\n";
        orderSummary += "Date Range: " + quote.getDateRange().getStart() + " to " + quote.getDateRange().getEnd()
                + "\n";
        orderSummary += "Bike Provider: " + quote.getBikeProvider().getName() + "\n";
        orderSummary += "Total Price: " + quote.getTotalPrice() + "\n";
        orderSummary += "Deposit: " + quote.getDeposit() + "\n";
        orderSummary += "In Store Collection: " + storeCollection;

        Booking booking = new Booking(bookingId, quote, customer, storeCollection, customer.getAddress(), orderSummary);

        // Functionality for a payment system is assumed to happen here.
        //
        // This is because we need to confirm the payment for the booking first
        // before we can reserve bikes, set up deliveries, and add the booking
        // to the bike provider.

        quote.getBikeProvider().addBooking(booking);

        if (!storeCollection) {
            for (Bike bike : quote.getBikes()) {
                DeliveryServiceFactory.getDeliveryService().scheduleDelivery(bike,
                        quote.getBikeProvider().getLocation(), customer.getAddress(), quote.getDateRange().getStart());
            }
            DeliveryServiceFactory.getDeliveryService().scheduleDelivery(booking.getDeposit(),
                    quote.getBikeProvider().getLocation(), customer.getAddress(), quote.getDateRange().getStart());
        }

        quote.reserveBikes();
        displayConfirmation(booking);
        return booking;
    }

    public long generateBookingId(Quote quote, Customer customer) {
        maxId++;
        long bookingId = maxId;
        return bookingId;
    }

    public boolean isCustomer(Customer customer) {
        return customers.contains(customer);
    }

    public boolean confirmBooking(Booking booking) {
        // some i/o for displayConfirmation, where the customer can then select yes or
        // no
        return true;
    }

    public String displayConfirmation(Booking booking) {
        // some function to the output of the system that displays confirmation
        String confirmation = "";
        confirmation += "Please confirm your booking information is correct:\n";
        confirmation += booking.toString();
        return confirmation;
    }

    public boolean containsType(BikeType bikeType) {
        return bikeTypes.contains(bikeType);
    }

    /**
     * Method resets state of the system for JUnit testing. Use ONLY for testing
     * purposes.
     * 
     */
    public static void resetForTest() {
        instance = new BikeRentalSystem();

    }
}
