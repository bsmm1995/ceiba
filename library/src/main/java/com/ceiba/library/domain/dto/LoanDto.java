package com.ceiba.library.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoanDto implements Serializable {
    private Long id;
    private String isbn;
    private String userId;
    private Integer userType;
    private String maxReturnDate;
}
