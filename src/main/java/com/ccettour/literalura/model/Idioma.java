package com.ccettour.literalura.model;

public enum Idioma {
    INGLES("en"),
    ESPANIOL("es"),
    ITALIANO("it"),
    PORTUGUES("pt"),
    FRANCES("fr");

    private String idiomaApi;

    Idioma (String idiomaApi){
        this.idiomaApi = idiomaApi;
    }

    public static Idioma fromSigla(String sigla){
        for (Idioma idioma : Idioma.values()){
            if(idioma.idiomaApi.equalsIgnoreCase(sigla)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("No se encontró el idioma: "+sigla);
    }

}
