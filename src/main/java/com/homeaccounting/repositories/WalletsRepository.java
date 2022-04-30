package com.homeaccounting.repositories;

import com.homeaccounting.dao.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for working with wallets and database.
 */
public interface WalletsRepository extends JpaRepository<Wallet, Long> {

    /**
     * Get all wallets.
     *
     * @return list of wallets
     */
    List<Wallet> findAll();

    /**
     * Get wallet by id.
     *
     * @param idWallet id of wallet
     * @return wallet
     */
    Wallet findWalletById(Long idWallet);
}
