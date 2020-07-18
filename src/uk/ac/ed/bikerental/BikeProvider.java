
package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class BikeProvider {
    private String name;
    private Location shopAddress;
    private String phoneNumber;
    private TimeRange openingHours;
    private Map<BikeType, BigDecimal> dailyRentalPrice;
    private BigDecimal depositRate;
    private Map<BikeType, Collection<Bike>> bikes;
    private Collection<BikeProvider> partners;
    private Collection<Booking> bookings;
    private ValuationPolicy valuationPolicy;
    private PricingPolicy pricingPolicy;

    public BikeProvider(String name, String postCode, String streetAddress, String phoneNumber, TimeRange openingHours,
            BigDecimal depositRate) {
        this.name = name;
        this.shopAddress = new Location(postCode, streetAddress);
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
        this.depositRate = depositRate;
        this.bikes = new HashMap<BikeType, Collection<Bike>>();
        this.dailyRentalPrice = new HashMap<BikeType, BigDecimal>();
        this.partners = new HashSet<BikeProvider>();
        this.bookings = new HashSet<Booking>();
        BikeRentalSystem.getInstance().addBikeProvider(this);
        this.valuationPolicy = new StandardValuationPolicy();
        this.pricingPolicy = new StandardPricingPolicy();

    }

    public void setCustomValuationPolicy(ValuationPolicy valuationPolicy) {
        this.valuationPolicy = valuationPolicy;
    }

    public void setCustomPricingPolicy(PricingPolicy pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
        for (Entry<BikeType, BigDecimal> entry : dailyRentalPrice.entrySet()) {
            pricingPolicy.setDailyRentalPrice(entry.getKey(), entry.getValue());
        }
    }

    public Optional<Booking> registerDepositReturn(long bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                booking.registerDepositReturn();
                return Optional.of(booking);

            }
        }
        return Optional.empty();
    }

    public void registerDepositReturnToPartner(BikeProvider partner, long bookingId) {
        Optional<Booking> bookingOpt = partner.registerDepositReturn(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Provider transfers money to partner
            booking.registerDepositReturn();
        } else {
            System.out.println("Booking not found. Please communicate directly with partner to resolve the issue.");
        }
    }

    public void registerBikeReturnToPartner(BikeProvider partner, long bikeId) {
        Optional<Bike> bikeOpt = partner.registerBikeReturn(bikeId);
        ;
        if (bikeOpt.isPresent()) {
            DeliveryServiceFactory.getDeliveryService().scheduleDelivery(bikeOpt.get(), this.shopAddress,
                    partner.getLocation(), LocalDate.now());
        } else {
            System.out.println("Bike not found. Please communicate directly with partner to resolve the issue.");
        }
    }

    public Optional<Bike> registerBikeReturn(long bikeId) {
        for (BikeType key : bikes.keySet()) {
            for (Bike bike : bikes.get(key)) {
                if (bike.getBikeId() == bikeId) {
                    /*
                     * if registerBikeReturnToPartner calls this method it call this line but it is
                     * OK as we know that delivery person we deliver bike over night
                     */
                    bike.setInStore(true);
                    return Optional.of(bike);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((openingHours == null) ? 0 : openingHours.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + ((shopAddress == null) ? 0 : shopAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BikeProvider other = (BikeProvider) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (openingHours == null) {
            if (other.openingHours != null)
                return false;
        } else if (!openingHours.equals(other.openingHours))
            return false;
        if (phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        if (shopAddress == null) {
            if (other.shopAddress != null)
                return false;
        } else if (!shopAddress.equals(other.shopAddress))
            return false;
        return true;
    }

    public Optional<Quote> getQuote(Map<BikeType, Integer> bikeMap, DateRange dateRange) {
        assert !bikeMap.keySet().isEmpty();
        ArrayList<Bike> bikesToOffer = new ArrayList<Bike>();
        for (BikeType bikeType : bikeMap.keySet()) {
            int quantity = bikeMap.get(bikeType);
            Collection<Bike> bikesOfCurrentType = findBikes(bikeType, quantity, dateRange);
            if (bikesOfCurrentType.size() == quantity) {
                bikesToOffer.addAll(bikesOfCurrentType);
            } else {
                return Optional.empty();
            }
        }
        BigDecimal totalPrice = pricingPolicy.calculatePrice(bikesToOffer, dateRange);
        BigDecimal totalDeposit = calculateDeposit(bikesToOffer, dateRange);
        Quote quote = new Quote(this, dateRange, totalPrice, totalDeposit, bikesToOffer);
        return Optional.of(quote);
    }

    private BigDecimal calculateDeposit(ArrayList<Bike> bikesToOffer, DateRange dateRange) {
        BigDecimal result = new BigDecimal(0);
        for (Bike bike : bikesToOffer) {
            result = result.add(valuationPolicy.calculateValue(bike, LocalDate.now()));
        }
        return result.multiply(depositRate);
    }

    private Collection<Bike> findBikes(BikeType bikeType, int quantity, DateRange dateRange) {
        Collection<Bike> result = new ArrayList<Bike>();
        if (bikes.keySet().contains(bikeType)) {
            Collection<Bike> listOfBikes = bikes.get(bikeType);
            int counter = 0;
            for (Bike bike : listOfBikes) {
                if (bike.isAvailable(dateRange)) {
                    result.add(bike);
                    counter++;
                    if (counter == quantity) {
                        break;
                    }
                }
            }
        }
        return result;

    }

    public boolean isPartner(BikeProvider partner) {
        assert partner != null;
        return partners.contains(partner);
    }

    public boolean addPartner(BikeProvider partner) {
        assert partner != null;
        return partners.add(partner);
    }

    public boolean addBooking(Booking booking) {
        assert booking != null;
        return bookings.add(booking);
    }

    public void makeBookingCompleted(long bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                booking.makeCompleted();
            }
        }
    }

    public void addBike(Bike bike) throws IllegalArgumentException {
        BikeType type = bike.getType();
        if (dailyRentalPrice.containsKey(bike.getType())) {
            if (!bikes.keySet().contains(type)) {
                bikes.put(type, new ArrayList<Bike>());
            }
            bikes.get(type).add(bike);
        } else {
            throw new IllegalArgumentException("Set daily price for this type of bike first.");
        }
    }

    public void addBike(long bikeid, BikeType type, LocalDate manufactureDate) throws IllegalArgumentException {
        if (dailyRentalPrice.containsKey(type)) {
            this.addBike(new Bike(bikeid, type, manufactureDate));
        } else {
            throw new IllegalArgumentException("Set daily price for this type of bike first.");
        }
    }

    public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) throws IllegalArgumentException {
        if (BikeRentalSystem.getInstance().containsType(bikeType)) {
            dailyRentalPrice.put(bikeType, dailyPrice);
            pricingPolicy.setDailyRentalPrice(bikeType, dailyPrice);
        } else {
            throw new IllegalArgumentException("BikeType not recognized. Please add bike type to the system first.");
        }
    }

    public void addBikeType(String name, BigDecimal replecementValue) {
        BikeRentalSystem.getInstance().addBikeType(name, replecementValue);
    }

    public void addBike(long bikeid, String bikeType, LocalDate manufactureDate) throws IllegalArgumentException {
        Optional<BikeType> typeFromSystem = BikeRentalSystem.getInstance().findBikeType(bikeType);
        if (typeFromSystem.isPresent()) {
            this.addBike(new Bike(bikeid, typeFromSystem.get(), manufactureDate));
        } else {
            throw new IllegalArgumentException(
                    "This type of bike is not in database." + " Please add it first using addBikeType()");
        }
    }

    public Location getLocation() {
        return shopAddress;
    }

    public String getName() {
        return name;
    }

}
