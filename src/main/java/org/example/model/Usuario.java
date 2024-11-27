package org.example.model;

import org.example.estructura.ListaDoblementeEncadenada.Lista;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

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
        this.hijos = new Lista<>();
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

    // Getters y setters para atributos básicos
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

    public Usuario getMadre() {
        return madre;
    }

    public Lista<Usuario> getHijos() {
        return hijos;
    }

    public Lista<Usuario> getConfirmadores() {
        return confirmadores;
    }

    // Relación con el padre
    public void setPadre(Usuario padre) {
        if (this.padre != padre) {
            this.padre = padre;
            if (padre != null && !padre.getHijos().contiene(this)) {
                padre.agregarHijo(this); // Relación bidireccional
            }
        }
    }

    // Relación con la madre
    public void setMadre(Usuario madre) {
        if (this.madre != madre) {
            this.madre = madre;
            if (madre != null && !madre.getHijos().contiene(this)) {
                madre.agregarHijo(this); // Relación bidireccional
            }
        }
    }

    // Agregar hijo a la lista
    public void agregarHijo(Usuario hijo) {
        if (hijo == null) {
            throw new IllegalArgumentException("El hijo no puede ser nulo.");
        }
        if (!hijos.contiene(hijo)) {
            hijos.agregar(hijo);
            if (hijo.getPadre() == null) {
                hijo.setPadre(this);
            }
        }
    }

    // Calcular la edad del usuario
    public int getEdad() {
        LocalDate fechaFin = (fechaDefuncion != null) ? fechaDefuncion : LocalDate.now();
        return Period.between(fechaNacimiento, fechaFin).getYears();
    }

    // Obtener nombre completo
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder(primerNombre);
        if (segundoNombre != null && !segundoNombre.isEmpty()) {
            nombreCompleto.append(" ").append(segundoNombre);
        }
        nombreCompleto.append(" ").append(apellidoPaterno).append(" ").append(apellidoMaterno);
        return nombreCompleto.toString();
    }

    // Métodos relacionados con correos
    public Lista<Correo> getBandejaDeEntrada() {
        return bandejaDeEntrada;
    }

    public void recibirCorreo(Correo correo) {
        if (correo == null) {
            throw new IllegalArgumentException("El correo no puede ser nulo.");
        }
        bandejaDeEntrada.agregar(correo);
    }

    // Agregar confirmador a la lista
    // Agregar confirmador a la lista
    public void agregarConfirmador(Usuario confirmador) {
        if (confirmador == null) {
            throw new IllegalArgumentException("El confirmador no puede ser nulo.");
        }
        // Asegurarse de que la lista está inicializada
        if (confirmadores == null) {
            confirmadores = new Lista<>();
        }
        // Evitar duplicados
        if (!confirmadores.contiene(confirmador)) {
            confirmadores.agregar(confirmador);
            System.out.println("Confirmador agregado: " + confirmador.getNombreCompleto());
        } else {
            System.out.println("El confirmador ya está registrado.");
        }
    }

    // Confirmar registro por fallecimiento
    public boolean registroConfirmadoPorFallecimiento() {
        if (confirmadores == null) {
            return false; // Si no hay confirmadores, no está confirmado
        }
        return confirmadores.size() >= 3;
    }

    // Confirmar registro por menor de edad
    public boolean registroConfirmadoPorMenor() {
        if (confirmadores == null) {
            return false; // Si no hay confirmadores, no está confirmado
        }
        return confirmadores.size() >= 1;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (ID: " + id + ")";
    }

    @Override
    public int compareTo(Usuario o) {
        return Integer.compare(this.id, o.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return cedula == usuario.cedula; // Comparar por cédula
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula); // Usar la cédula para el hash
    }
}