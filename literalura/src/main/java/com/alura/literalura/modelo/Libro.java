package com.alura.literalura.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Integer cantidadDescargas;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Integer getCantidadDescargas() { return cantidadDescargas; }
    public void setCantidadDescargas(Integer cantidadDescargas) { this.cantidadDescargas = cantidadDescargas; }
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }
}
