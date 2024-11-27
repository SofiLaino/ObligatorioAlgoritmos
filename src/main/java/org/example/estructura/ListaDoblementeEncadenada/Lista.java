package org.example.estructura.ListaDoblementeEncadenada;

import org.example.model.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lista<T extends Comparable<T>> implements IListaDoble<T> {
    private NodoDoble<T> inicio; // Nodo inicial de la lista
    private int contador; // Contador de elementos en la lista

    // Constructor
    public Lista() {
        inicio = null;
        contador = 0;
    }

    // Retorna el número de elementos en la lista
    public int size() {
        return contador;
    }

    @Override
    public void agregar(T n) {
        // Agrega un elemento al final de la lista
        NodoDoble<T> nodo = new NodoDoble<>(n);
        if (estaVacio()) {
            inicio = nodo;
        } else {
            NodoDoble<T> aux = inicio;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nodo);
            nodo.setAnterior(aux);
        }
        contador++;
    }

    // Agrega un elemento al inicio de la lista
    public void agregarInicio(T n) {
        NodoDoble<T> nodo = new NodoDoble<>(n);
        if (estaVacio()) {
            inicio = nodo;
        } else {
            inicio.setAnterior(nodo);
            nodo.setSiguiente(inicio);
            inicio = nodo;
        }
        contador++;
    }

    // Obtiene un elemento específico en la lista
    public T obtenerElemento(T n) {
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            if (actual.getDato().compareTo(n) == 0) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    @Override
    public boolean estaVacio() {
        return inicio == null;
    }

    @Override
    public void imprimir() {
        // Imprime los elementos de la lista
        NodoDoble<T> actual = inicio;
        int posicion = 1;
        while (actual != null) {
            System.out.println(posicion++ + " - " + actual.getDato());
            actual = actual.getSiguiente();
        }
    }

    @Override
    public void vacia() {
        // Vacía la lista
        while (!estaVacio()) {
            eliminar();
        }
        contador = 0;
    }

    @Override
    public void eliminar() {
        // Elimina el primer elemento de la lista
        if (!estaVacio()) {
            inicio = inicio.getSiguiente();
            if (inicio != null) {
                inicio.setAnterior(null);
            }
            contador--;
        }
    }

    // Elimina un elemento específico de la lista
    public void removerItem(T item) {
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            if (actual.getDato().compareTo(item) == 0) {
                if (actual.getAnterior() == null && actual.getSiguiente() == null) {
                    inicio = null;
                } else if (actual.getAnterior() == null) {
                    inicio = actual.getSiguiente();
                    inicio.setAnterior(null);
                } else if (actual.getSiguiente() == null) {
                    actual.getAnterior().setSiguiente(null);
                } else {
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }
                contador--;
                return;
            }
            actual = actual.getSiguiente();
        }
    }

    // Retorna una nueva lista con todos los elementos excepto el primero
    public Lista<T> cola() {
        Lista<T> nuevaLista = new Lista<>();
        NodoDoble<T> actual = inicio != null ? inicio.getSiguiente() : null;
        while (actual != null) {
            nuevaLista.agregar(actual.getDato());
            actual = actual.getSiguiente();
        }
        return nuevaLista;
    }

    // Retorna el primer nodo de la lista
    public NodoDoble<T> cabeza() {
        return inicio;
    }

    // Verifica si la lista contiene un elemento
    public boolean contiene(T n) {
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            if (actual.getDato().compareTo(n) == 0) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    // Ordena una lista de usuarios por fecha de nacimiento
    public void ordenarListaPorFechaNacimiento(Lista<Usuario> lista) {
        lista.setCabeza(mergeSort(lista.cabeza()));
    }

    // Implementación de MergeSort para ordenar la lista
    private NodoDoble<Usuario> mergeSort(NodoDoble<Usuario> cabeza) {
        if (cabeza == null || cabeza.getSiguiente() == null) {
            return cabeza;
        }
        NodoDoble<Usuario> mitad = dividir(cabeza);
        NodoDoble<Usuario> izquierda = mergeSort(cabeza);
        NodoDoble<Usuario> derecha = mergeSort(mitad);
        return fusionar(izquierda, derecha);
    }

    private NodoDoble<Usuario> dividir(NodoDoble<Usuario> cabeza) {
        NodoDoble<Usuario> lento = cabeza;
        NodoDoble<Usuario> rapido = cabeza;
        while (rapido.getSiguiente() != null && rapido.getSiguiente().getSiguiente() != null) {
            lento = lento.getSiguiente();
            rapido = rapido.getSiguiente().getSiguiente();
        }
        NodoDoble<Usuario> mitad = lento.getSiguiente();
        lento.setSiguiente(null);
        if (mitad != null) mitad.setAnterior(null);
        return mitad;
    }

    private NodoDoble<Usuario> fusionar(NodoDoble<Usuario> izquierda, NodoDoble<Usuario> derecha) {
        NodoDoble<Usuario> dummy = new NodoDoble<>(null);
        NodoDoble<Usuario> actual = dummy;

        while (izquierda != null && derecha != null) {
            if (izquierda.getDato().getFechaNacimiento().compareTo(derecha.getDato().getFechaNacimiento()) <= 0) {
                actual.setSiguiente(izquierda);
                izquierda.setAnterior(actual);
                izquierda = izquierda.getSiguiente();
            } else {
                actual.setSiguiente(derecha);
                derecha.setAnterior(actual);
                derecha = derecha.getSiguiente();
            }
            actual = actual.getSiguiente();
        }
        if (izquierda != null) actual.setSiguiente(izquierda);
        if (derecha != null) actual.setSiguiente(derecha);

        return dummy.getSiguiente();
    }

    // Actualiza la cabeza de la lista
    public void setCabeza(NodoDoble<T> nuevoInicio) {
        this.inicio = nuevoInicio;
        contador = 0;
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
    }

    // Convierte la lista en un ArrayList
    public List<Usuario> toList() {
        List<Usuario> list = new ArrayList<>();
        NodoDoble<T> actual = cabeza();
        while (actual != null) {
            list.add((Usuario) actual.getDato());
            actual = actual.getSiguiente();
        }
        return list;
    }

    public int tamaño() {
        return contador;
    }

    // Obtiene un elemento en una posición específica
    public T obtenerElementoEnPosicion(int posicion) {
        if (posicion < 0 || posicion >= contador) {
            throw new IndexOutOfBoundsException("Posición fuera de rango.");
        }
        NodoDoble<T> actual = cabeza();
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // Remueve un elemento en una posición específica
    public void removerEnPosicion(int posicion) {
        if (posicion < 0 || posicion >= contador) {
            throw new IndexOutOfBoundsException("Posición fuera de rango.");
        }
        NodoDoble<T> actual = cabeza();
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        removerItem(actual.getDato());
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoDoble<T> actual = inicio;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }
}