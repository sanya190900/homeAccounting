package com.homeaccounting.controllers;

import com.homeaccounting.dto.AmountByCategory;
import com.homeaccounting.dto.AmountByDate;
import com.homeaccounting.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

/**
 * Controller for receiving dashboard requests.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    /**
     * Dependency Injection constructor.
     *
     * @param dashboardService dashboard service
     */
    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Method for receiving get request on path '/dashboard/usage/byDate'.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List of amounts by date in period
     */
    @GetMapping("/usage/byDate")
    public ResponseEntity<List<AmountByDate>> getUsageByPeriodAndWallet(
            @RequestParam String periodStart,
            @RequestParam String periodStop,
            @RequestParam Long idWallet
    ) {
        return dashboardService.getUsageByPeriodAndWallet(periodStart, periodStop, idWallet);
    }

    /**
     * Method for receiving get request on path '/dashboard/usage/byCategory/expenses'.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List expenses of amounts by category
     */
    @GetMapping("/usage/byCategory/expenses")
    public ResponseEntity<List<AmountByCategory>> getUsageByPeriodAndWalletGroupByCategoryExpenses(
            @RequestParam String periodStart,
            @RequestParam String periodStop,
            @RequestParam Long idWallet
    ) {
        return dashboardService.getUsageByPeriodAndWalletGroupByCategoryExpenses(periodStart, periodStop, idWallet);
    }

    /**
     * Method for receiving get request on path '/dashboard/usage/byCategory/income'.
     *
     * @param periodStart starting date time period
     * @param periodStop ending date time period
     * @param idWallet wallet id
     * @return List incomes of amounts by category
     */
    @GetMapping("/usage/byCategory/income")
    public ResponseEntity<List<AmountByCategory>> getUsageByPeriodAndWalletGroupByCategoryIncome(
            @RequestParam String periodStart,
            @RequestParam String periodStop,
            @RequestParam Long idWallet
    ) {
        return dashboardService.getUsageByPeriodAndWalletGroupByCategoryIncome(periodStart, periodStop, idWallet);
    }

    /**
     * Method for receiving get request on path '/dashboard/firstTransactionDate'.
     *
     * @param idWallet wallet id
     * @return date time of first transaction by wallet id
     */
    @GetMapping("/firstTransactionDate")
    public ResponseEntity<Instant> getFirstTransactionDateByWallet(
            @RequestParam Long idWallet
    ) {
        return dashboardService.getFirstTransactionDateByWallet(idWallet);
    }
}
