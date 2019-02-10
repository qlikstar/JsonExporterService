package com.decision.engines.exporter.model;

import com.decision.engines.exporter.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address extends DateAudit {
    /**
     *
     */
    private static final long serialVersionUID = 1990797570522025038L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String streetAddress;
    @Column
    private String additionalAddress;
    @Column
    private String houseNo;

    @Column(nullable = false)
    private String city;
    @Column
    private String stateCode;
    @Column
    private Integer zipcode;

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getAdditionalAddress() {
        return additionalAddress;
    }

    public void setAdditionalAddress(String additionalAddress) {
        this.additionalAddress = additionalAddress;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }
}
