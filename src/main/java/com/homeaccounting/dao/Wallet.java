package com.homeaccounting.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Representation table 'wallets' as a model.
 */
@Entity
@Table(name = "wallets")
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Transaction> transactions = new LinkedHashSet<>();

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

}
