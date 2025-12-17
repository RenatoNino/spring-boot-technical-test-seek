package com.renato.pruebatecnica.seek.prueba_tecnica_seek.validations;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientCreateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos.ClientUpdateRequest;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.Client;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.exceptions.BusinessException;

@Component
public class ClientValidation {

    public void validateCreateClientBody(ClientCreateRequest request) {
        validateAge(request.getBirthDate(), request.getAge());
    }

    public void validateUpdateClientBody(Client existing, ClientUpdateRequest request) {
        LocalDate birthDate = request.getBirthDate() != null ? request.getBirthDate() : existing.getBirthDate();
        Integer age = request.getAge() != null ? request.getAge() : existing.getAge();

        if (birthDate != null && age != null) {
            validateAge(birthDate, age);
        }
    }

    public void validateAge(LocalDate birthDate, Integer age) {
        if (birthDate == null || age == null) {
            return;
        }
        int years = Period.between(birthDate, LocalDate.now()).getYears();
        if (years != age) {
            throw new BusinessException("Age does not match birth date");
        }
    }
}
