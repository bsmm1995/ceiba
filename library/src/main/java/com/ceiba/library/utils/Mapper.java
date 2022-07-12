package com.ceiba.library.utils;

import org.modelmapper.ModelMapper;

/**
 * Utility to map entities and dtos
 *
 * @author: Bladimir Minga
 * @version: 05/07/2022
 */
public class Mapper {
    private Mapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
