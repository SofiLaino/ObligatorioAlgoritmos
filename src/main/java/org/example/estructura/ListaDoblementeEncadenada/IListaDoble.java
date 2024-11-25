package org.example.estructura.ListaDoblementeEncadenada;

import java.util.Iterator;

public interface IListaDoble<T extends Comparable<T>> extends Iterable<T> {

    void agregar(T n);

    boolean estaVacio();

    void imprimir();

    void vacia();

    void eliminar();

    @Override
    Iterator<T> iterator();
}