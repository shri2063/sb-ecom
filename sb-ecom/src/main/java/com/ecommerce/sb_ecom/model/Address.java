package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name="addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=5, message = "Street name must be atleast 5 characters")
    private String street;

    @NotBlank
    @Size(min=5, message = "building name must be atleast 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min=5, message = "city name must be atleast 5 characters")
    private String city;

    @NotBlank
    @Size(min=5, message = "state name must be atleast 5 characters")
    private String state;

    @NotBlank
    @Size(min=5, message = "country name must be atleast 5 characters")
    private String country;

    @NotBlank
    @Size(min=5, message = "pincode must be atleast 5 characters")
    private String pincode;
    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private Set<User> users;


    public Address(Long id, String street, String buildingName, String city, String state, String country, String pincode) {
        this.id = id;
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
