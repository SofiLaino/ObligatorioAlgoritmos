package org.example.controller;

import org.example.estructura.ListaDoblementeEncadenada.Lista;
import org.example.model.ArbolGenealogico;
import org.example.model.Usuario;
import org.example.view.ConsolaView;

import java.util.Comparator;
import java.util.Optional;
import java.util.List;

public class ArbolController {
    private ArbolGenealogico arbolGenealogico;
    private ConsolaView vista;

    public ArbolController(ArbolGenealogico arbolGenealogico, ConsolaView vista) {
        this.arbolGenealogico = arbolGenealogico;
        this.vista = vista;
    }

    public void setVista(ConsolaView vista) {
        this.vista = vista;
    }

    public void agregarUsuarioRaiz(Usuario usuario) {
        if (usuario == null) {
            mostrarMensaje("El usuario no puede ser nulo.");
            return;
        }
        arbolGenealogico.agregarRaiz(usuario);
        mostrarMensaje("Usuario raíz agregado exitosamente.");
    }

    // Cambié el tipo de retorno a List<Usuario>
    public List<Usuario> obtenerUsuariosRaiz() {
        return arbolGenealogico.getRaices().toList(); // Convertimos a List<Usuario>
    }

    public void agregarPadre(int usuarioId, Usuario padre) {
        Usuario usuario = arbolGenealogico.buscarUsuarioPorId(usuarioId);
        if (usuario == null) {
            mostrarMensaje("Usuario no encontrado.");
            return;
        }

        if (usuario.getPadre() != null) {
            mostrarMensaje("Este usuario ya tiene un padre registrado.");
            return;
        }

        usuario.setPadre(padre);
        padre.agregarHijo(usuario);
        mostrarMensaje("Padre agregado exitosamente.");
    }

    public void agregarMadre(int usuarioId, Usuario madre) {
        Usuario usuario = arbolGenealogico.buscarUsuarioPorId(usuarioId);
        if (usuario == null) {
            mostrarMensaje("Usuario no encontrado.");
            return;
        }

        if (usuario.getMadre() != null) {
            mostrarMensaje("Este usuario ya tiene una madre registrada.");
            return;
        }

        usuario.setMadre(madre);
        madre.agregarHijo(usuario);
        mostrarMensaje("Madre agregada exitosamente.");
    }

    public void agregarHijo(int usuarioId, Usuario hijo) {
        Usuario usuario = arbolGenealogico.buscarUsuarioPorId(usuarioId);
        if (usuario == null) {
            mostrarMensaje("Usuario no encontrado.");
            return;
        }

        if (!usuario.getHijos().contiene(hijo)) {
            usuario.agregarHijo(hijo);

            if (hijo.getPadre() == null) {
                hijo.setPadre(usuario);
            } else if (hijo.getMadre() == null) {
                hijo.setMadre(usuario);
            }

            mostrarMensaje("Hijo agregado exitosamente.");
        } else {
            mostrarMensaje("Este hijo ya está registrado.");
        }
    }

    // Cambié el tipo de retorno a List<Usuario>
    public List<Usuario> obtenerFamiliares(Usuario usuario) {
        List<Usuario> familiares = new java.util.ArrayList<>();
        if (usuario.getPadre() != null) familiares.add(usuario.getPadre());
        if (usuario.getMadre() != null) familiares.add(usuario.getMadre());
        familiares.addAll(usuario.getHijos().toList());
        return familiares;
    }

    public List<Usuario> obtenerFamiliaresPendientes(Usuario usuarioRaiz) {
        if (usuarioRaiz == null) return new java.util.ArrayList<>();
        return obtenerUsuariosPendientes(usuarioRaiz, new java.util.ArrayList<>());
    }

    public List<Usuario> obtenerFamiliaresPorEdad(Usuario usuarioRaiz, int generacion) {
        if (usuarioRaiz == null) return new java.util.ArrayList<>();

        // Obtener la lista de familiares hasta la generación deseada
        List<Usuario> familiares = obtenerFamiliaresHastaGeneracion(usuarioRaiz, generacion, 0);

        // Ordenar la lista de familiares por fecha de nacimiento
        familiares.sort(Comparator.comparing(Usuario::getFechaNacimiento));

        return familiares;
    }

