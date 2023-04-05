package com.example.vmo_project;

import com.example.vmo_project.constant.ConstantError;
import com.example.vmo_project.dto.FeeTypeDto;
import com.example.vmo_project.entity.FeeType;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.FeeTypeRepository;
import com.example.vmo_project.request.UpdateFeePriceRequest;
import com.example.vmo_project.service.FeeTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeeTypeServiceTest {
    @Mock
    private FeeTypeRepository feeTypeRepository;

    @InjectMocks
    private FeeTypeService feeTypeService;

    @Test
    void testGetAllAndGetById() {
        // Arrange
        FeeType feeType1 = new FeeType(1L, "electricity", 10000D, new ArrayList<>());
        FeeType feeType2 = new FeeType(2L, "water", 20000D, new ArrayList<>());

        List<FeeType> feeTypes = Arrays.asList(feeType1, feeType2);

        when(feeTypeRepository.findAll())
                .thenReturn(feeTypes);
        when(feeTypeRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(feeTypes.get(0)));
        when(feeTypeRepository.findById(2L))
                .thenReturn(java.util.Optional.ofNullable(feeTypes.get(1)));

        // Act
        List<FeeTypeDto> result = feeTypeService.getAll();
        FeeTypeDto apartmentDto1 = feeTypeService.getById(1L);
        FeeTypeDto apartmentDto2 = feeTypeService.getById(2L);

        // Assert
        assertNotNull(result);
        assertNotNull(apartmentDto1);
        assertNotNull(apartmentDto2);
        assertEquals(2, result.size());
        assertEquals(apartmentDto1.getName(), result.get(0).getName());
        assertEquals(apartmentDto2.getPrice(), result.get(1).getPrice());
        assertNotEquals(apartmentDto2.getPrice(), result.get(0).getPrice());
        assertThrows(NotFoundException.class, () -> feeTypeService.getById(3L), ConstantError.FEE_TYPE_NOT_FOUND + 3L);
    }

    @Test
    void testUpdateExistingFeeType() {
        // Arrange
        UpdateFeePriceRequest request = new UpdateFeePriceRequest(10.0);
        FeeType feeType = new FeeType(1L, "Type1", 5.0, new ArrayList<>());
        when(feeTypeRepository.findById(1L))
                .thenReturn(Optional.of(feeType));

        // Act
        FeeTypeDto result = feeTypeService.update(1L, request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Type1", result.getName());
        assertEquals(10.0, result.getPrice());
        verify(feeTypeRepository).save(feeType);
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        UpdateFeePriceRequest request = new UpdateFeePriceRequest(200.0);
        when(feeTypeRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> feeTypeService.update(1L, request), ConstantError.FEE_TYPE_NOT_FOUND + 1L);
    }
}
