package com.appregistros.servicioweb.service;

import com.appregistros.servicioweb.model.Usuario;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private static final String URL = "https://sqpvqkkdfgtzdgrjrgxs.supabase.co/rest/v1/customers?select=*";
    private static final String API_KEY_ENV = "SUPABASE_API_KEY";

    public List<Usuario> obtenerUsuarios() {
        try {
            String apiKey = System.getenv(API_KEY_ENV);
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("Configure la variable de entorno " + API_KEY_ENV);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .timeout(Duration.ofSeconds(15))
                    .header("apikey", apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Error al obtener clientes: HTTP " + response.statusCode());
            }

            JsonNode datos = new ObjectMapper().readTree(response.body());
            List<Usuario> usuarios = new ArrayList<>();

            for (JsonNode dato : datos) {
                Usuario usuario = new Usuario();
                usuario.setId(dato.path("id").isMissingNode() ? null : dato.path("id").asInt());
                usuario.setNombre(dato.path("first_name").asText().trim() + " "
                        + dato.path("last_name").asText().trim());
                usuario.setUsername(dato.path("email").asText().split("@")[0]);
                usuario.setEmail(dato.path("email").asText());
                usuario.setTelefono(dato.path("phone").asText());
                usuario.setCiudad(dato.path("city").asText());
                usuario.setEmpresa(dato.path("status").asText());
                usuarios.add(usuario);
            }

            return usuarios;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Consulta cancelada", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API de clientes", e);
        }
    }
}

