package com.example.pantallaslistados.Objetos;

public class Catedratico {
    private String id;
    private String nombre;
    private String telefono;
    private String fechaNacimiento;
    private String direccion;
    private String foto;
    private String correo;

    public Catedratico() {
    }

    public Catedratico(String nombre, String telefono, String foto, String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.foto = foto;
        this.correo = correo;
    }

    public Catedratico(String id, String nombre, String telefono, String fechaNacimiento, String direccion, String foto, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.foto = foto;
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
