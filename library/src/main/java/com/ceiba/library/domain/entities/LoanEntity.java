package com.ceiba.library.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "CEIBA_LOAN")
@Getter
@Setter
public class LoanEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "ISBN", nullable = false, length = 10)
    private String isbn;

    @Column(name = "USER_IDENTIFICATION", nullable = false, length = 10)
    private String userId;

    @Column(name = "USER_TYPE", nullable = false)
    private Integer userType;

    @Column(
            name = "MAXIMUM_RETURN_DATE",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime maxReturnDate;
}
