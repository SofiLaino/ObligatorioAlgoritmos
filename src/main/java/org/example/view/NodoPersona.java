package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.model.Usuario;

public class NodoPersona extends VBox {
    private Usuario usuario;

    public NodoPersona(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        this.usuario = usuario;
        inicializar();
    }

    private void inicializar() {
        // Crear una etiqueta con el nombre completo del usuario
        Label lblNombre = new Label(usuario.getNombreCompleto() != null ? usuario.getNombreCompleto() : "Nombre desconocido");
        lblNombre.getStyleClass().add("label-nombre");

        // Crear una etiqueta para mostrar la edad (si está disponible)
        Label lblEdad = new Label();
        if (usuario.getFechaNacimiento() != null) {
            lblEdad.setText("Edad: " + usuario.getEdad() + " años");
        } else {
            lblEdad.setText("Edad desconocida");
        }
        lblEdad.getStyleClass().add("label-edad");

        // Aplicar una clase CSS para el estilo del nodo
        this.getStyleClass().add("nodo-persona");

        // Configuración del VBox
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10); // Espacio entre elementos
        this.setPadding(new javafx.geometry.Insets(10));

        // Agregar las etiquetas al VBox
        this.getChildren().addAll(lblNombre, lblEdad);

        // Evento al hacer clic en el nodo
        this.setOnMouseClicked(event -> mostrarDetallesUsuario());
    }

    private void mostrarDetallesUsuario() {
        // Aquí puedes agregar una ventana emergente o funcionalidad adicional
        System.out.println("Has hecho clic en: " + usuario.getNombreCompleto());
        System.out.println("Detalles: " + usuario.toString());
    }
}