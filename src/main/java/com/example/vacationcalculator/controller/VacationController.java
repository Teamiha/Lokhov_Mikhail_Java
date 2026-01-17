package com.example.vacationcalculator.controller;

import com.example.vacationcalculator.dto.VacationPayRequest;
import com.example.vacationcalculator.dto.VacationPayResponse;
import com.example.vacationcalculator.service.VacationPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST controller for vacation pay calculation endpoint.
 * 
 * <p>Exposes a single GET endpoint `/calculate` that accepts query parameters
 * and returns calculated vacation pay according to Russian labor law conventions.</p>
 */
@RestController
@RequestMapping("/calculate")
@Validated
public class VacationController {

    private static final Logger log = LoggerFactory.getLogger(VacationController.class);
    
    private final VacationPayService vacationPayService;

    /**
     * Constructor for dependency injection.
     * 
     * @param vacationPayService vacation pay calculation service
     */
    public VacationController(VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
    }

    /**
     * Calculates vacation pay based on provided query parameters.
     * 
     * <p>Accepts query parameters bound to VacationPayRequest:
     * <ul>
     *   <li>averageSalary (required): Average monthly salary for the last 12 months</li>
     *   <li>vacationDays (optional): Number of vacation days</li>
     *   <li>startDate (optional): Vacation start date (YYYY-MM-DD)</li>
     *   <li>endDate (optional): Vacation end date (YYYY-MM-DD)</li>
     * </ul>
     * </p>
     * 
     * <p>Either vacationDays or both startDate and endDate must be provided.</p>
     * 
     * <p>Example: GET /calculate?averageSalary=100000&vacationDays=28</p>
     * 
     * @param request vacation pay calculation request bound from query parameters
     * @return response containing calculated vacation pay, payable days, and details
     */
    @GetMapping
    public ResponseEntity<VacationPayResponse> calculate(
            @Valid @ModelAttribute VacationPayRequest request) {
        
        log.info("Received vacation pay calculation request: averageSalary={}, vacationDays={}, startDate={}, endDate={}",
                request.getAverageSalary(), request.getVacationDays(),
                request.getStartDate(), request.getEndDate());

        VacationPayResponse response = vacationPayService.calculatePay(request);

        log.info("Calculated vacation pay: {} rubles for {} days",
                response.getVacationPay(), response.getPayableDays());

        return ResponseEntity.ok(response);
    }
}
