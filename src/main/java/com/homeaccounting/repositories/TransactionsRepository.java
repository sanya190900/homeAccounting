package com.homeaccounting.repositories;

import com.homeaccounting.dao.Transaction;
import com.homeaccounting.dao.Wallet;
import com.homeaccounting.dto.AmountByCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Repository for working with transactions and database.
 */
@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long> {

    /**
     * Get transactions by wallet in period order by Date.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List of transactions
     */
    List<Transaction> findTransactionsByDateBetweenAndWalletIdOrderByDate(Instant periodStart, Instant periodStop, long idWallet);

    /**
     * Get first transaction in wallet.
     *
     * @param wallet wallet
     * @return first transaction
     */
    Transaction findFirstByWalletOrderByDate(Wallet wallet);

    /**
     * Get value of wallet before date.
     *
     * @param periodStart starting date time period
     * @param idWallet wallet id
     * @return amount of money that were in wallet before specified date
     */
    @Nullable
    @Query("SELECT SUM(t.value) FROM Transaction t WHERE t.date < :periodStart AND t.wallet.id = :idWallet")
    BigDecimal sumValueByDateBeforeAndWalletOrderByDate(@Param("periodStart") Instant periodStart, @Param("idWallet") long idWallet);

    /**
     * Get usage by wallet in period in category usages.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param wallet wallet
     * @return list of expenses (values) in period by categories in specified wallet
     */
    @Nullable
    @Query("SELECT new com.homeaccounting.dto.AmountByCategory(c, SUM(t.value)) FROM Transaction t INNER JOIN Category c ON t.category.id = c.id WHERE t.date > :periodStart AND t.date < :periodStop AND t.wallet = :wallet AND c.idgroup.id = 1 GROUP BY t.category, c.id")
    List<AmountByCategory> getUsageByPeriodAndWalletGroupByCategoryExpenses(@Param("periodStart") Instant periodStart, @Param("periodStop") Instant periodStop, @Param("wallet") Wallet wallet);

    /**
     * Get usage by wallet in period in category income.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param wallet wallet
     * @return list of incomes (values) in period by categories in specified wallet
     */
    @Nullable
    @Query("SELECT new com.homeaccounting.dto.AmountByCategory(c, SUM(t.value)) FROM Transaction t INNER JOIN Category c ON t.category.id = c.id WHERE t.date > :periodStart AND t.date < :periodStop AND t.wallet = :wallet AND c.idgroup.id = 2 GROUP BY t.category, c.id")
    List<AmountByCategory> getUsageByPeriodAndWalletGroupByCategoryIncome(@Param("periodStart") Instant periodStart, @Param("periodStop") Instant periodStop, @Param("wallet") Wallet wallet);
}
