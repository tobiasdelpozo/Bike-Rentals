package uk.ac.ed.bikerental;

import java.math.BigDecimal;

public class BikeType {
    private String name;
    private BigDecimal replecementValue;

    public BikeType(String name, BigDecimal replecementValue) {
        assert !name.isEmpty();
        this.name = name;
        this.replecementValue = replecementValue;
        BikeRentalSystem.getInstance().addBikeType(this);
    }

    public BigDecimal getReplacementValue() {
        return replecementValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReplecementValue(BigDecimal replecementValue) {
        this.replecementValue = replecementValue;
    }

}