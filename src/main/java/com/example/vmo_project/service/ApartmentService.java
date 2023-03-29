package com.example.vmo_project.service;

import com.example.vmo_project.CONST.ConstantError;
import com.example.vmo_project.dto.ApartmentDto;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.BillRepository;
import com.example.vmo_project.repository.PersonRepository;
import com.example.vmo_project.request.InsertApartmentRequest;
import com.example.vmo_project.request.UpdateApartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final PersonRepository personRepository;
    private final BillRepository billRepository;

    // Lấy danh sách tất cả căn hộ
    public List<ApartmentDto> getAll() {
        return apartmentRepository.findAll()
                .stream()
                .map(ApartmentDto::new)
                .toList();
    }

    // Lấy căn hộ theo id
    public ApartmentDto getById(Long id) {
        return new ApartmentDto(apartmentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
        }));
    }

    // Lấy danh sách các căn hộ theo keyword
    public List<ApartmentDto> getByApartmentNumber(String num) {
        return apartmentRepository.findByApartmentNumber(num)
                .stream()
                .map(ApartmentDto::new)
                .toList();
    }

    // Thêm căn hộ
    public ApartmentDto add(InsertApartmentRequest request) {
        Apartment apartment = Apartment.builder()
                .apartmentNumber(request.getApartmentNumber())
                .area(request.getArea())
                .numberOfRooms(request.getNumberOfRooms())
                .status(request.isStatus())
                .bills(new ArrayList<>())
                .persons(new ArrayList<>())
                .build();
        apartmentRepository.save(apartment);
        return new ApartmentDto(apartment);
    }

    // Sửa căn hộ (trạng thái căn hộ và số người)
    public ApartmentDto update(Long id, UpdateApartmentRequest request) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
        });
        apartment.setStatus(request.isStatus());
        apartment.setPersons(personRepository.findByIdIn(request.getPersonId()));
        apartmentRepository.save(apartment);
        return new ApartmentDto(apartment);
    }

    // Xóa căn hộ
    public void delete(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
        });
        apartmentRepository.delete(apartment);
    }
}
