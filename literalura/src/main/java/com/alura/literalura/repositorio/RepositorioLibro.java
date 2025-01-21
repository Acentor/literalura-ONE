package com.alura.literalura.repositorio;

import com.alura.literalura.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RepositorioLibro extends JpaRepository<Libro, Long> {
    List<Libro> findByIdioma(String idioma);
}
