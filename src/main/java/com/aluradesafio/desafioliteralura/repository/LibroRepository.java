package com.aluradesafio.desafioliteralura.repository;

import com.aluradesafio.desafioliteralura.modelos.Idiomas;
import com.aluradesafio.desafioliteralura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository <Libro, Long> {

    Optional<Libro> findByTituloContainsIgnoreCase(String nombreLibro);

    List<Libro> findByIdiomas(Idiomas idioma);

    boolean existsByTitulo(String titulo);

}
