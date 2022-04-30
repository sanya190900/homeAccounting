package com.homeaccounting.services;

import com.homeaccounting.dao.Category;
import com.homeaccounting.dao.Transaction;
import com.homeaccounting.dao.Wallet;
import com.homeaccounting.dto.CategoryForTransfer;
import com.homeaccounting.dto.GroupForTransfer;
import com.homeaccounting.dto.TransactionForTransfer;
import com.homeaccounting.dto.wallet.WalletForTransfer;
import com.homeaccounting.repositories.TransactionsRepository;
import com.homeaccounting.repositories.WalletsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for manipulations with transactions.
 */
@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final WalletsRepository walletsRepository;

    /**
     * Dependency Injection constructor.
     *
     * @param transactionsRepository repository for transactions
     * @param walletsRepository repository for wallets
     */
    @Autowired
    public TransactionsService(TransactionsRepository transactionsRepository, WalletsRepository walletsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.walletsRepository = walletsRepository;
    }

    /**
     * Getting transactions by wallet in period.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List of transactions
     */
    public ResponseEntity<List<TransactionForTransfer>> getTransactionsByPeriodAndWallet(String periodStart, String periodStop, Long idWallet) {
        Wallet wallet = walletsRepository.findWalletById(idWallet);     // Find specified wallet by id
        List<Transaction> transactions = transactionsRepository         // Find transactions in specified period by wallet ordered by date
                .findTransactionsByDateBetweenAndWalletIdOrderByDate(
                        Instant.parse(periodStart),
                        Instant.parse(periodStop),
                        idWallet
                        );

        List<TransactionForTransfer> transactionsForTransfer = new ArrayList<>(); // Resulting list
        // Preparing transaction for transfer
        transactions.forEach(
                transaction ->
                    transactionsForTransfer.add(
                            new TransactionForTransfer(
                                    transaction.getId(),
                                    transaction.getDate(),
                                    transaction.getDescription(),
                                    transaction.getPhoto(),
                                    transaction.getValue(),
                                    new CategoryForTransfer(
                                            transaction.getCategory().getId(),
                                            transaction.getCategory().getName(),
                                            transaction.getCategory().getIcon(),
                                            new GroupForTransfer(
                                                    transaction.getCategory().getIdgroup().getId(),
                                                    transaction.getCategory().getIdgroup().getType()
                                            )
                                    ),
                                    new WalletForTransfer(
                                            wallet.getId(),
                                            wallet.getName(),
                                            wallet.getCurrency()
                                    )
                            )
                    )
        );

        return new ResponseEntity<>(transactionsForTransfer, HttpStatus.OK);
    }

    /**
     * Adding transaction to database.
     *
     * @param transactionForTransfer transaction for adding
     * @return added transaction.
     */
    public ResponseEntity<?> addTransaction(TransactionForTransfer transactionForTransfer) {
        Transaction transaction = buildTransaction(transactionForTransfer);

        return new ResponseEntity<>(transactionsRepository.save(transaction), HttpStatus.CREATED);
    }

    /**
     * Updating specified transaction in database.
     *
     * @param transactionForTransfer transaction for update
     * @return updated transaction.
     */
    public ResponseEntity<?> updateTransaction(TransactionForTransfer transactionForTransfer) {
        Transaction transaction = buildTransaction(transactionForTransfer);

        return new ResponseEntity<>(transactionsRepository.save(transaction), HttpStatus.OK);
    }

    /**
     * Deleting specified transaction by id.
     *
     * @param idTransaction transaction id
     * @return deletion status
     */
    public ResponseEntity<?> deleteTransaction(long idTransaction) {
        transactionsRepository.deleteById(idTransaction);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Converting TransactionFroTransfer to DAO Transaction
     *
     * @param transactionForTransfer transaction for transfer
     * @return converted transaction
     */
    private Transaction buildTransaction(TransactionForTransfer transactionForTransfer) {
        Transaction transaction = new Transaction();
        Category category = new Category();
        Wallet wallet = new Wallet();

        category.setId(transactionForTransfer.getCategory().getId());
        wallet.setId(transactionForTransfer.getWallet().getId());

        transaction.setId(transactionForTransfer.getId());
        transaction.setDate(transactionForTransfer.getDate());
        transaction.setDescription(transactionForTransfer.getDescription());
        transaction.setPhoto(transactionForTransfer.getPhoto());
        transaction.setValue(transactionForTransfer.getValue());
        transaction.setWallet(wallet);
        transaction.setCategory(category);

        return transaction;
    }
}
