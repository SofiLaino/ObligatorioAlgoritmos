package org.example.estructura.ListaDoblementeEncadenada;

import java.util.Iterator;

public interface IListaDoble<T extends Comparable<T>> extends Iterable<T> {

    // Agrega un elemento a la lista.
    void agregar(T elemento);

// Verifica si la lista está vacía.

    boolean estaVacio();

    // Imprime los elementos de la lista.
    void imprimir();

    // Vacía la lista.
    void vacia();

    // Elimina el último elemento de la lista.
    void eliminar();

    // Retorna la cantidad de elementos en la lista.
    int size();

    // Verifica si un elemento está en la lista.
    boolean contiene(T elemento);

    // Obtiene un elemento por su posición.
    T obtenerElementoEnPosicion(int indice);

    // Permite recorrer la lista con un iterador.
    @Override
    Iterator<T> iterator();
}