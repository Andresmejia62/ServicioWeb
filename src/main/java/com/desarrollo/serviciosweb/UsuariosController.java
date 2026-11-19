package com.desarrollo.serviciosweb;

import com.appregistros.servicioweb.model.Usuario;
import com.appregistros.servicioweb.service.UsuarioService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class UsuariosController {
    @FXML
    private Button btnConsultar;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, Integer> colId;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colUsername;

    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colTelefono;

    @FXML
    private TableColumn<Usuario, String> colCiudad;

    @FXML
    private TableColumn<Usuario, String> colEmpresa;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void onConsultarUsuarios() {
        btnConsultar.setDisable(true);
        lblEstado.setText("Consultando...");

        Task<List<Usuario>> consulta = new Task<>() {
            @Override
            protected List<Usuario> call() {
                return usuarioService.obtenerUsuarios();
            }
        };

        consulta.setOnSucceeded(event -> {
            List<Usuario> usuarios = consulta.getValue();
            tablaUsuarios.setItems(FXCollections.observableArrayList(usuarios));
            lblEstado.setText("Se consultaron " + usuarios.size() + " clientes.");
            btnConsultar.setDisable(false);
        });

        consulta.setOnFailed(event -> {
            Throwable error = consulta.getException();
            lblEstado.setText(error.getMessage());
            btnConsultar.setDisable(false);
        });

        Thread hiloConsulta = new Thread(consulta, "consulta-clientes");
        hiloConsulta.setDaemon(true);
        hiloConsulta.start();
    }
}
