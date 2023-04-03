package com.example.vmo_project;

import com.example.vmo_project.dto.FeeTypeDto;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.FeeType;
import com.example.vmo_project.repository.FeeTypeRepository;
import com.example.vmo_project.service.FeeTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeeTypeServiceTest {
    @Mock
    private FeeTypeRepository feeTypeRepository;

    @InjectMocks
    private FeeTypeService feeTypeService;

    @Test
    void testGetAll() {
        // Arrange
        FeeType feeType1 = new FeeType(1L, "electricity", 10000D, new ArrayList<>());
        FeeType feeType2 = new FeeType(2L, "water", 20000D, new ArrayList<>());

        List<FeeType> feeTypes = Arrays.asList(feeType1, feeType2);

        when(feeTypeRepository.findAll())
                .thenReturn(feeTypes);

        // Act
        List<FeeTypeDto> result = feeTypeService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
