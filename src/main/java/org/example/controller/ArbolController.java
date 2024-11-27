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

    public Lista<Usuario> obtenerUsuariosRaiz() {
        Lista<Usuario> usuariosRaiz = new Lista<>();
        for (Usuario usuario : arbolGenealogico.getRaices()) {
            usuariosRaiz.agregar(usuario);
        }
        return usuariosRaiz;
    }

    public Lista<Usuario> obtenerFamiliares(Usuario usuario) {
        // Verificar si el usuario no es nulo
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        // Crear una lista para almacenar los familiares
        Lista<Usuario> familiares = new Lista<>();

        // Agregar al padre, si existe
        if (usuario.getPadre() != null) {
            if (!familiares.contiene(usuario.getPadre())) { // Evitar duplicados
                familiares.agregar(usuario.getPadre());
            }
        }

        // Agregar a la madre, si existe
        if (usuario.getMadre() != null) {
            if (!familiares.contiene(usuario.getMadre())) { // Evitar duplicados
                familiares.agregar(usuario.getMadre());
            }
        }

        // Agregar a los hijos, si existen
        for (Usuario hijo : usuario.getHijos()) {
            if (!familiares.contiene(hijo)) { // Evitar duplicados
                familiares.agregar(hijo);
            }
        }

        return familiares; // Retornar la lista de familiares
    }

    public Lista<Usuario> obtenerFamiliaresPendientes(Usuario usuarioRaiz) {
        // Validar si el usuario raíz es nulo
        if (usuarioRaiz == null) {
            mostrarMensaje("El usuario raíz no puede ser nulo.");
            return new Lista<>();
        }

        // Retornar la lista de usuarios pendientes de confirmación
        return obtenerUsuariosPendientes(usuarioRaiz, new Lista<Usuario>());
    }

    public Lista<Usuario> obtenerFamiliaresPorEdad(Usuario usuarioRaiz, int generacion) {
        if (usuarioRaiz == null) return new Lista<>();

        // Obtener la lista de familiares hasta la generación deseada
        Lista<Usuario> familiares = (Lista<Usuario>) obtenerFamiliaresHastaGeneracion(usuarioRaiz, generacion, 0);

        // Ordenar la lista de familiares por fecha de nacimiento (usando MergeSort de tu implementación)
        familiares.ordenarListaPorFechaNacimiento(familiares);

        return familiares;
    }

    private Lista<Usuario> obtenerUsuariosPendientes(Usuario usuario, Lista<Usuario> pendientes) {
        if (usuario == null) return pendientes;

        if (!usuario.isConfirmado()) {
            pendientes.agregar(usuario);
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

    private Lista<Usuario> obtenerFamiliaresHastaGeneracion(Usuario usuario, int generacion, int nivelActual) {
        Lista<Usuario> familiares = new Lista<>();
        if (usuario == null || nivelActual > generacion) return familiares;

        if (nivelActual == generacion) {
            familiares.agregar(usuario);
        } else {
            if (usuario.getPadre() != null) {
                Lista<Usuario> padres = obtenerFamiliaresHastaGeneracion(usuario.getPadre(), generacion, nivelActual + 1);
                for (Usuario padre : padres) {
                    familiares.agregar(padre);
                }
            }
            if (usuario.getMadre() != null) {
                Lista<Usuario> madres = obtenerFamiliaresHastaGeneracion(usuario.getMadre(), generacion, nivelActual + 1);
                for (Usuario madre : madres) {
                    familiares.agregar(madre);
                }
            }
            for (Usuario hijo : usuario.getHijos()) {
                Lista<Usuario> hijos = obtenerFamiliaresHastaGeneracion(hijo, generacion, nivelActual + 1);
                for (Usuario h : hijos) {
                    familiares.agregar(h);
                }
            }
        }
        return familiares;
    }

    public Lista<Usuario> obtenerTodosUsuarios() {
        Lista<Usuario> usuarios = new Lista<>();
        for (Usuario raiz : arbolGenealogico.getRaices()) {
            obtenerUsuariosRecursivamente(raiz, usuarios);
        }
        return usuarios;
    }

    public Lista<Usuario> obtenerFamiliaresHastaSegundoGrado(Usuario usuario) {
        Lista<Usuario> familiares = new Lista<>();

        if (usuario == null) {
            return familiares;
        }

        // Agregar padres
        if (usuario.getPadre() != null) {
            familiares.agregar(usuario.getPadre());

            // Agregar abuelos paternos
            if (usuario.getPadre().getPadre() != null) {
                familiares.agregar(usuario.getPadre().getPadre());
            }
            if (usuario.getPadre().getMadre() != null) {
                familiares.agregar(usuario.getPadre().getMadre());
            }
        }

        if (usuario.getMadre() != null) {
            familiares.agregar(usuario.getMadre());

            // Agregar abuelos maternos
            if (usuario.getMadre().getPadre() != null) {
                familiares.agregar(usuario.getMadre().getPadre());
            }
            if (usuario.getMadre().getMadre() != null) {
                familiares.agregar(usuario.getMadre().getMadre());
            }
        }

        // Agregar hermanos (evitar duplicados)
        if (usuario.getPadre() != null) {
            for (Usuario hermano : usuario.getPadre().getHijos()) {
                if (!hermano.equals(usuario) && !familiares.contiene(hermano)) {
                    familiares.agregar(hermano);
                }
            }
        }
        if (usuario.getMadre() != null) {
            for (Usuario hermano : usuario.getMadre().getHijos()) {
                if (!hermano.equals(usuario) && !familiares.contiene(hermano)) {
                    familiares.agregar(hermano);
                }
            }
        }

        // Agregar hijos
        for (Usuario hijo : usuario.getHijos()) {
            if (!familiares.contiene(hijo)) {
                familiares.agregar(hijo);
            }
        }

        return familiares;
    }

    public Lista<Usuario> obtenerFamiliaresDirectos(Usuario usuario) {
        Lista<Usuario> familiaresDirectos = new Lista<>();

        if (usuario == null) {
            return familiaresDirectos; // Retornar lista vacía si el usuario es null
        }

        // Agregar padres
        if (usuario.getPadre() != null) {
            familiaresDirectos.agregar(usuario.getPadre());
        }
        if (usuario.getMadre() != null) {
            familiaresDirectos.agregar(usuario.getMadre());
        }

        // Agregar hijos
        for (Usuario hijo : usuario.getHijos()) {
            familiaresDirectos.agregar(hijo);
        }

        // Agregar hermanos (hijos de los padres, excluyendo al usuario mismo)
        if (usuario.getPadre() != null) {
            for (Usuario hermano : usuario.getPadre().getHijos()) {
                if (!hermano.equals(usuario)) {
                    familiaresDirectos.agregar(hermano);
                }
            }
        }
        if (usuario.getMadre() != null) {
            for (Usuario hermano : usuario.getMadre().getHijos()) {
                if (!hermano.equals(usuario) && !familiaresDirectos.contiene(hermano)) {
                    familiaresDirectos.agregar(hermano); // Evitar duplicados
                }
            }
        }

        return familiaresDirectos;
    }

    public Lista<Usuario> obtenerPendientesDeConfirmacion(Usuario usuario) {
        Lista<Usuario> pendientes = new Lista<>();
        obtenerPendientesRecursivamente(usuario, pendientes);
        return pendientes;
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

    public String buscarParentescoPorNombre(Usuario usuarioRaiz, String nombre) {
        if (usuarioRaiz == null || nombre == null || nombre.isBlank()) {
            return "Datos inválidos para la búsqueda.";
        }
        return buscarParentescoRecursivo(usuarioRaiz, nombre, 0, new HashSet<>())
                .orElse("No se encontró parentesco con el usuario especificado.");
    }

    public void agregarUsuarioRaiz(Usuario usuario) {
        if (usuario == null) {
            mostrarMensaje("El usuario no puede ser nulo.");
            return;
        }
        arbolGenealogico.agregarRaiz(usuario);
        mostrarMensaje("Usuario raíz agregado exitosamente.");
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

        // Establecer relación bidireccional
        usuario.setPadre(padre);
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

        // Establecer relación bidireccional
        usuario.setMadre(madre);
        mostrarMensaje("Madre agregada exitosamente.");
    }

    public void agregarHijo(int usuarioId, Usuario hijo) {
        Usuario usuario = arbolGenealogico.buscarUsuarioPorId(usuarioId);
        if (usuario == null) {
            mostrarMensaje("Usuario no encontrado.");
            return;
        }

        if (usuario.getHijos().contiene(hijo)) {
            mostrarMensaje("Este hijo/a ya está registrado.");
            return;
        }

        // Establecer relación bidireccional
        usuario.agregarHijo(hijo);
        mostrarMensaje("Hijo/a agregado exitosamente.");
    }

    public void agregarFamiliar(Usuario usuario, Usuario familiar, String tipoParentesco) {
        if (usuario == null) {
            mostrarMensaje("El usuario no puede ser nulo.");
            return;
        }

        if (familiar == null) {
            mostrarMensaje("El familiar no puede ser nulo.");
            return;
        }

        switch (tipoParentesco.toLowerCase()) {
            case "padre":
                if (usuario.getPadre() != null) {
                    mostrarMensaje("El usuario ya tiene un padre registrado.");
                    return;
                }
                usuario.setPadre(familiar); // Relación bidireccional manejada dentro de setPadre
                break;

            case "madre":
                if (usuario.getMadre() != null) {
                    mostrarMensaje("El usuario ya tiene una madre registrada.");
                    return;
                }
                usuario.setMadre(familiar); // Relación bidireccional manejada dentro de setMadre
                break;

            case "hijo":
                if (usuario.getHijos().contiene(familiar)) {
                    mostrarMensaje("Este hijo/a ya está registrado.");
                    return;
                }
                usuario.agregarHijo(familiar); // Relación bidireccional manejada dentro de agregarHijo
                break;

            default:
                mostrarMensaje("Tipo de parentesco no válido. Por favor, elija 'Padre', 'Madre' o 'Hijo/a'.");
                return;
        }

        // Procesar notificaciones automáticas después de agregar el familiar
        procesarNotificacionesAutomaticas(familiar, usuario);

        mostrarMensaje(tipoParentesco + " agregado exitosamente y notificación enviada.");
    }

    private void obtenerUsuariosRecursivamente(Usuario usuario, Lista<Usuario> usuarios) {
        if (usuario == null || usuarios.contiene(usuario)) return;

        usuarios.agregar(usuario);

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

    private void procesarNotificacionesAutomaticas(Usuario familiar, Usuario solicitante) {
        if (familiar.getFechaDefuncion() != null) {
            notificarFallecimiento(familiar); // Notificar fallecimiento
        } else if (familiar.getEdad() < 18) {
            notificarRegistroMenor(familiar); // Notificar menor de edad
        } else {
            invitarPersonaMayorDeEdad(solicitante, familiar); // Invitar mayor de edad
        }
    }

    private void notificarRegistroMenor(Usuario menor) {
        Lista<Usuario> validadores = obtenerFamiliaresHastaSegundoGrado(menor);
        for (Usuario validador : validadores) {
            String asunto = "Confirmación de registro de menor de edad";
            String mensaje = "Se requiere tu confirmación para registrar al menor " + menor.getNombreCompleto() + " (Id: " + menor.getId() +")";
            enviarCorreo(validador, asunto, mensaje);
        }
        mostrarMensaje("Se han enviado notificaciones para confirmar el registro del menor " + menor.getNombreCompleto());
    }

    private void notificarFallecimiento(Usuario fallecido) {
        Lista<Usuario> validadores = obtenerFamiliaresDirectos(fallecido);
        for (Usuario validador : validadores) {
            String asunto = "Confirmación de fallecimiento";
            String mensaje = "Se requiere tu confirmación para registrar como fallecido a " + fallecido.getNombreCompleto() + " (Id: " + fallecido.getId() +")";
            enviarCorreo(validador, asunto, mensaje);
        }
        mostrarMensaje("Se han enviado notificaciones para confirmar el fallecimiento de " + fallecido.getNombreCompleto());
    }

    public void invitarPersonaMayorDeEdad(Usuario usuario, Usuario invitado) {
        if (invitado.getEdad() < 18) {
            mostrarMensaje("La persona no es mayor de edad.");
            return;
        }

        String asunto = "Invitación para confirmar registro";
        String mensaje = "Por favor, confirma tu registro en el sistema." + " (Id: " + invitado.getId() +")";

        enviarCorreo(invitado, asunto, mensaje);
        mostrarMensaje("Se ha enviado una invitación a " + invitado.getNombreCompleto() + ".");
    }

    public boolean confirmarDatosPersona(Usuario usuario) {
        System.out.println("Iniciando confirmación de datos de persona...");

        if (usuario == null) {
            System.out.println("Usuario es null.");
            mostrarMensaje("Usuario inválido.");
            return false;
        }

        if (!usuario.isConfirmado()) {
            usuario.setConfirmado(true);
            System.out.println("Datos de " + usuario.getNombreCompleto() + " confirmados.");
            mostrarMensaje("Los datos de " + usuario.getNombreCompleto() + " han sido confirmados.");
            return true;
        } else {
            System.out.println("El usuario ya estaba confirmado.");
            mostrarMensaje("El usuario ya estaba confirmado.");
            return false;
        }
    }

    public boolean confirmarRegistroFallecido(Usuario fallecido, Usuario confirmador) {
        System.out.println("Iniciando confirmación de fallecido...");

        // Validar que los parámetros no sean nulos
        if (fallecido == null) {
            System.out.println("Fallecido es null.");
            mostrarMensaje("El usuario fallecido no puede ser nulo.");
            return false;
        }
        if (confirmador == null) {
            System.out.println("No se ha configurado el confirmador correctamente.");
            mostrarMensaje("No hay un usuario actual definido para confirmar esta acción.");
            return false;
        }

        // Verificar que el usuario fallecido tenga fecha de defunción registrada
        if (fallecido.getFechaDefuncion() == null) {
            System.out.println("El usuario no tiene una fecha de defunción registrada.");
            mostrarMensaje("El usuario no está marcado como fallecido.");
            return false;
        }

        // Verificar si el confirmador ya ha registrado su confirmación
        if (!fallecido.getConfirmadores().contiene(confirmador)) {
            fallecido.agregarConfirmador(confirmador); // Agregar confirmador
            System.out.println("Confirmador agregado: " + confirmador.getNombreCompleto());
            mostrarMensaje("Confirmación registrada por " + confirmador.getNombreCompleto() + ".");
        } else {
            System.out.println("El confirmador ya ha registrado su confirmación.");
            mostrarMensaje("El confirmador ya ha registrado su confirmación.");
            return false; // Ya se confirmó, no es necesario proceder más
        }

        // Verificar si cumple el requisito (mínimo 3 confirmadores)
        if (fallecido.registroConfirmadoPorFallecimiento()) {
            fallecido.setConfirmado(true);
            System.out.println("El registro del fallecido " + fallecido.getNombreCompleto() + " ha sido confirmado.");
            mostrarMensaje("El registro del fallecido " + fallecido.getNombreCompleto() + " ha sido completamente confirmado.");
            return true;
        }

        System.out.println("No hay suficientes confirmadores todavía.");
        mostrarMensaje("Aún no se han registrado suficientes confirmaciones.");
        return true;
    }

    public boolean confirmarRegistroMenor(Usuario menor, Usuario confirmador) {
        System.out.println("Confirmando registro de menor...");

        if (menor == null || confirmador == null) {
            System.out.println("Menor o confirmador es null.");
            mostrarMensaje("Datos inválidos para confirmar el registro.");
            return false;
        }

        // Verificar si la persona es menor de edad
        if (menor.getEdad() >= 18) {
            System.out.println("La persona no es menor de edad.");
            mostrarMensaje("La persona no es menor de edad.");
            return false;
        }

        // Validar si el confirmador es un familiar directo hasta el segundo grado
        Lista<Usuario> validadores = obtenerFamiliaresHastaSegundoGrado(menor);
        if (validadores.contiene(confirmador)) {
            // Verificar duplicados antes de agregar confirmador
            if (!menor.getConfirmadores().contiene(confirmador)) {
                menor.agregarConfirmador(confirmador);
                System.out.println("Confirmador agregado: " + confirmador.getNombreCompleto());
                mostrarMensaje("Confirmación registrada por " + confirmador.getNombreCompleto() + ".");
            } else {
                System.out.println("El confirmador ya estaba registrado.");
                mostrarMensaje("El confirmador ya ha registrado su confirmación.");
            }

            // Verificar si se cumple el requisito de confirmación
            if (menor.registroConfirmadoPorMenor()) {
                menor.setConfirmado(true);
                System.out.println("Registro del menor confirmado.");
                mostrarMensaje("El registro del menor " + menor.getNombreCompleto() + " ha sido completamente confirmado.");
                return true;
            } else {
                System.out.println("No hay suficientes confirmadores aún.");
                mostrarMensaje("Aún no se ha registrado suficiente confirmación.");
                return false;
            }
        } else {
            System.out.println("El confirmador no es válido.");
            mostrarMensaje("El confirmador no cumple los requisitos para confirmar este registro.");
            return false;
        }
    }

    private boolean esFamiliarDirectoHastaSegundoGrado(Usuario menor, Usuario confirmador) {
        return menor.getPadre() == confirmador || menor.getMadre() == confirmador ||
                menor.getHijos().contiene(confirmador) || // abuelos
                menor.getPadre() != null && menor.getPadre().getPadre() == confirmador || // bisabuelos
                menor.getPadre() != null && menor.getPadre().getHijos().contiene(confirmador); // hermanos
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

    public void actualizarUsuarioActual(Usuario nuevoUsuario) {
        this.usuarioActual = nuevoUsuario;
        mostrarMensaje("Usuario actual actualizado a: " + nuevoUsuario.getNombreCompleto());
    }

    public void enviarCorreo(Usuario destinatario, String asunto, String mensaje) {
        if (destinatario == null) {
            throw new IllegalArgumentException("Destinatario no válido.");
        }
        Correo correo = new Correo(destinatario, asunto, mensaje); // Crear el correo con los datos correctos
        destinatario.recibirCorreo(correo); // Agregar el correo a la bandeja de entrada del destinatario
        mostrarMensaje("Correo enviado a: " + destinatario.getNombreCompleto());
    }
    public Usuario buscarUsuarioPorId(int usuarioId){
        return arbolGenealogico.buscarUsuarioPorId(usuarioId);
    }
}