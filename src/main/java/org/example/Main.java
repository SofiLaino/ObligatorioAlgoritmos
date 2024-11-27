package org.example;

import org.example.controller.ArbolController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.example.model.ArbolGenealogico;
import org.example.model.Usuario;
import org.example.view.ConsolaView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Inicialización del árbol genealógico y controlador
        ArbolGenealogico arbolGenealógico = new ArbolGenealogico();
        ArbolController controller = new ArbolController(arbolGenealógico, null);

        // Cargar datos de prueba
        cargarDatosDePrueba(controller);

        // Inicializar la vista
        new ConsolaView(controller, primaryStage);

        primaryStage.setTitle("Árbol Genealógico");
        primaryStage.show();
    }

    private void cargarDatosDePrueba(ArbolController controller) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Usuario raíz
            Usuario usuarioRaiz1 = new Usuario("Sofia", "Noelia", "Laino", "Gonzalez",
                    LocalDate.parse("28/09/1994", dateFormatter), null, 54867197);
            controller.agregarUsuarioRaiz(usuarioRaiz1);

            // Familiares del usuario raíz
            Usuario padre = new Usuario("Sergio", "Nicolas", "Laino", "Quintana",
                    LocalDate.parse("26/02/1960", dateFormatter), null, 15284176);
            controller.agregarPadre(usuarioRaiz1.getId(), padre);

            Usuario madre = new Usuario("Maria", "del Carmen", "Gonzalez", "Gonzalez",
                    LocalDate.parse("02/06/1960", dateFormatter), null, 17566342);
            controller.agregarMadre(usuarioRaiz1.getId(), madre);

            Usuario hijo = new Usuario("Osvaldito", "Justin", "Laino", "Gonzalez",
                    LocalDate.parse("15/05/1995", dateFormatter), null, 33445566);
            controller.agregarHijo(usuarioRaiz1.getId(), hijo);

            // Abuelos paternos
            Usuario abueloPaterno = new Usuario("Francisco", "", "Laino", "Decundo",
                    LocalDate.parse("05/06/1929", dateFormatter), null, 45672345);
            if (padre != null) {
                controller.agregarPadre(padre.getId(), abueloPaterno);
            }

            Usuario abuelaPaterna = new Usuario("Maria", "Irma", "Quintana", "Mieres",
                    LocalDate.parse("04/12/1929", dateFormatter), null, 55667788);
            if (padre != null) {
                controller.agregarMadre(padre.getId(), abuelaPaterna);
            }

            // Abuelos maternos
            Usuario abueloMaterno = new Usuario("Braulio", "", "Gonzalez", "Gonzalez",
                    LocalDate.parse("26/03/1914", dateFormatter), null, 66778899);
            if (madre != null) {
                controller.agregarPadre(madre.getId(), abueloMaterno);
            }

            Usuario abuelaMaterna = new Usuario("Maria", "Dolores", "Gonzalez", "Camacho",
                    LocalDate.parse("22/11/1918", dateFormatter), null, 77889900);
            if (madre != null) {
                controller.agregarMadre(madre.getId(), abuelaMaterna);
            }

            System.out.println("Datos de prueba cargados exitosamente.");
        } catch (DateTimeParseException e) {
            System.err.println("Error al cargar datos: formato de fecha incorrecto.");
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar datos: " + e.getMessage());
        }
    }
}