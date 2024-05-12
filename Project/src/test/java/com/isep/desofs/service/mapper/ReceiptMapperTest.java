package com.isep.desofs.service.mapper;

import static com.isep.desofs.domain.ReceiptAsserts.*;
import static com.isep.desofs.domain.ReceiptTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReceiptMapperTest {

    private ReceiptMapper receiptMapper;

    @BeforeEach
    void setUp() {
        receiptMapper = new ReceiptMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReceiptSample1();
        var actual = receiptMapper.toEntity(receiptMapper.toDto(expected));
        assertReceiptAllPropertiesEquals(expected, actual);
    }
}
