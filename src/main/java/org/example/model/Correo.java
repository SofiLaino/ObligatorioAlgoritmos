package org.example.model;

import org.example.estructura.ListaDoblementeEncadenada.Lista;

public class Correo implements Comparable<Correo> {
    private static int contadorId = 1;
    private int id;
    private Usuario destinatario;
    private String mensaje;
    private boolean leido;

    public Correo(Usuario destinatario, String mensaje) {
        this.id = contadorId++;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.leido = false;
    }

    public int getId() {
        return id;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isLeido() {
        return leido;
    }

    public void marcarComoLeido() {
        this.leido = true;
    }

    @Override
    public String toString() {
        return (leido ? "[LEÍDO] " : "[NO LEÍDO] ") + "Mensaje ID: " + id + " - " + mensaje;
    }

    @Override
    public int compareTo(Correo o) {
        return 0;
    }
}