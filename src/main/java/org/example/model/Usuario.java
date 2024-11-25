package org.example.model;

import org.example.estructura.ListaDoblementeEncadenada.Lista;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;

public class Usuario implements Comparable<Usuario> {
    private static int contadorId = 1;
    private Lista<Correo> bandejaDeEntrada = new Lista<>();
    private Lista<Usuario> confirmadores = new Lista<>();

    private int id;
    private String primerNombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private LocalDate fechaDefuncion;
    private boolean confirmado;
    private int cedula;

    private Usuario padre;
    private Usuario madre;
    private Lista<Usuario> hijos;

    public Usuario() {
        this.id = generarId();
        this.confirmado = false;
        this.hijos = new Lista<Usuario>();
    }

    public Usuario(String primerNombre, String segundoNombre, String apellidoPaterno, String apellidoMaterno,
                   LocalDate fechaNacimiento, LocalDate fechaDefuncion, int cedula) {
        this();
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaDefuncion = fechaDefuncion;
        setCedula(cedula);
    }

    private static int generarId() {
        return contadorId++;
    }

    // Métodos para atributos básicos
    public int getId() {
        return id;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDate getFechaDefuncion() {
        return fechaDefuncion;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        if (String.valueOf(cedula).length() == 8) {
            this.cedula = cedula;
        } else {
            throw new IllegalArgumentException("La cédula debe tener exactamente 8 dígitos.");
        }
    }

    public Usuario getPadre() {
        return padre;
    }

    public void setPadre(Usuario padre) {
        this.padre = padre;
        if (padre != null && !padre.hijos.contiene(this)) {
            padre.agregarHijo(this);
        }
    }

    public Usuario getMadre() {
        return madre;
    }

    public void setMadre(Usuario madre) {
        this.madre = madre;
        if (madre != null && !madre.hijos.contiene(this)) {
            madre.agregarHijo(this);
        }
    }

    public Lista<Usuario> getHijos() {
        return hijos;
    }


    public void agregarHijo(Usuario hijo) {
        if (hijo != null && !hijos.contiene(hijo)) {
            hijos.agregar(hijo);
            if (this.equals(hijo.getPadre())) {
                hijo.setPadre(this);
            } else if (this.equals(hijo.getMadre())) {
                hijo.setMadre(this);
            }
        }
    }

    public int getEdad() {
        LocalDate fechaFin = (fechaDefuncion != null) ? fechaDefuncion : LocalDate.now();
        return Period.between(fechaNacimiento, fechaFin).getYears();
    }

    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder(primerNombre);
        if (segundoNombre != null && !segundoNombre.isEmpty()) {
            nombreCompleto.append(" ").append(segundoNombre);
        }
        nombreCompleto.append(" ").append(apellidoPaterno).append(" ").append(apellidoMaterno);
        return nombreCompleto.toString();
    }

    public Lista<Correo> getBandejaDeEntrada() {
        return bandejaDeEntrada;
    }

    public void recibirCorreo(Correo correo) {
        bandejaDeEntrada.agregar(correo);
    }

    public void agregarConfirmador(Usuario confirmador) {
        if (!confirmadores.contiene(confirmador)) {
            confirmadores.agregar(confirmador);
        }
    }

    public boolean registroConfirmadoPorFallecimiento() {
        return confirmadores.contador() >= 3;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (ID: " + id + ")";
    }

    @Override
    public int compareTo(Usuario o) {
        return 0;
    }
}