package org.example.controller;

import org.example.estructura.ListaDoblementeEncadenada.Lista;
import org.example.model.ArbolGenealogico;
import org.example.model.Correo;
import org.example.model.Usuario;
import org.example.view.ConsolaView;

import java.util.*;

public class ArbolController {
    private ArbolGenealogico arbolGenealogico;
    private ConsolaView vista;
    private Usuario usuarioActual;

    public ArbolController(ArbolGenealogico arbolGenealogico, ConsolaView vista) {
        this.arbolGenealogico = arbolGenealogico;
        this.vista = vista;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
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

    public List<Usuario> obtenerUsuariosRaiz() {
        return arbolGenealogico.getRaices().toList();
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

    public void agregarFamiliar(Usuario usuario, Usuario familiar) {
        if (familiar == null) {
            mostrarMensaje("El familiar no puede ser nulo.");
            return;
        }

        // Determinar la relación entre los usuarios (puedes expandir según la lógica de tu sistema)
        usuario.agregarHijo(familiar);

        // Procesar notificaciones automáticas según la lógica definida
        procesarNotificacionesAutomaticas(familiar, usuario);
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
        return buscarParentescoRecursivo(usuarioRaiz, nombre, 0, new HashSet<>())
                .orElse("No se encontró parentesco con el usuario especificado.");
    }

    private Optional<String> buscarParentescoRecursivo(Usuario usuario, String nombre, int nivel, Set<Usuario> visitados) {
        if (usuario == null || visitados.contains(usuario)) return Optional.empty();

        // Marca al usuario como visitado
        visitados.add(usuario);

        // Si encontramos el usuario, devolvemos el nivel actual
        if (usuario.getNombreCompleto().equalsIgnoreCase(nombre)) {
            return Optional.of("Nivel " + nivel);
        }

        // Busca recursivamente en los padres
        Optional<String> resultado = buscarParentescoRecursivo(usuario.getPadre(), nombre, nivel + 1, visitados);
        if (resultado.isPresent()) return resultado;

        resultado = buscarParentescoRecursivo(usuario.getMadre(), nombre, nivel + 1, visitados);
        if (resultado.isPresent()) return resultado;

        // Busca recursivamente en los hijos
        for (Usuario hijo : usuario.getHijos()) {
            resultado = buscarParentescoRecursivo(hijo, nombre, nivel + 1, visitados);
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

    public void procesarNotificacionesAutomaticas(Usuario familiar, Usuario solicitante) {
        if (familiar.getFechaDefuncion() != null) {
            // Persona fallecida
            Correo notificacion = new Correo(solicitante,
                    "Confirmación requerida: El registro de " + familiar.getNombreCompleto() +
                            " como fallecido necesita ser validado por 3 familiares directos.");
            solicitante.recibirCorreo(notificacion);
            mostrarMensaje("Se ha enviado una notificación para confirmar el fallecimiento de " + familiar.getNombreCompleto());
        } else if (familiar.getEdad() < 18) {
            // Persona menor de edad
            Correo notificacion = new Correo(solicitante,
                    "Confirmación requerida: El registro de " + familiar.getNombreCompleto() +
                            " como menor de edad necesita ser validado por un segundo progenitor o familiar directo.");
            solicitante.recibirCorreo(notificacion);
            mostrarMensaje("Se ha enviado una notificación para confirmar el registro del menor " + familiar.getNombreCompleto());
        } else {
            // Persona mayor de edad
            Correo notificacion = new Correo(solicitante,
                    "Invitación enviada: " + familiar.getNombreCompleto() +
                            " debe confirmar su registro.");
            solicitante.recibirCorreo(notificacion);
            mostrarMensaje("Se ha enviado una invitación para confirmar los datos de " + familiar.getNombreCompleto());
        }
    }

    public void invitarPersonaMayorDeEdad(Usuario usuario, Usuario invitado) {
        if (invitado.getEdad() < 18) {
            mostrarMensaje("La persona no es mayor de edad.");
            return;
        }

        Correo invitacion = new Correo(usuario, "Invitación enviada a " + invitado.getNombreCompleto() +
                " para confirmar su registro.");
        usuario.recibirCorreo(invitacion);

        mostrarMensaje("Se ha enviado una invitación para confirmar los datos de " + invitado.getNombreCompleto() + ".");
    }

    public void confirmarDatosPersona(Usuario usuario) {
        if (!usuario.isConfirmado()) {
            usuario.setConfirmado(true);
            mostrarMensaje("Los datos de " + usuario.getNombreCompleto() + " han sido confirmados.");
        } else {
            mostrarMensaje("El usuario ya estaba confirmado.");
        }
    }

    public void confirmarRegistroFallecido(Usuario fallecido, Usuario confirmador) {
        if (fallecido.getFechaDefuncion() == null) {
            mostrarMensaje("El usuario no está marcado como fallecido.");
            return;
        }

        fallecido.agregarConfirmador(confirmador);
        mostrarMensaje("Confirmación de " + confirmador.getNombreCompleto() + " registrada.");

        if (fallecido.registroConfirmadoPorFallecimiento()) {
            fallecido.setConfirmado(true);
            mostrarMensaje("El registro del fallecido " + fallecido.getNombreCompleto() + " ha sido confirmado.");
        }
    }

    public void confirmarRegistroMenor(Usuario menor, Usuario confirmador) {
        if (menor.getEdad() >= 18) {
            mostrarMensaje("La persona no es menor de edad.");
            return;
        }

        if (esFamiliarDirectoHastaSegundoGrado(menor, confirmador)) {
            menor.setConfirmado(true);
            mostrarMensaje("El registro del menor " + menor.getNombreCompleto() + " ha sido confirmado por " + confirmador.getNombreCompleto() + ".");
        } else {
            mostrarMensaje("El confirmador no cumple los requisitos.");
        }
    }

    private boolean esFamiliarDirectoHastaSegundoGrado(Usuario menor, Usuario confirmador) {
        return menor.getPadre() == confirmador || menor.getMadre() == confirmador ||
                menor.getHijos().contiene(confirmador) || // abuelos
                menor.getPadre() != null && menor.getPadre().getPadre() == confirmador || // bisabuelos
                menor.getPadre() != null && menor.getPadre().getHijos().contiene(confirmador); // hermanos
    }

    public Lista<Usuario> obtenerPendientesDeConfirmacion(Usuario usuario) {
        Lista<Usuario> pendientes = new Lista<>();
        obtenerPendientesRecursivamente(usuario, pendientes);
        return pendientes;
    }

    private void obtenerPendientesRecursivamente(Usuario usuario, Lista<Usuario> pendientes) {
        if (usuario == null) return;

        if (!usuario.isConfirmado()) {
            pendientes.agregar(usuario);
        }

        for (Usuario hijo : usuario.getHijos()) {
            obtenerPendientesRecursivamente(hijo, pendientes);
        }

        if (usuario.getPadre() != null) {
            obtenerPendientesRecursivamente(usuario.getPadre(), pendientes);
        }
        if (usuario.getMadre() != null) {
            obtenerPendientesRecursivamente(usuario.getMadre(), pendientes);
        }
    }

    public void enviarCorreo(Usuario destinatario, String mensaje) {
        if (destinatario == null) {
            mostrarMensaje("Destinatario no válido.");
            return;
        }
        Correo correo = new Correo(destinatario, mensaje);
        destinatario.recibirCorreo(correo);
        mostrarMensaje("Correo enviado a: " + destinatario.getNombreCompleto());
    }
}
