package com.homeaccounting.services;

import com.homeaccounting.dao.Wallet;
import com.homeaccounting.dto.wallet.WalletAndValue;
import com.homeaccounting.dto.wallet.WalletForTransfer;
import com.homeaccounting.repositories.WalletsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for manipulations with wallets.
 */
@Service
public class WalletsService {

    private final WalletsRepository walletsRepository;
    private final DashboardService dashboardService;

    /**
     * Dependency Injection constructor.
     *
     * @param walletsRepository repository for wallet
     * @param dashboardService dashboard manipulation service
     */
    @Autowired
    public WalletsService(WalletsRepository walletsRepository, DashboardService dashboardService) {
        this.walletsRepository = walletsRepository;
        this.dashboardService = dashboardService;
    }

    /**
     * Getting all wallets.
     *
     * @return List of wallets
     */
    public ResponseEntity<List<WalletForTransfer>> getAllWallets() {
        return new ResponseEntity<>(buildWalletForTransfer(), HttpStatus.OK);
    }

    /**
     * Getting wallets and them values
     *
     * @return List of wallets and them values
     */
    public ResponseEntity<List<WalletAndValue>> getWalletsAndValues() {
        List<WalletForTransfer> walletsForTransfer = buildWalletForTransfer();
        List<WalletAndValue> walletsAndValue = new ArrayList<>();

        walletsForTransfer.forEach(
                walletForTransfer -> walletsAndValue.add(
                        new WalletAndValue(
                                walletForTransfer,
                                dashboardService.getWalletValueBeforeDate(walletForTransfer.getId(), Instant.now())
                        )
                )
        );

        return new ResponseEntity<>(walletsAndValue, HttpStatus.OK);
    }

    /**
     * Adding specified wallet into database.
     *
     * @param walletForTransfer wallet to add
     * @return added wallet
     */
    public ResponseEntity<?> addWallet(WalletForTransfer walletForTransfer) {
        Wallet wallet = buildWallet(walletForTransfer);

        return new ResponseEntity<>(walletsRepository.save(wallet), HttpStatus.CREATED);
    }

    /**
     * Updating specified wallet.
     *
     * @param walletForTransfer wallet to update
     * @return updated wallet
     */
    public ResponseEntity<?> updateWallet(WalletForTransfer walletForTransfer) {
        Wallet wallet = buildWallet(walletForTransfer);

        return new ResponseEntity<>(walletsRepository.save(wallet), HttpStatus.OK);
    }

    /**
     * Deleting specified wallet from database.
     *
     * @param idWallet wallet id
     * @return deletion status
     */
    public ResponseEntity<?> deleteWallet(long idWallet) {
        walletsRepository.deleteById(idWallet);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Converting WalletForTransfer to DAO Wallet
     *
     * @param walletForTransfer wallet for transfer
     * @return converted wallet
     */
    private Wallet buildWallet(WalletForTransfer walletForTransfer) {
        Wallet wallet = new Wallet();

        wallet.setId(walletForTransfer.getId());
        wallet.setName(walletForTransfer.getName());
        wallet.setCurrency(walletForTransfer.getCurrency());

        return wallet;
    }


    /**
     * Getting Wallets and converting them to Wallet for Transfer.
     *
     * @return converted wallet
     */
    private List<WalletForTransfer> buildWalletForTransfer() {
        List<Wallet> wallets = walletsRepository.findAll();
        List<WalletForTransfer> walletsForTransfer = new ArrayList<>();

        wallets.forEach(
                wallet -> walletsForTransfer.add(
                        new WalletForTransfer(
                                wallet.getId(),
                                wallet.getName(),
                                wallet.getCurrency()
                        )
                )
        );

        return  walletsForTransfer;
    }
}
