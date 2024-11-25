package org.example.estructura.ListaDoblementeEncadenada;

public class NodoDoble<T extends Comparable<T>> {
    private T dato;
    private NodoDoble<T> siguiente;
    private NodoDoble<T> anterior;

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoDoble<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoDoble<T> siguiente) {
        this.siguiente = siguiente;
    }

    public NodoDoble<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoDoble<T> anterior) {
        this.anterior = anterior;
    }

    public NodoDoble(T dato) {
        this.dato = dato;
        siguiente = null;
        anterior = null;
    }
}