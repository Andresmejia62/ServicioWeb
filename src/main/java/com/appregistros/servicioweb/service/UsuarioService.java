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
    private static final String URL = "https://jsonplaceholder.typicode.com/users";

    public List<Usuario> obtenerUsuarios() {
        try {
           HttpClient cliente = HttpClient.newHttpClient();
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create(URL))
                   .timeout(Duration.ofSeconds(15))
                   .GET()
                   .build();

           HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

           if (response.statusCode() != 200) {
               throw new RuntimeException("Error al obtener los usuarios: " + response.statusCode());
           }

           ObjectMapper mapper = new ObjectMapper();
           JsonNode datos = mapper.readTree(response.body());
           List<Usuario> usuarios = new ArrayList<>();

           for (JsonNode dato : datos) {
               Usuario usuario = new Usuario();
               usuario.setId(dato.path("id").isMissingNode() ? null : dato.path("id").asInt());
               usuario.setNombre(dato.path("name").asText());
               usuario.setUsername(dato.path("username").asText());
               usuario.setEmail(dato.path("email").asText());
               usuario.setTelefono(dato.path("phone").asText());
               usuario.setEmpresa(dato.path("company").path("name").asText());
               usuario.setCiudad(dato.path("address").path("city").asText());
               usuarios.add(usuario);
           }

           return usuarios;
        } catch (Exception e) {
           throw new RuntimeException("Error al consumir la API de usuarios", e);
        }
    }
}

