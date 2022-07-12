package com.ceiba.library.qualifier;

import com.ceiba.library.domain.dto.LoanInDto;
import com.ceiba.library.domain.dto.LoanOutDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class LoansTest {

    public static final int USUARIO_AFILIADO = 1;
    public static final int USUARIO_EMPLEADO = 2;
    public static final int USUARIO_INVITADO = 3;
    public static final int USUARIO_DESCONOCIDO = 5;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void prestamoLibroUsuarioAfiliadoDeberiaAlmacenarCorrectamenteYCalcularFechaDeDevolucion() throws Exception {

        MvcResult resultadoLibroPrestado = mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("ASDA7884", "974148", USUARIO_AFILIADO)))).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate").exists()).andReturn();


        LoanOutDto resultadoPrestamo = objectMapper.readValue(resultadoLibroPrestado.getResponse().getContentAsString(), LoanOutDto.class);

        LocalDate fechaPrestamo = LocalDate.now();
        fechaPrestamo = addDaysSkippingWeekends(fechaPrestamo, 10);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        mvc.perform(MockMvcRequestBuilders.get("/loans/" + resultadoPrestamo.getId()).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate", is(fechaPrestamo.format(formato)))).andExpect(jsonPath("$.isbn", is("ASDA7884"))).andExpect(jsonPath("$.userId", is("974148"))).andExpect(jsonPath("$.userType", is(USUARIO_AFILIADO)));


    }

    @Test
    void prestamoLibroUsuarioEmpleadoDeberiaAlmacenarCorrectamenteYCalcularFechaDeDevolucion() throws Exception {

        MvcResult resultadoLibroPrestado = mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("AWQ489", "7481545", USUARIO_EMPLEADO)))).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate").exists()).andReturn();

        LoanOutDto resultadoPrestamo = objectMapper.readValue(resultadoLibroPrestado.getResponse().getContentAsString(), LoanOutDto.class);

        LocalDate fechaPrestamo = LocalDate.now();
        fechaPrestamo = addDaysSkippingWeekends(fechaPrestamo, 8);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        mvc.perform(MockMvcRequestBuilders.get("/loans/" + resultadoPrestamo.getId()).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate", is(fechaPrestamo.format(formato)))).andExpect(jsonPath("$.isbn", is("AWQ489"))).andExpect(jsonPath("$.userId", is("7481545"))).andExpect(jsonPath("$.userType", is(USUARIO_EMPLEADO)));


    }

    @Test
    void prestamoLibroUsuarioInvitadoDeberiaAlmacenarCorrectamenteYCalcularFechaDeDevolucion() throws Exception {

        MvcResult resultadoLibroPrestado = mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("EQWQW8545", "74851254", USUARIO_INVITADO)))).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate").exists()).andReturn();

        LoanOutDto resultadoPrestamo = objectMapper.readValue(resultadoLibroPrestado.getResponse().getContentAsString(), LoanOutDto.class);

        LocalDate fechaPrestamo = LocalDate.now();
        fechaPrestamo = addDaysSkippingWeekends(fechaPrestamo, 7);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        mvc.perform(MockMvcRequestBuilders.get("/loans/" + resultadoPrestamo.getId()).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate", is(fechaPrestamo.format(formato)))).andExpect(jsonPath("$.isbn", is("EQWQW8545"))).andExpect(jsonPath("$.userId", is("74851254"))).andExpect(jsonPath("$.userType", is(USUARIO_INVITADO)));


    }

    @Test
    void usuarioInvitadoTratandoDePrestarUnSegundoLibroDeberiaRetornarExcepcion() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("EQWQW8545", "1111111111", USUARIO_INVITADO)))).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate").exists());

        mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("EQWQW8545", "1111111111", USUARIO_INVITADO)))).andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message", is("El usuario con identificación 1111111111 ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo")));

    }

    @Test
    void usuarioNoInvitadoTratandoDePrestarUnSegundoLibroDeberiaPrestarloCorrectamente() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("EQWQW8545", "1111111111", USUARIO_AFILIADO)))).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate").exists());

        mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("EQWQW8545", "1111111111", USUARIO_AFILIADO)))).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.maxReturnDate").exists());

    }

    @Test
    void prestamoConTipoDeUsuarioNoPermitidoDeberiaRetornarExcepcion() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/loans").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new LoanInDto("EQWQW8545", "1111111111", USUARIO_DESCONOCIDO)))).andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message", is("Tipo de usuario no permitido en la biblioteca")));
    }


    static LocalDate addDaysSkippingWeekends(LocalDate date, int days) {
        LocalDate result = date;
        int addedDays = 0;
        while (addedDays < days) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++addedDays;
            }
        }
        return result;
    }
}
