package com.alura.literalura.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseInitializer {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void initialize() {
        try {
            String databaseName = url.substring(url.lastIndexOf("/") + 1);
            String serverUrl = url.substring(0, url.lastIndexOf("/")) + "/postgres";
            try (Connection connection = DriverManager.getConnection(serverUrl, username, password)) {
                try (Statement statement = connection.createStatement()) {
                    String checkDatabaseSql = "SELECT 1 FROM pg_database WHERE datname = '" + databaseName + "'";
                    if (!statement.executeQuery(checkDatabaseSql).next()) {
                        String createDatabaseSql = "CREATE DATABASE " + databaseName;
                        statement.executeUpdate(createDatabaseSql);
                        System.out.println("Base de datos '" + databaseName + "' creada exitosamente.");
                    } else {
                        System.out.println("La base de datos '" + databaseName + "' ya existe.");}}}
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();}}}
