package com.mds.java.sample.code.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private String name;
    private int year;
    private String language;
    private String country;
    private Set<String> cast;

}
