package com.mds.java.sample.code.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Actor {

    private String name;
    private LocalDate dateOfBirth;
    private String countryOfBirth;
    private float remuneration;



}
