package com.homeaccounting.services;

import com.homeaccounting.dao.Transaction;
import com.homeaccounting.dao.Wallet;
import com.homeaccounting.dto.AmountByCategory;
import com.homeaccounting.dto.AmountByDate;
import com.homeaccounting.repositories.TransactionsRepository;
import com.homeaccounting.repositories.WalletsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service for manipulations with dashboard.
 */
@Service
public class DashboardService {

    private final TransactionsRepository transactionsRepository;
    private final WalletsRepository walletsRepository;

    /**
     * Dependency Injection constructor.
     *
     * @param transactionsRepository repository for transactions
     * @param walletsRepository repository for wallets
     */
    @Autowired
    public DashboardService(TransactionsRepository transactionsRepository, WalletsRepository walletsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.walletsRepository = walletsRepository;
    }

    /**
     * Get usage amount by date in period.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List of amounts by date
     */
    public ResponseEntity<List<AmountByDate>> getUsageByPeriodAndWallet(String periodStart, String periodStop, Long idWallet) {
        List<AmountByDate> usage = new ArrayList<>();                       // Resulting list
        Instant instantStart = Instant.parse(periodStart);                  // Parsing format ISO string date time into Instant
        Instant instantStop = Instant.parse(periodStop);                    // Parsing format ISO string date time into Instant
        BigDecimal sum = getWalletValueBeforeDate(idWallet, instantStart);  // Requesting value in wallet before specified date time
        boolean added = false;                                              // Flag for controlling added data to the result list
        boolean end = false;                                                // Flag for controlling out of bounds exception into transactions list

        List<Transaction> transactions = transactionsRepository             // Get transactions by wallet in period order by Date.
                .findTransactionsByDateBetweenAndWalletIdOrderByDate(
                        Instant.parse(periodStart),
                        Instant.parse(periodStop),
                        idWallet
                );

        if (transactions.size() > 0) {                                      // If transactions exist
            Iterator<Transaction> it = transactions.iterator();             // For iterating over transactions
            Transaction transaction = it.next();                            // Iterated transaction
            for (                                                           // Loop by days into specified period
                    int i = 0;
                    i < Duration.between(
                            instantStart,
                            instantStop.isBefore(Instant.now()) ? instantStop : Instant.now()   // if end of period > current date, then current date = end of period
                    ).toDays() + 1;
                    i++
            ) {
                Instant endOfDayCurrDate = instantStart                 // find end time of current date
                        .plus(i + 1, ChronoUnit.DAYS)
                        .minus(1, ChronoUnit.SECONDS);   // i + 1 because 2022-04-01T00:00:00 + 1 day - 1 second = 2022-04-01T23:59:59

                // Finding current usage by day into specified period
                while (true) {
                    if (transaction.getDate().isBefore(endOfDayCurrDate) && !end) {     // If iterated transaction was in current date, then
                        sum = sum.add(transaction.getValue());                          // updating sum
                        usage.add(new AmountByDate(transaction.getDate(), sum));        // and adding this value into resulting list
                        if (it.hasNext())                                               // If exist next transaction
                            transaction = it.next();                                    // iterate over it
                        else
                            end = true;                                                 // Finishing filling resulting list
                        added = true;                                                   // Mark added flag as true
                    } else {                                                            // if iterated transaction was not in current date, then
                        if (!added)                                                     // if it was not added into resulting list
                            usage.add(new AmountByDate(endOfDayCurrDate, sum));         // Adding amount of value before this date

                        added = false;                                                  // Marking added as false
                        break;                                                          // Break from while loop
                    }
                }
            }
        }

        return new ResponseEntity<>(usage, HttpStatus.OK);
    }

    /**
     * Getting value by wallet before specified date.
     *
     * @param idWallet wallet id
     * @param periodStart counting value before this date
     * @return value by wallet before specified date
     */
    public BigDecimal getWalletValueBeforeDate(long idWallet, Instant periodStart) {
        BigDecimal value = transactionsRepository.sumValueByDateBeforeAndWalletOrderByDate(periodStart, idWallet);
        return value != null ? value : BigDecimal.valueOf(0);   // Checking for null value (if null return 0)
    }

    /**
     * Getting amount by wallet and category 'expenses' in period.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List of amounts by categories
     */
    public ResponseEntity<List<AmountByCategory>> getUsageByPeriodAndWalletGroupByCategoryExpenses(String periodStart, String periodStop, Long idWallet) {
        List<AmountByCategory> usage;
        Wallet wallet = walletsRepository.findWalletById(idWallet);

        usage = transactionsRepository.getUsageByPeriodAndWalletGroupByCategoryExpenses(Instant.parse(periodStart), Instant.parse(periodStop), wallet);

        for (AmountByCategory abc: usage) {
            abc.setValue(abc.getValue().abs());
        }

        return new ResponseEntity<>(usage, HttpStatus.OK);
    }

    /**
     * Getting amount by wallet and category 'incomes' in period.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List of amounts by categories
     */
    public ResponseEntity<List<AmountByCategory>> getUsageByPeriodAndWalletGroupByCategoryIncome(String periodStart, String periodStop, Long idWallet) {
        List<AmountByCategory> usage;
        Wallet wallet = walletsRepository.findWalletById(idWallet);

        usage = transactionsRepository.getUsageByPeriodAndWalletGroupByCategoryIncome(Instant.parse(periodStart), Instant.parse(periodStop), wallet);

        return new ResponseEntity<>(usage, HttpStatus.OK);
    }

    /**
     * Getting date time of first transaction in specified wallet.
     *
     * @param idWallet wallet id
     * @return date time of first transaction
     */
    public ResponseEntity<Instant> getFirstTransactionDateByWallet(Long idWallet) {
        Wallet wallet = walletsRepository.findWalletById(idWallet);

        return new ResponseEntity<>(transactionsRepository.findFirstByWalletOrderByDate(wallet).getDate(), HttpStatus.OK);
    }
}
