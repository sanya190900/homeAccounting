package com.homeaccounting.controllers;

import com.homeaccounting.dto.TransactionForTransfer;
import com.homeaccounting.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for receiving transactions requests.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    /**
     * Dependency Injection constructor.
     *
     * @param transactionsService transactions service
     */
    @Autowired
    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    /**
     * Method for receiving get request on path '/transactions'.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List of transactions by period
     */
    @GetMapping
    public ResponseEntity<List<TransactionForTransfer>> getTransactionsByPeriod(
            @RequestParam String periodStart,
            @RequestParam String periodStop,
            @RequestParam Long idWallet
    ) {
        return transactionsService.getTransactionsByPeriodAndWallet(periodStart, periodStop, idWallet);
    }

    /**
     * Method for receiving put request on path '/transactions'.
     *
     * @param transaction transaction for add
     * @return added transaction
     */
    @PutMapping()
    public ResponseEntity<?> addTransaction(
            @RequestBody TransactionForTransfer transaction
    ) {
        return transactionsService.addTransaction(transaction);
    }

    /**
     * Method for receiving post request on path '/transactions'.
     *
     * @param transaction transaction for update
     * @return updated transaction
     */
    @PostMapping()
    public ResponseEntity<?> updateTransaction(
            @RequestBody TransactionForTransfer transaction
    ) {
        return transactionsService.updateTransaction(transaction);
    }

    /**
     * Method for receiving post request on path '/transactions'.
     *
     * @param idTransaction id of transaction for deletion
     * @return status of deletion
     */
    @DeleteMapping("/{idTransaction}")
    public ResponseEntity<?> deleteTransaction(
            @PathVariable long idTransaction
    ) {
        return transactionsService.deleteTransaction(idTransaction);
    }
}
