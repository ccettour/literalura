package com.ccettour.literalura.repository;

import com.ccettour.literalura.model.Autor;
import com.ccettour.literalura.model.Idioma;
import com.ccettour.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LiteraluraRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreContainsIgnoreCase(String nombreAutor);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.titulo ILIKE %:tituloLibro%")
    Optional<Libro> libroPorTitulo(String tituloLibro);

    @Query("SELECT l FROM Autor a JOIN a.libros l")
    List<Libro> listarLibros();

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento <= :anio AND (a.anioMuerte >= :anio OR a.anioMuerte IS NULL AND a.anioNacimiento IS NOT NULL)")
    List<Autor> listarAutoresVivosEnAnio(int anio);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.idioma = :idioma")
    List<Libro> librosPorIdioma(Idioma idioma);

    @Query("SELECT COUNT(l) FROM Autor a JOIN a.libros l WHERE l.idioma = :idioma")
    Long cantidadLibrosPorIdioma(Idioma idioma);
}
