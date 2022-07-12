package com.ceiba.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanInDto implements Serializable {
    @NotEmpty(message = "The ISBN cannot be null and void.")
    @Size(max = 10, message = "The isbn must be a maximum of 10 characters.")
    private String isbn;

    @NotEmpty(message = "The user ID cannot be null and void.")
    @Size(max = 10, message = "The Customer ID must be a maximum of 10 characters.")
    private String userId;

    @NotNull(message = "The user type cannot be null.")
    @Range(min = 1, max = 5, message = "The user type is a digit greater than zero and less than 4.")
    private Integer userType;
}
