package com.ecommerce.sbecom.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name should be atleast 5 characters.")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name should be atleast 5 characters.")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City name should be atleast 4 characters.")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name should be atleast 2 characters.")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name should be atleast 2 characters.")
    private String country;

    @NotBlank
    @Size(min = 5, message = "Pincode name should be atleast 5 characters.")
    private String pincode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String buildingName, String city, String country, String pincode, String state, String street) {
        this.buildingName = buildingName;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
        this.state = state;
        this.street = street;
    }
}
