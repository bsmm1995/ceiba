package com.ceiba.library.controller;

import com.ceiba.library.domain.dto.LoanDto;
import com.ceiba.library.domain.dto.LoanInDto;
import com.ceiba.library.domain.dto.LoanOutDto;
import com.ceiba.library.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * API to manage loans
 *
 * @author: Bladimir Minga
 * @version: 06/07/2022
 */
@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanController {
    private final LoanService loanService;

    /**
     * Get all loans
     *
     * @return All registered loans
     */
    @GetMapping
    public ResponseEntity<List<LoanDto>> getAll() {
        log.info("Endpoint to get all loans.");
        return ResponseEntity.ok(this.loanService.getAll());
    }

    /**
     * Get a loan for your ID
     *
     * @param id Loan ID to be searched
     * @return Loan found
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getById(@PathVariable long id) {
        log.info("Endpoint to get loan by ID. id=" + id);
        return ResponseEntity.ok(this.loanService.getById(id));
    }

    /**
     * Create a new loan
     *
     * @param data Object containing the new loan data
     * @return New loan created
     */
    @PostMapping
    public ResponseEntity<LoanOutDto> create(@RequestBody @Valid LoanInDto data) {
        log.info("Endpoint to create a loan. data=" + data);
        return ResponseEntity.ok(this.loanService.create(data));
    }

    /**
     * Updating an existing loan
     *
     * @param id   Loan ID to be updated
     * @param data Object containing the loan data to be updated
     * @return Updated loan
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoanDto> update(@PathVariable long id, @RequestBody @Valid LoanInDto data) {
        log.info("Endpoint to update a loan. id=" + id + ", data=" + data);
        return ResponseEntity.ok(this.loanService.update(id, data));
    }

    /**
     * Delete a loan by its ID
     *
     * @param id ID of the loan to be eliminated
     * @return ID of the loan that was eliminated
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable long id) {
        log.info("Endpoint to delete a loan. id" + id);
        return ResponseEntity.ok(this.loanService.deleteById(id));
    }
}
