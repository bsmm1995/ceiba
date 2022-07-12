package com.ceiba.library.controller;

import com.ceiba.library.domain.dto.LoanDto;
import com.ceiba.library.domain.dto.LoanInDto;
import com.ceiba.library.domain.dto.LoanOutDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class LoanControllerITest {
    @Autowired
    private LoanController loanController;

    @Test
    void getAll() {
        ResponseEntity<List<LoanDto>> result = this.loanController.getAll();
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        this.loanController.create(getDto());

        result = this.loanController.getAll();
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void create() {
        ResponseEntity<LoanOutDto> resultSaved = this.loanController.create(getDto());
        assertNotNull(resultSaved.getBody());
        assertEquals(1, resultSaved.getBody().getId());
    }

    private LoanInDto getDto() {
        LoanInDto dto = new LoanInDto();
        String isbn = "asd-123";
        dto.setIsbn(isbn);
        String userId = "1234567890";
        dto.setUserId(userId);
        dto.setUserType(1);
        return dto;
    }
}
