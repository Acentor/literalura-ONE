package com.alura.literalura;

import com.alura.literalura.modelo.Autor;
import com.alura.literalura.modelo.Libro;
import com.alura.literalura.repositorio.RepositorioAutor;
import com.alura.literalura.repositorio.RepositorioLibro;
import com.alura.literalura.servicio.ServicioGutendex;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
    @Autowired
    private RepositorioLibro repositorioLibro;
    
    @Autowired
    private RepositorioAutor repositorioAutor;
    
    @Autowired
    private ServicioGutendex servicioGutendex;

     @Autowired
    private Environment env;

    @PostConstruct
    public void checkDatabase() {
        try {
            String url = env.getProperty("spring.datasource.url");
            String username = env.getProperty("spring.datasource.username");
            String password = env.getProperty("spring.datasource.password");
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                System.out.println("Conexión a la base de datos establecida correctamente.");}
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            System.err.println("Verifique que PostgreSQL esté corriendo y las credenciales sean correctas.");
            System.exit(1);}}

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);}

    @Override
    public void run(String... args) {
        Scanner escaner = new Scanner(System.in);
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== Literalura - Catálogo de Libros ===");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar todos los libros");
            System.out.println("3. Listar libros por idioma");
            System.out.println("4. Listar todos los autores");
            System.out.println("5. Buscar autores vivos en un año específico");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = escaner.nextInt();
            escaner.nextLine(); // Consumir el salto de línea
            switch (opcion) {
                case 1:
                    buscarLibro(escaner);
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarLibrosPorIdioma(escaner);
                    break;
                case 4:
                    listarAutores();
                    break;
                case 5:
                    buscarAutoresVivos(escaner);
                    break;
                case 6:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida");}}}

    private void buscarLibro(Scanner escaner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = escaner.nextLine();
        Optional<Libro> libroOpt = servicioGutendex.buscarLibroPorTitulo(titulo);
        if (libroOpt.isPresent()) {
            Libro libro = libroOpt.get();
            repositorioLibro.save(libro);
            System.out.println("Libro guardado: " + libro.getTitulo());
        } else {
            System.out.println("No se encontró el libro");}}

    private void listarLibros() {
        List<Libro> libros = repositorioLibro.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros en la base de datos");
            return;}
        
        for (Libro libro : libros) {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Descargas: " + libro.getCantidadDescargas());}}

    private void listarLibrosPorIdioma(Scanner escaner) {
        System.out.println("Idiomas disponibles: en (inglés), es (español)");
        System.out.print("Ingrese el código del idioma: ");
        String idioma = escaner.nextLine().toLowerCase();
        List<Libro> libros = repositorioLibro.findByIdioma(idioma);
        System.out.println("Libros en " + idioma + ": " + libros.size());
        for (Libro libro : libros) {
            System.out.println("- " + libro.getTitulo());}}

    private void listarAutores() {
        List<Autor> autores = repositorioAutor.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores en la base de datos");
            return;}
        for (Autor autor : autores) {
            System.out.println("\nNombre: " + autor.getNombre());
            System.out.println("Año de nacimiento: " + 
                (autor.getAnioNacimiento() != null ? autor.getAnioNacimiento() : "Desconocido"));
            System.out.println("Año de fallecimiento: " + 
                (autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento() : "Desconocido"));}}

    private void buscarAutoresVivos(Scanner escaner) {
        System.out.print("Ingrese el año para buscar autores vivos: ");
        int anio = escaner.nextInt();
        List<Autor> autores = repositorioAutor.buscarAutoresVivosPorAnio(anio);
        System.out.println("Autores vivos en " + anio + ":");
        
        for (Autor autor : autores) {
            System.out.println("- " + autor.getNombre() + 
                " (" + autor.getAnioNacimiento() + " - " + 
                (autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento() : "presente") + ")");}}}