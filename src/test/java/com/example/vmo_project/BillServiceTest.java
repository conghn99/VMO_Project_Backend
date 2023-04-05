package com.example.vmo_project;

import com.example.vmo_project.dto.BillDto;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.repository.BillRepository;
import com.example.vmo_project.service.BillService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {
    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    @Test
    public void testCalcUnpaidBill() {
        // Arrange
        Bill bill1 = new Bill(1L, 100D, 50D, LocalDate.of(2023, 2, 1), null, false, null, new ArrayList<>());
        Bill bill2 = new Bill(2L, 200D, 150D, LocalDate.of(2023, 3, 1), null, false, null, new ArrayList<>());
        Bill bill3 = new Bill(3L, 300D, 250D, LocalDate.of(2023, 4, 1), null, false, null, new ArrayList<>());
        List<Bill> bills = Arrays.asList(bill1, bill2, bill3);

        when(billRepository.findAll())
                .thenReturn(bills);

        // Act
        List<BillDto> billDtos = billService.getAll();

        // Assert
        assertEquals(3, billDtos.size());
        assertEquals(1L, billDtos.get(0).getId());
        assertEquals(2L, billDtos.get(1).getId());
        assertEquals(100D, billDtos.get(0).getElectricityNumber());
        assertEquals(50D, billDtos.get(0).getWaterNumber());
        assertEquals(LocalDate.of(2023, 4, 1), billDtos.get(2).getBillDate());
        assertFalse(billDtos.get(1).isStatus());
    }
}
