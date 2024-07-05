package com.ccettour.literalura.model;

import jakarta.persistence.*;

import java.util.OptionalDouble;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    @ManyToOne
    private Autor autor;

    private Double cantidadDeDescargas;

    public Libro(LibroData libroData){
        this.titulo = libroData.titulo();
        this.idioma = Idioma.fromSigla(libroData.idioma().get(0));
        this.cantidadDeDescargas = OptionalDouble.of(libroData.cantidadDeDescargas()).orElse(0);
    }

    public Libro() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Double getCantidadDeDescargas() {
        return cantidadDeDescargas;
    }

    public void setCantidadDeDescargas(Double cantidadDeDescargas) {
        this.cantidadDeDescargas = cantidadDeDescargas;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo +
                ", idioma:" + idioma +
                ", autor:" + autor +
                ", descargas:" + cantidadDeDescargas;
    }
}
