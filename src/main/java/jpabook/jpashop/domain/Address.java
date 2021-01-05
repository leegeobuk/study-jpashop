package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    private Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public static Address of() {
        return new Address();
    }

    public static Address of(String city, String street, String zipcode) {
        return new Address(city, street, zipcode);
    }

    public String getFullAddress() {
        return getCity() + " " + getStreet() + " " + getZipcode();
    }
}
