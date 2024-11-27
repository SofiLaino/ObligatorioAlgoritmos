package org.example.model;

import org.example.estructura.ListaDoblementeEncadenada.Lista;

public class Correo implements Comparable<Correo> {
    private static int contadorId = 1;
    private int id;
    private Usuario destinatario;
    private String asunto;
    private String mensaje;
    private boolean leido;

    // Constructor para inicializar los atributos
    public Correo(Usuario destinatario, String asunto, String mensaje) {
        this.id = contadorId++;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.leido = false;
    }

    // Métodos de acceso (getters) y modificación (setters)
    public int getId() {
        return id; // Retornar el ID único del correo
    }

    public Usuario getDestinatario() {
        return destinatario; // Retornar el destinatario del correo
    }

    public String getAsunto() {
        return asunto; // Retornar el asunto del correo
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto; // Modificar el asunto del correo
    }

    public String getMensaje() {
        return mensaje; // Retornar el contenido del mensaje
    }

    public boolean isLeido() {
        return leido; // Retornar el estado de lectura del correo
    }

    public void marcarComoLeido() {
        this.leido = true; // Cambiar el estado a "leído"
    }

    // Representación del correo como cadena
    @Override
    public String toString() {
        // Mostrar "[LEÍDO]" o "[NO LEÍDO]" dependiendo del estado y el asunto del correo
        return (leido ? "[LEÍDO ✔] " : "[NO LEÍDO 📧] ") + asunto + " (ID: " + id + ")";
    }

    // Comparar correos por su ID
    @Override
    public int compareTo(Correo o) {
        return Integer.compare(this.id, o.id); // Comparar ID de los correos
    }
}