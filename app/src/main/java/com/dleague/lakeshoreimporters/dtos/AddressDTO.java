package com.dleague.lakeshoreimporters.dtos;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "AddressDTO")
public class AddressDTO implements Serializable {
    @PrimaryKey(autoGenerate = true)
    Long id;
    @ColumnInfo(name = "ADDRESS_ID")
    String addressId;
    @ColumnInfo(name = "FIRST_NAME")
    String firstName;
    @ColumnInfo(name = "LAST_NAME")
    String lastName;
    @ColumnInfo(name = "COMPANY")
    String company;
    @ColumnInfo(name = "ADDRESS_1")
    String address1;
    @ColumnInfo(name = "ADDRESS_2")
    String address2;
    @ColumnInfo(name = "CITY")
    String city;
    @ColumnInfo(name = "STATE")
    String state;
    @ColumnInfo(name = "COUNTRY")
    String country;
    @ColumnInfo(name = "POSTAL_CODE")
    String postalCode;
    @ColumnInfo(name = "PHONE")
    String phone;
    @ColumnInfo(name = "CURSOR")
    String cursor;

    public AddressDTO() {
    }

    @Ignore
    public AddressDTO(String firstName, String lastName, String company, String address1, String address2,
                      String city, String state, String country, String postalCode, String phone) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setCompany(company);
        this.setAddress1(address1);
        this.setAddress2(address2);
        this.setCity(city);
        this.setState(state);
        this.setCountry(country);
        this.setPostalCode(postalCode);
        this.setPhone(phone);
    }

    @Ignore
    public AddressDTO(String addressId, String firstName, String lastName, String company, String address1, String address2, String city, String state, String country, String postalCode, String phone, String cursor) {
        this.setAddressId(addressId);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setCompany(company);
        this.setAddress1(address1);
        this.setAddress2(address2);
        this.setCity(city);
        this.setState(state);
        this.setCountry(country);
        this.setPostalCode(postalCode);
        this.setPhone(phone);
        this.setCursor(cursor);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
