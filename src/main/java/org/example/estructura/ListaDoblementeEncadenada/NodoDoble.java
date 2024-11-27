package org.example.estructura.ListaDoblementeEncadenada;

public class NodoDoble<T extends Comparable<T>> {

    private T dato; // Dato almacenado en el nodo
    private NodoDoble<T> siguiente; // Referencia al siguiente nodo
    private NodoDoble<T> anterior; // Referencia al nodo anterior

    // Constructor que inicializa el nodo con un dato
    public NodoDoble(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }

    // Obtiene el dato almacenado en el nodo
    public T getDato() {
        return dato;
    }

    // Asigna un dato al nodo
    public void setDato(T dato) {
        this.dato = dato;
    }

    // Obtiene el nodo siguiente
    public NodoDoble<T> getSiguiente() {
        return siguiente;
    }

    // Asigna el nodo siguiente
    public void setSiguiente(NodoDoble<T> siguiente) {
        this.siguiente = siguiente;
    }

    // Obtiene el nodo anterior
    public NodoDoble<T> getAnterior() {
        return anterior;
    }

    // Asigna el nodo anterior
    public void setAnterior(NodoDoble<T> anterior) {
        this.anterior = anterior;
    }
}