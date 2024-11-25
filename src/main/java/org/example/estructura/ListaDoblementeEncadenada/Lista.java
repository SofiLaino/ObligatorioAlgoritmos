package org.example.estructura.ListaDoblementeEncadenada;

import org.example.model.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lista<T extends Comparable<T>> implements IListaDoble<T> {
    NodoDoble<T> inicio;
    int contador;

    public int contador() {
        return contador;
    }

    @Override
    public void agregar(T n) {
        NodoDoble<T> nodo = new NodoDoble<T>(n);
        if (estaVacio()) {
            nodo.setSiguiente(null);
            nodo.setAnterior(null);
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

    public void agregarInicio(T n) {
        NodoDoble<T> nodo = new NodoDoble<T>(n);
        if (estaVacio()) {
            nodo.setSiguiente(null);
            nodo.setAnterior(null);
            inicio = nodo;
        } else {
            inicio.setAnterior(nodo);
            nodo.setSiguiente(inicio);
            nodo.setAnterior(null);
            inicio = nodo;
        }
        contador++;
    }

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
        NodoDoble<T> actual = inicio;
        int contador = 1;
        while (actual != null) {
            System.out.println(contador++ + " - " + actual.getDato());
            actual = actual.getSiguiente();
        }
    }

    @Override
    public void vacia() {
        while (!estaVacio()) {
            eliminar();
        }
        contador = 0;
    }

    @Override
    public void eliminar() {
        if (contador > 0) {
            inicio = inicio.getSiguiente();
            if (inicio != null) {
                inicio.setAnterior(null);
            }
            contador--;
        }
    }

    public void removerItem(T item) {
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            if (actual.getDato().compareTo(item) == 0) {
                if (actual.getAnterior() == null && actual.getSiguiente() == null) {
                    inicio = null;
                } else {
                    if (actual.getAnterior() == null) {
                        actual.getSiguiente().setAnterior(null);
                        inicio = actual.getSiguiente();
                    } else {
                        if (actual.getSiguiente() == null) {
                            actual.getAnterior().setSiguiente(null);
                        } else {
                            actual.getAnterior().setSiguiente(actual.getSiguiente());
                            actual.getSiguiente().setAnterior(actual.getAnterior());
                        }
                    }
                }
            }
            actual = actual.getSiguiente();
        }
    }

    public Lista<T> cola() {
        Lista<T> cola = new Lista<T>();
        NodoDoble<T> actual = inicio.getSiguiente();
        while (actual != null) {
            cola.agregar(actual.getDato());
            actual = actual.getSiguiente();
        }
        return cola;
    }

    public NodoDoble<T> cabeza() {
        return inicio;
    }

    public boolean contiene(T n) {
        Lista<T> lista = new Lista<T>();
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            if (actual.getDato().compareTo(n) == 0) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Lista() {
        inicio = null;
        contador = 0;
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

    public void ordenarListaPorFechaNacimiento(Lista<Usuario> lista) {
        // Usar MergeSort para ordenar la lista
        lista.setCabeza(mergeSort(lista.cabeza()));
    }

    // Método principal de MergeSort
    private NodoDoble<Usuario> mergeSort(NodoDoble<Usuario> cabeza) {
        // Caso base: lista vacía o con un solo elemento
        if (cabeza == null || cabeza.getSiguiente() == null) {
            return cabeza;
        }

        // Dividir la lista en dos mitades
        NodoDoble<Usuario> mitad = dividir(cabeza);

        // Ordenar cada mitad recursivamente
        NodoDoble<Usuario> izquierda = mergeSort(cabeza);
        NodoDoble<Usuario> derecha = mergeSort(mitad);

        // Fusionar las dos mitades ordenadas
        return fusionar(izquierda, derecha);
    }

    // Método para dividir la lista en dos mitades
    private NodoDoble<Usuario> dividir(NodoDoble<Usuario> cabeza) {
        NodoDoble<Usuario> lento = cabeza;
        NodoDoble<Usuario> rapido = cabeza;

        // Avanzar rápido dos pasos y lento un paso
        while (rapido.getSiguiente() != null && rapido.getSiguiente().getSiguiente() != null) {
            lento = lento.getSiguiente();
            rapido = rapido.getSiguiente().getSiguiente();
        }

        // Dividir la lista en dos mitades
        NodoDoble<Usuario> mitad = lento.getSiguiente();
        lento.setSiguiente(null); // Terminar la primera mitad
        if (mitad != null) {
            mitad.setAnterior(null); // Separar la segunda mitad
        }

        return mitad;
    }

    // Método para fusionar dos listas ordenadas
    private NodoDoble<Usuario> fusionar(NodoDoble<Usuario> izquierda, NodoDoble<Usuario> derecha) {
        // Nodo ficticio para construir la lista fusionada
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

        // Conectar los nodos restantes
        if (izquierda != null) {
            actual.setSiguiente(izquierda);
            izquierda.setAnterior(actual);
        }
        if (derecha != null) {
            actual.setSiguiente(derecha);
            derecha.setAnterior(actual);
        }

        // Retornar el nuevo inicio de la lista fusionada
        return dummy.getSiguiente();
    }

    public void setCabeza(NodoDoble<T> nuevoInicio) {
        this.inicio = nuevoInicio;
        // Actualizar el contador recorriendo los nodos
        contador = 0;
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
    }



    public List<Usuario> toList() {
        List<Usuario> list = new ArrayList<>();
        NodoDoble<T> current = this.cabeza(); // Usamos cabeza() para obtener el primer nodo de la lista
        while (current != null) {
            list.add((Usuario) current.getDato()); // Aseguramos el casting a Usuario si la lista contiene instancias de Usuario
            current = current.getSiguiente();
        }
        return list;
    }

    public int tamaño() {
        return contador;  // Devuelve el número de elementos en la lista
    }

}