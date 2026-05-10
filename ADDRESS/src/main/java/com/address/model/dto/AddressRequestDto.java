package com.address.model.dto;

import com.address.model.enums.AddressType;

public class AddressRequestDto {

    private Long id;
    private String street;
    private Long pinCode;
    private String city;
    private String country;
    private AddressType addressType;

    public AddressRequestDto() {
    }

    public AddressRequestDto(Long id, String street, Long pinCode, String city, String country, AddressType addressType) {
        this.id = id;
        this.street = street;
        this.pinCode = pinCode;
        this.city = city;
        this.country = country;
        this.addressType = addressType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getPinCode() {
        return pinCode;
    }

    public void setPinCode(Long pinCode) {
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return "AddressRequestDto{" + "id=" + id + ", street='" + street + '\'' + ", pinCode=" + pinCode + ", city='" + city + '\'' + ", country='" + country + '\'' + ", addressType=" + addressType + '}';
    }

}
