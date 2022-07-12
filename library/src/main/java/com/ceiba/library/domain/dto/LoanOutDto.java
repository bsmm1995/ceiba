package com.ceiba.library.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoanOutDto implements Serializable {
    private Long id;
    private String maxReturnDate;
}
