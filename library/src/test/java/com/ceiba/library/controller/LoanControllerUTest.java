package com.ceiba.library.controller;

import com.ceiba.library.domain.dto.LoanDto;
import com.ceiba.library.domain.dto.LoanInDto;
import com.ceiba.library.domain.dto.LoanOutDto;
import com.ceiba.library.domain.entities.LoanEntity;
import com.ceiba.library.repository.LoanRepository;
import com.ceiba.library.service.LoanService;
import com.ceiba.library.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanControllerUTest {
    private LoanRepository loanRepositoryMock;
    private LoanService loanService;
    private final String isbn = "asd-123";
    private final String userId = "1234567890";
    private final int userType = 2;

    @BeforeEach
    void setUp() {
        this.loanRepositoryMock = Mockito.mock(LoanRepository.class);
        this.loanService = new LoanServiceImpl(loanRepositoryMock);
    }

    @Test
    void getAll() {
        when(this.loanRepositoryMock.findAll()).thenReturn(Arrays.asList(getEntity(1), getEntity(2)));
        List<LoanDto> result = this.loanService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    void getById() {
        long id = 1;
        when(this.loanRepositoryMock.findById(id)).thenReturn(Optional.of(getEntity(id)));
        LoanDto result = this.loanService.getById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void create() {
        long id = 1;
        when(this.loanRepositoryMock.save(any())).thenReturn(getEntity(id));
        LoanOutDto result =
                this.loanService.create(new LoanInDto(isbn, userId, userType));
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void update() {
        long id = 1;
        when(this.loanRepositoryMock.findById(id)).thenReturn(Optional.of(getEntity(id)));
        when(this.loanRepositoryMock.save(any())).thenReturn(getEntity(id));
        LoanDto result =
                this.loanService.update(id, new LoanInDto(isbn, userId, userType));
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(isbn, result.getIsbn());
    }

    @Test
    void deleteById() {
        long id = 1;
        when(this.loanRepositoryMock.findById(id)).thenReturn(Optional.of(getEntity(id)));
        long result = this.loanService.deleteById(id);
        assertEquals(id, result);
    }

    private LoanEntity getEntity(long id) {
        LoanEntity entity = new LoanEntity();
        entity.setId(id);
        entity.setIsbn(isbn);
        entity.setUserId(userId + id);
        entity.setUserType(userType);
        entity.setMaxReturnDate(LocalDateTime.now());
        return entity;
    }
}
