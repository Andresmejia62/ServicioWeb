package com.appregistros.servicioweb.model;

public class Usuario {
    private String nombre;
    private String username;
    private String email;
    private String telefono;
    private String empresa;
    private String ciudad;


    public Usuario() {
    }

    public Usuario(String nombre, String username, String email, String telefono, String empresa, String ciudad) {
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.telefono = telefono;
        this.empresa = empresa;
        this.ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
