package com.ccettour.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiData(
        @JsonAlias("count") Integer cantidad,
        @JsonAlias("results") List<LibroData> resultado) {
}
