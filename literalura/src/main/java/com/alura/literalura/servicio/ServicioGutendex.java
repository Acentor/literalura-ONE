package com.alura.literalura.servicio;

import com.alura.literalura.modelo.Autor;
import com.alura.literalura.modelo.Libro;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class ServicioGutendex {
    private final String URL_BASE = "https://gutendex.com/books";
    private final RestTemplate clienteRest = new RestTemplate();
    private final ObjectMapper mapeadorObjetos = new ObjectMapper();

    public Optional<Libro> buscarLibroPorTitulo(String titulo) {
        String url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
        
        try {
            String respuesta = clienteRest.getForObject(url, String.class);
            JsonNode raiz = mapeadorObjetos.readTree(respuesta);
            
            if (raiz.get("results").size() > 0) {
                JsonNode primerLibro = raiz.get("results").get(0);
                Libro libro = new Libro();
                libro.setTitulo(primerLibro.get("title").asText());
                libro.setCantidadDescargas(primerLibro.get("download_count").asInt());
                libro.setIdioma(primerLibro.get("languages").get(0).asText());
                if (primerLibro.get("authors").size() > 0) {
                    JsonNode nodoAutor = primerLibro.get("authors").get(0);
                    Autor autor = new Autor();
                    autor.setNombre(nodoAutor.get("name").asText());
                    autor.setAnioNacimiento(nodoAutor.get("birth_year").isNull() ? null : 
                                          nodoAutor.get("birth_year").asInt());
                    autor.setAnioFallecimiento(nodoAutor.get("death_year").isNull() ? null : 
                                             nodoAutor.get("death_year").asInt());
                    libro.setAutor(autor);}
                
                return Optional.of(libro);}
            
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();}}}