    public String buscarParentescoPorNombre(Usuario usuarioRaiz, String nombre) {
        if (usuarioRaiz == null || nombre == null || nombre.isBlank()) {
            return "Datos inválidos para la búsqueda.";
        }
        return buscarParentescoRecursivo(usuarioRaiz, nombre, 0)
                .orElse("No se encontró parentesco con el usuario especificado.");
    }

    private Optional<String> buscarParentescoRecursivo(Usuario usuario, String nombre, int nivel) {
        if (usuario == null) return Optional.empty();

        if (usuario.getNombreCompleto().equalsIgnoreCase(nombre)) {
            return Optional.of("Nivel " + nivel);
        }

        Optional<String> resultado = buscarParentescoRecursivo(usuario.getPadre(), nombre, nivel + 1);
        if (resultado.isPresent()) return resultado;

        resultado = buscarParentescoRecursivo(usuario.getMadre(), nombre, nivel + 1);
        if (resultado.isPresent()) return resultado;

        for (Usuario hijo : usuario.getHijos()) {
            resultado = buscarParentescoRecursivo(hijo, nombre, nivel + 1);
            if (resultado.isPresent()) return resultado;
        }

        return Optional.empty();
    }

    private List<Usuario> obtenerUsuariosPendientes(Usuario usuario, List<Usuario> pendientes) {
        if (usuario == null) return pendientes;

        if (!usuario.isConfirmado()) {
            pendientes.add(usuario);
        }

        if (usuario.getPadre() != null) {
            obtenerUsuariosPendientes(usuario.getPadre(), pendientes);
        }
        if (usuario.getMadre() != null) {
            obtenerUsuariosPendientes(usuario.getMadre(), pendientes);
        }
        for (Usuario hijo : usuario.getHijos()) {
            obtenerUsuariosPendientes(hijo, pendientes);
        }

        return pendientes;
    }

    private List<Usuario> obtenerFamiliaresHastaGeneracion(Usuario usuario, int generacion, int nivelActual) {
        List<Usuario> familiares = new java.util.ArrayList<>();
        if (usuario == null || nivelActual > generacion) return familiares;

        if (nivelActual == generacion) {
            familiares.add(usuario);
        } else {
            if (usuario.getPadre() != null) {
                familiares.addAll(obtenerFamiliaresHastaGeneracion(usuario.getPadre(), generacion, nivelActual + 1));
            }
            if (usuario.getMadre() != null) {
                familiares.addAll(obtenerFamiliaresHastaGeneracion(usuario.getMadre(), generacion, nivelActual + 1));
            }
            for (Usuario hijo : usuario.getHijos()) {
                familiares.addAll(obtenerFamiliaresHastaGeneracion(hijo, generacion, nivelActual + 1));
            }
        }
        return familiares;
    }

    // Cambié el tipo de retorno a List<Usuario>
    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> usuarios = new java.util.ArrayList<>();
        for (Usuario raiz : arbolGenealogico.getRaices()) {
            obtenerUsuariosRecursivamente(raiz, usuarios);
        }
        return usuarios;
    }

    private void obtenerUsuariosRecursivamente(Usuario usuario, List<Usuario> usuarios) {
        if (usuario == null || usuarios.contains(usuario)) return;

        usuarios.add(usuario);

        if (usuario.getPadre() != null) {
            obtenerUsuariosRecursivamente(usuario.getPadre(), usuarios);
        }
        if (usuario.getMadre() != null) {
            obtenerUsuariosRecursivamente(usuario.getMadre(), usuarios);
        }
        for (Usuario hijo : usuario.getHijos()) {
            obtenerUsuariosRecursivamente(hijo, usuarios);
        }
    }

    private void mostrarMensaje(String mensaje) {
        if (vista != null) {
            vista.mostrarMensaje(mensaje);
        }
    }
}
