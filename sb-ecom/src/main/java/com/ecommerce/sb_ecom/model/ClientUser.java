package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(
        name = "clent-users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames =  "username"),
                @UniqueConstraint(columnNames = "email")

        }
)
public class ClientUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long id;
    @Column(name = "email")
    @Email
    String email;
    @Column(name = "password")
    @NotBlank
    @Size(min = 5, message = "Password should be atleast of length 5")

    String password;
    @NotBlank
    @Size(min = 5, message = "user name should be atleast of length 5")
    @Column(name = "username")
    String userName;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user-role",
            joinColumns  = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @Getter
    @Setter
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Product> products = new HashSet<>();



    @ManyToMany(cascade = CascadeType.ALL)
    @Getter
    @Setter
    @JoinTable(
            name = "user-address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name= "address_id")
    )
    Set<Address> addresses = new HashSet<>();

    public ClientUser(Long id, String email, String password, String userName, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.roles = roles;
    }

    public ClientUser() {

    }
}
