package com.ccettour.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AutorData(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") String anioNacimiento,
        @JsonAlias("death_year") String anioMuerte
) {
}
