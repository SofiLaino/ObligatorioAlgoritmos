package org.example.model;

import org.example.estructura.ListaDoblementeEncadenada.Lista;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ArbolGenealogico {
    private Lista<Usuario> raices;

    public ArbolGenealogico() {
        this.raices = new Lista<Usuario>();
    }

    public Lista<Usuario> getRaices() {
        return raices;
    }

    public void agregarRaiz(Usuario raiz) {
        if (raiz != null) {
            raices.agregar(raiz);
        } else {
            throw new IllegalArgumentException("La raíz no puede ser nula.");
        }
    }

    public Usuario buscarUsuarioPorId(int id) {
        for (Usuario raiz : raices) {
            Usuario encontrado = buscarUsuarioPorIdRecursivo(raiz, id, new HashSet<>());
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }

    private Usuario buscarUsuarioPorIdRecursivo(Usuario usuario, int id, Set<Usuario> visitados) {
        if (usuario == null) return null;

        // Verificar si el usuario actual ya fue visitado (evitar ciclos)
        if (visitados.contains(usuario)) return null;

        // Marcar el usuario como visitado
        visitados.add(usuario);

        // Verificar si el usuario actual tiene el ID buscado
        if (usuario.getId() == id) return usuario;

        // Buscar en el padre
        if (usuario.getPadre() != null) {
            Usuario resultadoPadre = buscarUsuarioPorIdRecursivo(usuario.getPadre(), id, visitados);
            if (resultadoPadre != null) return resultadoPadre;
        }

        // Buscar en la madre
        if (usuario.getMadre() != null) {
            Usuario resultadoMadre = buscarUsuarioPorIdRecursivo(usuario.getMadre(), id, visitados);
            if (resultadoMadre != null) return resultadoMadre;
        }

        // Buscar en los hijos
        for (Usuario hijo : usuario.getHijos()) {
            Usuario resultadoHijo = buscarUsuarioPorIdRecursivo(hijo, id, visitados);
            if (resultadoHijo != null) return resultadoHijo;
        }

        // Si no se encuentra, retornar null
        return null;
    }

    public void mostrarArbol(Usuario usuarioRaiz) {
        if (usuarioRaiz != null) {
            System.out.println("Árbol genealógico de: " + usuarioRaiz.getNombreCompleto());
            mostrarArbolRecursivo(usuarioRaiz, 0);
        }
    }

    private void mostrarArbolRecursivo(Usuario usuario, int nivel) {
        if (usuario == null) return;

        String indentacion = " ".repeat(nivel * 4);
        System.out.println(indentacion + usuario);

        for (Usuario hijo : usuario.getHijos()) {
            mostrarArbolRecursivo(hijo, nivel + 1);
        }
    }
}