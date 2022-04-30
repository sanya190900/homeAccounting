package com.homeaccounting.controllers;

import com.homeaccounting.dto.wallet.WalletAndValue;
import com.homeaccounting.dto.wallet.WalletForTransfer;
import com.homeaccounting.services.WalletsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for receiving wallet requests.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/wallets")
public class WalletsController {

    private final WalletsService walletsService;

    /**
     * Dependency Injection constructor.
     *
     * @param walletsService wallet service
     */
    @Autowired
    public WalletsController(WalletsService walletsService) {
        this.walletsService = walletsService;
    }

    /**
     * Method for receiving get request on path '/wallets'.
     *
     * @return List of all wallets
     */
    @GetMapping
    public ResponseEntity<List<WalletForTransfer>> getAllWallets() {
        return walletsService.getAllWallets();
    }

    /**
     * Method for receiving get request on path '/wallets/values'.
     *
     * @return List of all wallets with current values
     */
    @GetMapping("/values")
    public ResponseEntity<List<WalletAndValue>> getWalletsAndValues() {
        return walletsService.getWalletsAndValues();
    }

    /**
     * Method for receiving put request on path '/wallets'.
     *
     * @param wallet wallet for add
     * @return added wallet
     */
    @PutMapping()
    public ResponseEntity<?> addWallet(
            @RequestBody WalletForTransfer wallet
    ) {
        return walletsService.addWallet(wallet);
    }

    /**
     * Method for receiving post request on path '/wallets'.
     *
     * @param wallet wallet for update
     * @return updated wallet
     */
    @PostMapping()
    public ResponseEntity<?> updateWallet(
            @RequestBody WalletForTransfer wallet
    ) {
        return walletsService.updateWallet(wallet);
    }

    /**
     * Method for receiving post request on path '/wallet'.
     *
     * @param idWallet id of wallet for deletion
     * @return status of deletion
     */
    @DeleteMapping("/{idWallet}")
    public ResponseEntity<?> deleteWallet(
            @PathVariable long idWallet
    ) {
        return walletsService.deleteWallet(idWallet);
    }
}
