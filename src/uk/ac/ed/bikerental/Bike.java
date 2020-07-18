package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class Bike implements Deliverable {
    private long bikeId;
    private BikeType type;
    private Collection<DateRange> datesReserved;
    private boolean isInStore;
    private boolean isBeingDelivered;
    private boolean deliveryComplete;
    private LocalDate dateOfProduction;

    public Bike(long bikeId, BikeType type, LocalDate manufactureDate) {
        this.bikeId = bikeId;
        this.type = type;
        this.isInStore = true;
        this.datesReserved = new ArrayList<DateRange>();
        this.isBeingDelivered = false;
        this.deliveryComplete = false;
        this.dateOfProduction = manufactureDate;
    }

    public LocalDate getDateOfProduction() {
        return dateOfProduction;
    }

    public void setDateOfProduction(LocalDate dateOfProduction) {
        this.dateOfProduction = dateOfProduction;
    }

    public BikeType getType() {
        return type;
    }

    public boolean isAvailable(DateRange reserveRange) {
        for (DateRange dateRange : datesReserved) {
            if (dateRange.overlaps(reserveRange)) {
                return false;
            }
        }
        return true;
    }

    public boolean reserve(DateRange dateRange) {
        if (this.isAvailable(dateRange)) {
            datesReserved.add(dateRange);
            return true;
        } else {
            return false;
        }
    }

    public Collection<DateRange> getDatesReserved() {
        return datesReserved;
    }

    public void setDatesReserved(Collection<DateRange> datesReserved) {
        this.datesReserved = datesReserved;
    }

    public long getBikeId() {
        return bikeId;
    }

    public void setBikeId(long bikeId) {
        this.bikeId = bikeId;
    }

    public boolean isBeingDelivered() {
        return isBeingDelivered;
    }

    public boolean isInStore() {
        return isInStore;
    }

    public void setInStore(boolean isInStore) {
        this.isInStore = isInStore;
    }

    public void setType(BikeType type) {
        this.type = type;
    }

    public boolean isDeliveryComplete() {
        return deliveryComplete;
    }

    @Override
    public void onPickup() {
        isBeingDelivered = true;
        deliveryComplete = false;
        isInStore = false;

    }

    @Override
    public void onDropoff() {
        isBeingDelivered = false;
        deliveryComplete = true;
        isInStore = true;
    }

}