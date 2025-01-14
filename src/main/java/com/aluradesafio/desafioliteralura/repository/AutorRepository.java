package com.aluradesafio.desafioliteralura.repository;

import com.aluradesafio.desafioliteralura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query(value = "SELECT * FROM autores WHERE autores.fecha_de_muerte >= :fecha", nativeQuery = true)
    List<Autor> fechaDeNacimientoyMuerte(Integer fecha);

    @Query(value = "SELECT * FROM autores a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :autor, '%'))", nativeQuery = true)
    Optional<Autor> bucarAutorPorNombre(@Param("autor") String autor);

}
