package org.example.view;

import org.example.controller.ArbolController;
import org.example.estructura.ListaDoblementeEncadenada.Lista;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class ConsolaView {
    private ArbolController controller;
    private Stage primaryStage;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ConsolaView(ArbolController controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
        mostrarMenu();
    }

    private void mostrarMenu() {
        Button btnCrearUsuario = new Button("Crear usuario raíz");
        Button btnAgregarFamiliar = new Button("Agregar familiar a un usuario raíz");
        Button btnEditarFamiliar = new Button("Editar familiar");
        Button btnMostrarArbol = new Button("Mostrar árbol genealógico de un usuario raíz");
        Button btnListarPendientes = new Button("Listar familiares pendientes de confirmación");
        Button btnListarFamiliares = new Button("Listar familiares por generación y edad");
        Button btnBuscarParentesco = new Button("Buscar parentesco por nombre");
        Button btnSalir = new Button("Salir");

        btnCrearUsuario.setOnAction(e -> crearUsuarioRaiz());
        btnAgregarFamiliar.setOnAction(e -> {
            if (controller.obtenerUsuariosRaiz().isEmpty()) {
                mostrarMensaje("No hay usuarios raíz disponibles. Primero debe crear un usuario raíz.");
            } else {
                seleccionarUsuarioParaAgregarFamiliar();
            }
        });

        btnMostrarArbol.setOnAction(e -> mostrarArbolGenealogico());
        btnListarPendientes.setOnAction(e -> listarPendientesConfirmacion());
        btnListarFamiliares.setOnAction(e -> listarFamiliaresPorEdad());
        btnBuscarParentesco.setOnAction(e -> buscarParentescoPorNombre());
        btnEditarFamiliar.setOnAction(e -> editarFamiliar());
        btnSalir.setOnAction(e -> primaryStage.close());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(50));
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(
                btnCrearUsuario,
                btnAgregarFamiliar,
                btnEditarFamiliar,
                btnMostrarArbol,
                btnListarPendientes,
                btnListarFamiliares,
                btnBuscarParentesco,
                btnSalir
        );

        Scene scene = new Scene(vbox, 700, 500);
        aplicarEstilos(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Menú Principal");
        primaryStage.show();
    }

    private void cargarMenuPrincipal() {
        mostrarMenu();
    }

    private void crearUsuarioRaiz() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(50));
        vbox.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Crear Usuario Raíz");
        lblTitulo.getStyleClass().add("titulo-ventana");

        TextField txtPrimerNombre = new TextField();
        TextField txtSegundoNombre = new TextField();
        TextField txtApellidoPaterno = new TextField();
        TextField txtApellidoMaterno = new TextField();
        TextField txtCedula = new TextField();
        DatePicker dpFechaNacimiento = new DatePicker();
        DatePicker dpFechaDefuncion = new DatePicker();

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = crearBotonVolver();

        btnGuardar.setOnAction(e -> {
            try {
                String primerNombre = txtPrimerNombre.getText().trim();
                String segundoNombre = txtSegundoNombre.getText().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().trim();
                int cedula = leerCedula(txtCedula.getText().trim());
                LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
                LocalDate fechaDefuncion = dpFechaDefuncion.getValue();

                if (primerNombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || fechaNacimiento == null) {
                    mostrarMensaje("Por favor, complete todos los campos obligatorios.");
                    return;
                }

                Usuario usuario = new Usuario(
                        primerNombre,
                        segundoNombre,
                        apellidoPaterno,
                        apellidoMaterno,
                        fechaNacimiento,
                        fechaDefuncion,
                        cedula
                );
                controller.agregarUsuarioRaiz(usuario);
                mostrarMensaje("Usuario raíz creado exitosamente.");
                cargarMenuPrincipal();
            } catch (Exception ex) {
                mostrarMensaje("Error al crear el usuario: " + ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> cargarMenuPrincipal());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Primer Nombre*:"), 0, 0);
        grid.add(txtPrimerNombre, 1, 0);
        grid.add(new Label("Segundo Nombre:"), 0, 1);
        grid.add(txtSegundoNombre, 1, 1);
        grid.add(new Label("Apellido Paterno*:"), 0, 2);
        grid.add(txtApellidoPaterno, 1, 2);
        grid.add(new Label("Apellido Materno*:"), 0, 3);
        grid.add(txtApellidoMaterno, 1, 3);
        grid.add(new Label("Cédula (8 dígitos)*:"), 0, 4);
        grid.add(txtCedula, 1, 4);
        grid.add(new Label("Fecha Nacimiento*:"), 0, 5);
        grid.add(dpFechaNacimiento, 1, 5);
        grid.add(new Label("Fecha Defunción:"), 0, 6);
        grid.add(dpFechaDefuncion, 1, 6);
        grid.add(btnGuardar, 1, 7);
        grid.add(btnVolver, 0, 7);

        vbox.getChildren().addAll(lblTitulo, grid);

        Scene scene = new Scene(vbox, 700, 500);
        aplicarEstilos(scene);

        primaryStage.setScene(scene);
    }

    private void seleccionarUsuarioParaAgregarFamiliar() {
        VBox contenedorSeleccion = new VBox(10);
        contenedorSeleccion.setPadding(new Insets(20));
        contenedorSeleccion.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Seleccione un Usuario Raíz");
        lblTitulo.getStyleClass().add("titulo-seleccion-usuario");

        ListView<Usuario> listViewUsuarios = new ListView<>();
        listViewUsuarios.getItems().addAll(controller.obtenerUsuariosRaiz());
        listViewUsuarios.getStyleClass().add("list-view-usuarios");

        Button btnSeleccionar = new Button("Seleccionar");
        btnSeleccionar.getStyleClass().add("boton-seleccionar");
        btnSeleccionar.setOnAction(e -> {
            Usuario usuarioSeleccionado = listViewUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                ventanaAgregarFamiliar(usuarioSeleccionado);
            } else {
                mostrarMensaje("Debe seleccionar un usuario raíz.");
            }
        });

        Button btnVolver = crearBotonVolver();

        contenedorSeleccion.getChildren().addAll(lblTitulo, listViewUsuarios, btnSeleccionar, btnVolver);

        Scene escenaSeleccion = new Scene(contenedorSeleccion, 400, 400);
        aplicarEstilos(escenaSeleccion);

        primaryStage.setScene(escenaSeleccion);
    }


    private void ventanaAgregarFamiliar(Usuario usuario) {
        VBox contenedorPrincipal = new VBox(10);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Agregar Familiar a " + usuario.getNombreCompleto());
        lblTitulo.getStyleClass().add("titulo-agregar-familiar");

        // Menú desplegable para seleccionar el tipo de parentesco
        ComboBox<String> cbTipoParentesco = new ComboBox<>();
        cbTipoParentesco.getItems().addAll("Padre", "Madre", "Hijo");
        cbTipoParentesco.setValue("Padre"); // Valor predeterminado

        TextField txtPrimerNombre = new TextField();
        txtPrimerNombre.setPromptText("Primer Nombre");

        TextField txtSegundoNombre = new TextField();
        txtSegundoNombre.setPromptText("Segundo Nombre (Opcional)");

        TextField txtApellidoPaterno = new TextField();
        txtApellidoPaterno.setPromptText("Apellido Paterno");

        TextField txtApellidoMaterno = new TextField();
        txtApellidoMaterno.setPromptText("Apellido Materno");

        TextField txtCedula = new TextField();
        txtCedula.setPromptText("Cédula (8 dígitos)");

        DatePicker dpFechaNacimiento = new DatePicker();
        dpFechaNacimiento.setPromptText("Fecha de Nacimiento");

        DatePicker dpFechaDefuncion = new DatePicker();
        dpFechaDefuncion.setPromptText("Fecha de Defunción (Opcional)");

        Button btnGuardar = new Button("Guardar");
        btnGuardar.getStyleClass().add("boton-guardar");

        btnGuardar.setOnAction(e -> {
            try {
                String tipoParentesco = cbTipoParentesco.getValue();
                String primerNombre = txtPrimerNombre.getText().trim();
                String segundoNombre = txtSegundoNombre.getText().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().trim();
                String cedulaTexto = txtCedula.getText().trim();
                LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
                LocalDate fechaDefuncion = dpFechaDefuncion.getValue();

                if (primerNombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || fechaNacimiento == null) {
                    mostrarMensaje("Por favor, complete todos los campos obligatorios.");
                    return;
                }

                int cedula = leerCedula(cedulaTexto);

                Usuario familiar = new Usuario(
                        primerNombre,
                        segundoNombre,
                        apellidoPaterno,
                        apellidoMaterno,
                        fechaNacimiento,
                        fechaDefuncion,
                        cedula
                );

                // Asignar el familiar según el tipo de parentesco seleccionado
                switch (tipoParentesco) {
                    case "Padre":
                        controller.agregarPadre(usuario.getId(), familiar);
                        break;
                    case "Madre":
                        controller.agregarMadre(usuario.getId(), familiar);
                        break;
                    case "Hijo":
                        controller.agregarHijo(usuario.getId(), familiar);
                        break;
                }

                mostrarMensaje(tipoParentesco + " agregado exitosamente.");
                ventanaAgregarFamiliar(usuario); // Recargar la ventana para mostrar los cambios
            } catch (Exception ex) {
                mostrarMensaje("Error al agregar el familiar: " + ex.getMessage());
            }
        });

        Button btnVolver = crearBotonVolver();

        // Lista de familiares del usuario
        ListView<Usuario> listViewFamiliares = new ListView<>();
        listViewFamiliares.getItems().addAll(controller.obtenerFamiliares(usuario));
        listViewFamiliares.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic para editar
                Usuario seleccionado = listViewFamiliares.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    ventanaAgregarFamiliar(seleccionado);
                }
            }
        });

        VBox listaFamiliares = new VBox(10);
        listaFamiliares.getChildren().addAll(new Label("Familiares existentes:"), listViewFamiliares);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Tipo de Parentesco:"), 0, 0);
        grid.add(cbTipoParentesco, 1, 0);

        grid.add(new Separator(), 0, 1, 2, 1);

        grid.add(new Label("Primer Nombre*:"), 0, 2);
        grid.add(txtPrimerNombre, 1, 2);
        grid.add(new Label("Segundo Nombre:"), 0, 3);
        grid.add(txtSegundoNombre, 1, 3);
        grid.add(new Label("Apellido Paterno*:"), 0, 4);
        grid.add(txtApellidoPaterno, 1, 4);
        grid.add(new Label("Apellido Materno*:"), 0, 5);
        grid.add(txtApellidoMaterno, 1, 5);
        grid.add(new Label("Cédula (8 dígitos)*:"), 0, 6);
        grid.add(txtCedula, 1, 6);
        grid.add(new Label("Fecha de Nacimiento*:"), 0, 7);
        grid.add(dpFechaNacimiento, 1, 7);
        grid.add(new Label("Fecha de Defunción:"), 0, 8);
        grid.add(dpFechaDefuncion, 1, 8);

        grid.add(btnGuardar, 1, 9);
        grid.add(btnVolver, 0, 9);

        contenedorPrincipal.getChildren().addAll(lblTitulo, grid, listaFamiliares);

        Scene escenaAgregarFamiliar = new Scene(contenedorPrincipal, 800, 700);
        aplicarEstilos(escenaAgregarFamiliar);

        primaryStage.setScene(escenaAgregarFamiliar);
    }

    private void editarFamiliar() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Editar Familiar");
        lblTitulo.getStyleClass().add("titulo");

        ListView<Usuario> listView = new ListView<>();
        listView.getItems().addAll(controller.obtenerTodosUsuarios()); // Carga de usuarios existentes
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button btnSeleccionar = new Button("Editar");
        btnSeleccionar.setOnAction(e -> {
            Usuario seleccionado = listView.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                ventanaAgregarFamiliar(seleccionado); // Reutilizar ventanaAgregarFamiliar
            } else {
                mostrarMensaje("Por favor, seleccione un usuario.");
            }
        });

        Button btnVolver = crearBotonVolver();
        btnVolver.setOnAction(e -> cargarMenuPrincipal());

        vbox.getChildren().addAll(lblTitulo, listView, btnSeleccionar, btnVolver);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
    }

    private void mostrarArbolGenealogico() {
        if (controller.obtenerUsuariosRaiz().isEmpty()) {
            mostrarMensaje("No hay usuarios raíz disponibles.");
            return;
        }

        VBox contenedorSeleccion = new VBox(10);
        contenedorSeleccion.setPadding(new Insets(20));
        contenedorSeleccion.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Seleccione un Usuario Raíz");
        lblTitulo.getStyleClass().add("titulo-seleccion-usuario");

        ListView<Usuario> listViewUsuarios = new ListView<>();
        listViewUsuarios.getItems().addAll(controller.obtenerUsuariosRaiz());
        listViewUsuarios.getStyleClass().add("list-view-usuarios");

        Button btnSeleccionar = new Button("Seleccionar");
        btnSeleccionar.getStyleClass().add("boton-seleccionar");
        btnSeleccionar.setOnAction(e -> {
            Usuario usuarioRaiz = listViewUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioRaiz != null) {
                try {
                    // Mostrar el árbol genealógico del usuario seleccionado
                    ArbolGenealogicoView arbolView = new ArbolGenealogicoView(controller, usuarioRaiz);
                    Scene scene = new Scene(arbolView.getRoot(), 800, 600);
                    aplicarEstilos(scene);

                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Árbol Genealógico de " + usuarioRaiz.getNombreCompleto());
                } catch (Exception ex) {
                    mostrarMensaje("Ocurrió un error al cargar el árbol genealógico: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                mostrarMensaje("Debe seleccionar un usuario raíz.");
            }
        });

        Button btnVolver = crearBotonVolver();

        contenedorSeleccion.getChildren().addAll(lblTitulo, listViewUsuarios, btnSeleccionar, btnVolver);

        Scene escenaSeleccion = new Scene(contenedorSeleccion, 400, 400);
        aplicarEstilos(escenaSeleccion);

        primaryStage.setScene(escenaSeleccion);
    }

    private void listarPendientesConfirmacion() {
        if (controller.obtenerUsuariosRaiz().isEmpty()) {
            mostrarMensaje("No hay usuarios raíz disponibles.");
            return;
        }

        Usuario usuarioRaiz = controller.obtenerUsuariosRaiz().get(0);

        List<Usuario> pendientes = controller.obtenerFamiliaresPendientes(usuarioRaiz);

        if (pendientes.isEmpty()) {
            mostrarMensaje("No hay familiares pendientes de confirmación.");
            return;
        }

        VBox contenedorPrincipal = new VBox(10);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Familiares Pendientes de Confirmación");
        lblTitulo.getStyleClass().add("titulo-pendientes");

        ListView<Usuario> listViewPendientes = new ListView<>();
        listViewPendientes.getItems().addAll(pendientes);
        listViewPendientes.getStyleClass().add("list-view-pendientes");

        Button btnVolver = crearBotonVolver();

        contenedorPrincipal.getChildren().addAll(lblTitulo, listViewPendientes, btnVolver);

        Scene escenaPendientes = new Scene(contenedorPrincipal, 700, 500);
        aplicarEstilos(escenaPendientes);
        primaryStage.setScene(escenaPendientes);
    }

    private void listarFamiliaresPorEdad() {
        if (controller.obtenerUsuariosRaiz().isEmpty()) {
            mostrarMensaje("No hay usuarios raíz disponibles.");
            return;
        }

        VBox contenedorPrincipal = new VBox(10);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Listar Familiares por Generación y Edad");
        lblTitulo.getStyleClass().add("titulo-listar-familiares");

        Spinner<Integer> spGeneracion = new Spinner<>(0, 10, 0);
        spGeneracion.getStyleClass().add("spinner-generacion");

        Button btnListar = new Button("Listar");
        btnListar.getStyleClass().add("boton-listar");
        btnListar.setOnAction(e -> {
            int generacion = spGeneracion.getValue();
            Usuario usuarioRaiz = controller.obtenerUsuariosRaiz().get(0);
            List<Usuario> familiares = controller.obtenerFamiliaresPorEdad(usuarioRaiz, generacion);

            if (familiares.isEmpty()) {
                mostrarMensaje("No hay familiares en la generación especificada.");
            } else {
                VBox contenedorLista = new VBox(10);
                contenedorLista.setPadding(new Insets(20));
                contenedorLista.setAlignment(Pos.TOP_CENTER);

                Label lblFamiliares = new Label("Familiares de la Generación " + generacion);
                lblFamiliares.getStyleClass().add("titulo-familiares-generacion");

                ListView<String> listViewFamiliares = new ListView<>();
                for (Usuario usuario : familiares) {
                    listViewFamiliares.getItems().add(usuario.getNombreCompleto() + " - Edad: " + usuario.getEdad());
                }
                listViewFamiliares.getStyleClass().add("list-view-familiares");

                Button btnVolver = crearBotonVolver();

                contenedorLista.getChildren().addAll(lblFamiliares, listViewFamiliares, btnVolver);

                Scene escenaListaFamiliares = new Scene(contenedorLista, 700, 500);
                aplicarEstilos(escenaListaFamiliares);
                primaryStage.setScene(escenaListaFamiliares);
            }
        });

        Button btnVolver = crearBotonVolver();

        contenedorPrincipal.getChildren().addAll(lblTitulo, new Label("Ingrese la generación (0 para la generación actual):"), spGeneracion, btnListar, btnVolver);

        Scene escenaFamiliares = new Scene(contenedorPrincipal, 700, 500);
        aplicarEstilos(escenaFamiliares);
        primaryStage.setScene(escenaFamiliares);
    }

    private void buscarParentescoPorNombre() {
        if (controller.obtenerUsuariosRaiz().isEmpty()) {
            mostrarMensaje("No hay usuarios raíz disponibles.");
            return;
        }

        VBox contenedorPrincipal = new VBox(10);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Buscar Parentesco por Nombre");
        lblTitulo.getStyleClass().add("titulo-buscar-parentesco");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ingrese el nombre completo");
        txtNombre.getStyleClass().add("text-field-buscar");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("boton-buscar");
        btnBuscar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarMensaje("Debe ingresar un nombre.");
                return;
            }

            Usuario usuarioRaiz = controller.obtenerUsuariosRaiz().get(0);
            String parentesco = controller.buscarParentescoPorNombre(usuarioRaiz, nombre);

            mostrarMensaje("Parentesco: " + parentesco);
        });

        Button btnVolver = crearBotonVolver();

        contenedorPrincipal.getChildren().addAll(lblTitulo, txtNombre, btnBuscar, btnVolver);

        Scene escenaBuscarParentesco = new Scene(contenedorPrincipal, 700, 400);
        aplicarEstilos(escenaBuscarParentesco);
        primaryStage.setScene(escenaBuscarParentesco);
    }

    private void aplicarEstilos(Scene scene) {
        try {
            String css = getClass().getResource("/org/example/css/estilos.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception ex) {
            mostrarMensaje("No se pudo cargar el archivo de estilos CSS.");
            ex.printStackTrace();
        }
    }

    public void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private int leerCedula(String input) throws Exception {
        try {
            int cedula = Integer.parseInt(input);
            if (String.valueOf(cedula).length() == 8) {
                return cedula;
            } else {
                throw new Exception("La cédula debe tener exactamente 8 dígitos.");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Cédula inválida. Debe ser un número de 8 dígitos.");
        }
    }

    private Button crearBotonVolver() {
        Button btnVolver = new Button("Volver");
        btnVolver.setAlignment(Pos.TOP_LEFT);
        btnVolver.getStyleClass().add("boton-volver");
        btnVolver.setOnAction(event -> mostrarMenu());
        return btnVolver;
    }

    private List<String> obtenerParentescosDeSangreSegunGrado(int grado) {
        List<String> parentescos = new java.util.ArrayList<>();
        switch (grado) {
            case 1: // Relación directa
                parentescos.add("Padre");
                parentescos.add("Madre");
                parentescos.add("Hermano");
                parentescos.add("Hermana");
                parentescos.add("Hijo");
                parentescos.add("Hija");
                break;
            case 2: // Relación de segundo grado
                parentescos.add("Abuelo");
                parentescos.add("Abuela");
                parentescos.add("Tío");
                parentescos.add("Tía");
                parentescos.add("Nieto");
                parentescos.add("Nieta");
                parentescos.add("Sobrino");
                parentescos.add("Sobrina");
                break;
            case 3: // Relación de tercer grado
                parentescos.add("Bisabuelo");
                parentescos.add("Bisabuela");
                parentescos.add("Primo");
                parentescos.add("Prima");
                parentescos.add("Bisnieto");
                parentescos.add("Bisnieta");
                break;
            default: // Relación genérica
                parentescos.add("Pariente Grado " + grado);
                break;
        }
        return parentescos;
    }
}