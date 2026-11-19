package com.appregistros.servicioweb.controller;

import com.appregistros.servicioweb.model.Usuario;
import com.appregistros.servicioweb.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class CustomersController {
    @FXML
    private Button btnConsultar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Usuario> tablaClientes;

    private final UsuarioService usuarioService = new UsuarioService();
    private ObservableList<Usuario> clientes = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        FilteredList<Usuario> filtrados = new FilteredList<>(clientes, cliente -> true);
        txtBuscar.textProperty().addListener((observable, anterior, texto) -> {
            String filtro = texto == null ? "" : texto.toLowerCase().trim();
            filtrados.setPredicate(cliente -> filtro.isEmpty()
                    || cliente.getNombre().toLowerCase().contains(filtro)
                    || cliente.getCorreo().toLowerCase().contains(filtro)
                    || cliente.getCiudad().toLowerCase().contains(filtro));
            lblEstado.setText(filtrados.size() + " cliente(s) mostrado(s)");
        });
        tablaClientes.setItems(filtrados);
    }

    @FXML
    private void onConsultar() {
        btnConsultar.setDisable(true);
        lblEstado.setText("Consultando clientes...");

        Task<List<Usuario>> consulta = new Task<>() {
            @Override
            protected List<Usuario> call() {
                return usuarioService.obtenerUsuarios();
            }
        };

        consulta.setOnSucceeded(event -> {
            clientes.setAll(consulta.getValue());
            lblEstado.setText(clientes.size() + " cliente(s) encontrado(s)");
            btnConsultar.setDisable(false);
        });

        consulta.setOnFailed(event -> {
            Throwable error = consulta.getException();
            lblEstado.setText(error.getMessage());
            btnConsultar.setDisable(false);
        });

        Thread hilo = new Thread(consulta, "consulta-clientes-nueva-vista");
        hilo.setDaemon(true);
        hilo.start();
    }

    @FXML
    private void onLimpiar() {
        txtBuscar.clear();
        lblEstado.setText(clientes.size() + " cliente(s) encontrado(s)");
    }
}
