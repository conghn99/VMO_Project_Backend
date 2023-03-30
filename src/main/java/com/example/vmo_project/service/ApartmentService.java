package com.example.vmo_project.service;

import com.example.vmo_project.CONST.ConstantError;
import com.example.vmo_project.dto.ApartmentDto;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Person;
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

        // set căn hộ của tất cả các person bên trong về null
        for (Person person : personRepository.findAll()) {
            if (person.getApartment() != null) {
                if (person.getApartment().getId().equals(id)) {
                    person.setApartment(null);
                    personRepository.save(person);
                }
            }
        }

        // set căn hộ của các person có trong request
        for (Long personId : request.getPersonId()) {
            Person person = personRepository.findById(personId).orElseThrow(() -> {
                throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + personId);
            });
            person.setApartment(apartment);
            personRepository.save(person);
        }
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
