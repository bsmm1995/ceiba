package com.ceiba.library.service;

import com.ceiba.library.domain.dto.LoanDto;
import com.ceiba.library.domain.dto.LoanInDto;
import com.ceiba.library.domain.dto.LoanOutDto;

import java.util.List;

/**
 * Manage the logic of loans.
 *
 * @author: Bladimir Minga
 * @version: 06/07/2022
 */
public interface LoanService {

    /**
     * Get all loans
     *
     * @return All registered loans
     */
    List<LoanDto> getAll();

    /**
     * Get a loan for your ID
     *
     * @param id Loan ID to be searched
     * @return Loan found
     */
    LoanDto getById(long id);

    /**
     * Create a new loan
     *
     * @param data Object containing the new loan data
     * @return New loan created
     */
    LoanOutDto create(LoanInDto data);

    /**
     * Updating an existing loan
     *
     * @param id   Loan ID to be updated
     * @param data Object containing the loan data to be updated
     * @return Updated loan
     */
    LoanDto update(long id, LoanInDto data);

    /**
     * Delete a loan by its ID
     *
     * @param id ID of the loan to be eliminated
     * @return ID of the loan that was eliminated
     */
    long deleteById(long id);
}
