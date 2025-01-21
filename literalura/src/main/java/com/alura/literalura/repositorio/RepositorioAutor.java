package com.alura.literalura.repositorio;

import com.alura.literalura.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RepositorioAutor extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE :anio BETWEEN a.anioNacimiento AND a.anioFallecimiento OR " +
           "(a.anioNacimiento <= :anio AND a.anioFallecimiento IS NULL)")
    List<Autor> buscarAutoresVivosPorAnio(Integer anio);
}