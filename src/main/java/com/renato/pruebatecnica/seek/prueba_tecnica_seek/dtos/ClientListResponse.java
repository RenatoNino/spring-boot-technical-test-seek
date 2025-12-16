package com.renato.pruebatecnica.seek.prueba_tecnica_seek.dtos;

import java.time.LocalDate;

public class ClientListResponse {
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private LocalDate estimatedDeathDate;

    public ClientListResponse(Long id, String name, String surname, Integer age, LocalDate estimatedDeathDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.estimatedDeathDate = estimatedDeathDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getEstimatedDeathDate() {
        return estimatedDeathDate;
    }

    public void setEstimatedDeathDate(LocalDate estimatedDeathDate) {
        this.estimatedDeathDate = estimatedDeathDate;
    }

}
