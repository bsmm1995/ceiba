package com.ceiba.library.service.impl;

import com.ceiba.library.domain.dto.LoanDto;
import com.ceiba.library.domain.dto.LoanInDto;
import com.ceiba.library.domain.dto.LoanOutDto;
import com.ceiba.library.domain.entities.LoanEntity;
import com.ceiba.library.exceptions.CeibaNotAllowedException;
import com.ceiba.library.exceptions.CeibaNotFoundException;
import com.ceiba.library.repository.LoanRepository;
import com.ceiba.library.service.LoanService;
import com.ceiba.library.utils.Constants;
import com.ceiba.library.utils.ManageDate;
import com.ceiba.library.utils.Mapper;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final Converter<LocalDateTime, String> dateToStringConverter;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        dateToStringConverter =
                new AbstractConverter<LocalDateTime, String>() {
                    protected String convert(LocalDateTime source) {
                        return new SimpleDateFormat("dd/MM/yyyy").format(Timestamp.valueOf(source));
                    }
                };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getAll() {
        return this.loanRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoanDto getById(long id) {
        return toDto(getEntityById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LoanOutDto create(LoanInDto data) {
        validateLoan(data);
        LoanEntity entity = toEntity(data);

        switch (entity.getUserType()) {
            case Constants.AFFILIATED_USER:
                entity.setMaxReturnDate(
                        ManageDate.addDaysFromToday(Constants.AFFILIATED_USER_DAYS));
                break;
            case Constants.EMPLOYEE_USER:
                entity.setMaxReturnDate(ManageDate.addDaysFromToday(Constants.EMPLOYEE_USER_DAYS));
                break;
            case Constants.INVITED_USER:
                entity.setMaxReturnDate(ManageDate.addDaysFromToday(Constants.INVITED_USER_DAYS));
                break;
            default:
                throw new CeibaNotAllowedException("Tipo de usuario no permitido en la biblioteca");
        }

        return toOutDto(this.loanRepository.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoanDto update(long id, LoanInDto data) {
        LoanEntity entity = getEntityById(id);
        entity.setIsbn(data.getIsbn());
        entity.setUserId(data.getUserId());
        entity.setUserType(data.getUserType());
        return toDto(this.loanRepository.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long deleteById(long id) {
        getById(id);
        this.loanRepository.deleteById(id);
        return id;
    }

    private LoanEntity getEntityById(long id) {
        return this.loanRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new CeibaNotFoundException(
                                        String.format("The loan with ID %s does not exist.", id)));
    }

    private void validateLoan(LoanInDto data) {
        Optional<LoanEntity> optional =
                this.loanRepository.findByUserIdAndUserType(
                        data.getUserId(), data.getUserType());
        optional.ifPresent(
                entity -> {
                    if (entity.getUserType() == Constants.INVITED_USER) {
                        throw new CeibaNotAllowedException(
                                String.format(
                                        "El usuario con identificación %s ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo",
                                        data.getUserId()));
                    }
                });
    }

    private LoanDto toDto(@NotNull LoanEntity entity) {
        return Mapper.modelMapper()
                .typeMap(LoanEntity.class, LoanDto.class)
                .addMappings(
                        mapper ->
                                mapper
                                        .using(dateToStringConverter)
                                        .map(LoanEntity::getMaxReturnDate, LoanDto::setMaxReturnDate))
                .map(entity);
    }

    private LoanOutDto toOutDto(@NotNull LoanEntity entity) {
        return Mapper.modelMapper()
                .typeMap(LoanEntity.class, LoanOutDto.class)
                .addMappings(
                        mapper ->
                                mapper
                                        .using(dateToStringConverter)
                                        .map(
                                                LoanEntity::getMaxReturnDate, LoanOutDto::setMaxReturnDate))
                .map(entity);
    }

    private LoanEntity toEntity(@NotNull LoanInDto dto) {
        return Mapper.modelMapper().typeMap(LoanInDto.class, LoanEntity.class).addMappings(mapper -> mapper.skip(LoanEntity::setId)).map(dto);
    }
}
