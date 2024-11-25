package org.example;

import org.example.controller.ArbolController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.example.model.ArbolGenealogico;
import org.example.model.Usuario;
import org.example.view.ConsolaView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Inicialización del árbol genealógico y controlador
        ArbolGenealogico arbolGenealogico = new ArbolGenealogico();
        ArbolController controller = new ArbolController(arbolGenealogico, null);

        // Cargar datos de prueba con diferentes grados de parentesco
        cargarDatosDePrueba(controller);

        // Inicializar la vista de consola
        new ConsolaView(controller, primaryStage);

        primaryStage.setTitle("Árbol Genealógico");
        primaryStage.show();
    }

    private void cargarDatosDePrueba(ArbolController controller) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Usuario raíz
        Usuario usuarioRaiz1 = new Usuario("Juan", "", "Pérez", "González",
                LocalDate.parse("01/01/1970", dateFormatter), null, 12345678);
        controller.agregarUsuarioRaiz(usuarioRaiz1);

        // Agregar familiares del usuario raíz
        Usuario padre = new Usuario("José", "", "Pérez", "Martínez",
                LocalDate.parse("01/01/1945", dateFormatter), null, 87654321);
        controller.agregarPadre(usuarioRaiz1.getId(), padre);

        Usuario madre = new Usuario("Ana", "", "González", "López",
                LocalDate.parse("05/05/1948", dateFormatter), null, 11223344);
        controller.agregarMadre(usuarioRaiz1.getId(), madre);

        Usuario hijo = new Usuario("Andrés", "", "Pérez", "Martínez",
                LocalDate.parse("15/05/1995", dateFormatter), null, 33445566);
        controller.agregarHijo(usuarioRaiz1.getId(), hijo);

        Usuario abueloPaterno = new Usuario("Ramón", "", "Pérez", "Soto",
                LocalDate.parse("15/03/1920", dateFormatter), null, 44556677);
        controller.agregarPadre(padre.getId(), abueloPaterno);

        Usuario abuelaPaterna = new Usuario("Clara", "", "Martínez", "Rivas",
                LocalDate.parse("20/07/1925", dateFormatter), null, 55667788);
        controller.agregarMadre(padre.getId(), abuelaPaterna);

        Usuario abueloMaterno = new Usuario("Javier", "", "González", "Soto",
                LocalDate.parse("10/10/1922", dateFormatter), null, 66778899);
        controller.agregarPadre(madre.getId(), abueloMaterno);

        Usuario abuelaMaterna = new Usuario("María", "", "López", "Rivas",
                LocalDate.parse("25/12/1926", dateFormatter), null, 77889900);
        controller.agregarMadre(madre.getId(), abuelaMaterna);

        Usuario abuelaMaterna2 = new Usuario("María2", "", "López2", "Rivas",
                LocalDate.parse("25/12/1926", dateFormatter), null, 77889900);
        controller.agregarMadre(abuelaMaterna.getId(), abuelaMaterna2);

        Usuario abueloMaterno2 = new Usuario("Javier2", "", "González2", "Soto",
                LocalDate.parse("10/10/1922", dateFormatter), null, 66778899);
        controller.agregarPadre(abuelaMaterna.getId(), abueloMaterno2);

        Usuario hermano = new Usuario("Luis", "", "Pérez", "González",
                LocalDate.parse("10/10/1975", dateFormatter), null, 99001122);
        controller.agregarHijo(padre.getId(), hermano);

        Usuario nieto = new Usuario("Carlos", "", "Pérez", "Pereira",
                LocalDate.parse("20/08/2000", dateFormatter), null, 10111213);
        controller.agregarHijo(hijo.getId(), nieto);

        System.out.println("Datos de prueba cargados exitosamente.");
    }
}