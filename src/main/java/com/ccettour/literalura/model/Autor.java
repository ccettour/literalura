package com.ccettour.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer anioNacimiento;
    private Integer anioMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor(AutorData autorData) {
        this.nombre = autorData.nombre();
        if(autorData.anioNacimiento()!=null){
            this.anioNacimiento = Integer.valueOf(autorData.anioNacimiento());
        } else {
            this.anioNacimiento = null;
        }
        if(autorData.anioMuerte()!=null){
            this.anioMuerte = Integer.valueOf(autorData.anioMuerte());
        } else {
            this.anioMuerte = null;
        }
    }

    public Autor() {}

    public Autor(String nombre, Integer anioNacimiento, Integer anioMuerte) {
        this.nombre = nombre;
        this.anioNacimiento = anioNacimiento;
        this.anioMuerte = anioMuerte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioMuerte() {
        return anioMuerte;
    }

    public void setAnioMuerte(Integer anioMuerte) {
        this.anioMuerte = anioMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }

    public void setLibros(Libro libro) {
        libro.setAutor(this);
        this.libros.add(libro);
    }

    @Override
    public String toString() {
        String autor;
        if((anioNacimiento == null) & (anioMuerte == null)){
            autor=nombre;
        } else {
            autor= nombre + " (" + anioNacimiento + "-" + anioMuerte + ")";
        }
        return autor;
    }
}
