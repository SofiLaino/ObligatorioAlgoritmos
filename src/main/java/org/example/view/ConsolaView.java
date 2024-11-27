package org.example.view;

import org.example.controller.ArbolController;
import org.example.estructura.ListaDoblementeEncadenada.Lista;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.Correo;
import org.example.model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsolaView {
    private ArbolController controller;
    private Stage primaryStage;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ListView<Usuario> listViewFamiliares; // Campo de clase

    public ConsolaView(ArbolController controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
        mostrarMenu();
    }

    private void mostrarMenu() {
        // Crear botones del menú
        Button btnCrearUsuario = new Button("Crear usuario raíz");
        Button btnAgregarFamiliar = new Button("Agregar familiar a un usuario raíz");
        Button btnEditarFamiliar = new Button("Editar familiar");
        Button btnMostrarArbol = new Button("Mostrar árbol genealógico de un usuario raíz");
        Button btnListarFamiliares = new Button("Listar familiares por generación y edad");
        Button btnBuscarParentesco = new Button("Buscar parentesco por nombre");
        Button btnCasillaCorreo = new Button("Casilla de Correo");
        Button btnSalir = new Button("Salir");

        // Asignar acciones a los botones
        btnCrearUsuario.setOnAction(e -> crearUsuarioRaiz());
        btnAgregarFamiliar.setOnAction(e -> {
            if (controller.obtenerUsuariosRaiz().estaVacio()) {
                mostrarMensaje("No hay usuarios raíz disponibles. Primero debe crear un usuario raíz.");
            } else {
                seleccionarUsuarioParaAgregarFamiliar();
            }
        });

        btnEditarFamiliar.setOnAction(e -> editarFamiliar());
        btnMostrarArbol.setOnAction(e -> mostrarArbolGenealogico());
        btnListarFamiliares.setOnAction(e -> listarFamiliaresPorEdad());
        btnBuscarParentesco.setOnAction(e -> buscarParentescoPorNombre());
        btnCasillaCorreo.setOnAction(e -> gestionarCasillaDeCorreo()); // Casilla de correo
        btnSalir.setOnAction(e -> primaryStage.close());

        // Diseñar la disposición de los botones
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(50));
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(
                btnCrearUsuario,
                btnAgregarFamiliar,
                btnEditarFamiliar,
                btnMostrarArbol,
                btnListarFamiliares,
                btnBuscarParentesco,
                btnCasillaCorreo,
                btnSalir
        );

        // Configurar la escena y mostrarla
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
        // Convertir la Lista personalizada a una lista estándar de Java
        for (Usuario usuario : controller.obtenerUsuariosRaiz()) {
            listViewUsuarios.getItems().add(usuario);
        }
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

        // Título de la ventana
        Label lblTitulo = new Label("Agregar Familiar a " + usuario.getNombreCompleto());
        lblTitulo.getStyleClass().add("titulo-agregar-familiar");

        // Menú desplegable para seleccionar el tipo de parentesco
        ComboBox<String> cbTipoParentesco = new ComboBox<>();
        cbTipoParentesco.getItems().addAll("Padre", "Madre", "Hijo");
        cbTipoParentesco.setValue("Padre"); // Valor predeterminado

        // Campos de texto para los datos del familiar
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

        // Campos para fechas
        DatePicker dpFechaNacimiento = new DatePicker();
        dpFechaNacimiento.setPromptText("Fecha de Nacimiento");

        DatePicker dpFechaDefuncion = new DatePicker();
        dpFechaDefuncion.setPromptText("Fecha de Defunción (Opcional)");

        // Lista de familiares existentes usando tu clase Lista
        ListView<Usuario> listViewFamiliares = new ListView<>();
        Lista<Usuario> familiares = controller.obtenerFamiliares(usuario); // Usando Lista personalizada
        for (Usuario familiar : familiares) { // Iterar sobre la lista para mostrar familiares
            listViewFamiliares.getItems().add(familiar);
        }
        listViewFamiliares.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic para editar
                Usuario seleccionado = listViewFamiliares.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    ventanaAgregarFamiliar(seleccionado);
                }
            }
        });

        // Acción del botón Guardar
        Button btnGuardar = new Button("Guardar");
        btnGuardar.getStyleClass().add("boton-guardar");
        btnGuardar.setOnAction(e -> {
            try {
                // Leer y validar los campos
                String tipoParentesco = cbTipoParentesco.getValue();
                String primerNombre = txtPrimerNombre.getText().trim();
                String segundoNombre = txtSegundoNombre.getText().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().trim();
                String cedulaTexto = txtCedula.getText().trim();
                LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
                LocalDate fechaDefuncion = dpFechaDefuncion.getValue();

                // Validaciones básicas
                if (primerNombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || fechaNacimiento == null) {
                    mostrarMensaje("Por favor, complete todos los campos obligatorios marcados con '*'.");
                    return;
                }

                int cedula = leerCedula(cedulaTexto); // Validar y convertir la cédula

                // Validar si la cédula ya está registrada
                Lista<Usuario> todosLosUsuarios = controller.obtenerTodosUsuarios();
                boolean cedulaDuplicada = false;

                for (Usuario usuarioExistente : todosLosUsuarios) { // Iterar manualmente para verificar duplicados
                    if (usuarioExistente.getCedula() == cedula) {
                        cedulaDuplicada = true;
                        break;
                    }
                }

                if (cedulaDuplicada) {
                    mostrarMensaje("La cédula ingresada ya está registrada.");
                    return;
                }

                // Crear el objeto Usuario para el familiar
                Usuario familiar = new Usuario(
                        primerNombre,
                        segundoNombre,
                        apellidoPaterno,
                        apellidoMaterno,
                        fechaNacimiento,
                        fechaDefuncion,
                        cedula
                );

                // Llamar al controlador para agregar el familiar
                controller.agregarFamiliar(usuario, familiar, tipoParentesco);

                // Actualizar la lista de familiares para evitar duplicados
                listViewFamiliares.getItems().clear();
                Lista<Usuario> actualizados = controller.obtenerFamiliares(usuario);
                for (Usuario actualizado : actualizados) {
                    listViewFamiliares.getItems().add(actualizado);
                }

                mostrarMensaje(tipoParentesco + " agregado exitosamente.");
                mostrarMensaje("Se envió una notificación de confirmación al familiar agregado.");

                // Limpiar el formulario para permitir agregar otro familiar
                txtPrimerNombre.clear();
                txtSegundoNombre.clear();
                txtApellidoPaterno.clear();
                txtApellidoMaterno.clear();
                txtCedula.clear();
                dpFechaNacimiento.setValue(null);
                dpFechaDefuncion.setValue(null);
                cbTipoParentesco.setValue("Padre");

            } catch (NumberFormatException nfe) {
                mostrarMensaje("La cédula debe ser un número válido de 8 dígitos.");
            } catch (Exception ex) {
                mostrarMensaje("Error al agregar el familiar: " + ex.getMessage());
            }
        });

        // Botón Volver
        Button btnVolver = crearBotonVolver();

        VBox listaFamiliares = new VBox(10);
        listaFamiliares.getChildren().addAll(new Label("Familiares existentes:"), listViewFamiliares);

        // Diseño del formulario
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Tipo de Parentesco*:"), 0, 0);
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

        // Configuración de la escena
        Scene escenaAgregarFamiliar = new Scene(contenedorPrincipal, 800, 700);
        aplicarEstilos(escenaAgregarFamiliar);

        primaryStage.setScene(escenaAgregarFamiliar);
    }

    public void actualizarListaFamiliares(Usuario usuario) {
        if (listViewFamiliares != null) {
            listViewFamiliares.getItems().clear();

            // Obtener familiares usando tu clase Lista
            Lista<Usuario> familiares = controller.obtenerFamiliares(usuario);
            for (Usuario familiar : familiares) { // Iterar sobre la lista personalizada
                listViewFamiliares.getItems().add(familiar);
            }
        }
    }

    private void editarFamiliar() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Editar Familiar");
        lblTitulo.getStyleClass().add("titulo");

        // ListView para seleccionar un usuario a editar
        ListView<Usuario> listView = new ListView<>();
        Lista<Usuario> usuarios = controller.obtenerTodosUsuarios(); // Obtener usuarios como Lista personalizada

        // Iterar sobre la lista personalizada para cargar los elementos en el ListView
        for (Usuario usuario : usuarios) {
            listView.getItems().add(usuario);
        }

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

        vbox.getChildren().addAll(lblTitulo, new Label("Seleccione un familiar para editar:"), listView, btnSeleccionar, btnVolver);

        Scene scene = new Scene(vbox, 800, 600);
        aplicarEstilos(scene); // Aplicar estilos definidos
        primaryStage.setScene(scene);
    }

    private void mostrarArbolGenealogico() {
        if (controller.obtenerUsuariosRaiz().estaVacio()) {
            mostrarMensaje("No hay usuarios raíz disponibles.");
            return;
        }

        VBox contenedorSeleccion = new VBox(10);
        contenedorSeleccion.setPadding(new Insets(20));
        contenedorSeleccion.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Seleccione un Usuario Raíz");
        lblTitulo.getStyleClass().add("titulo-seleccion-usuario");

        ListView<Usuario> listViewUsuarios = new ListView<>();
        listViewUsuarios.getItems().addAll(controller.obtenerUsuariosRaiz().toList());
        listViewUsuarios.getStyleClass().add("list-view-usuarios");

        Button btnSeleccionar = new Button("Seleccionar");
        btnSeleccionar.getStyleClass().add("boton-seleccionar");
        btnSeleccionar.setOnAction(e -> {
            Usuario usuarioRaiz = listViewUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioRaiz != null) {
                try {
                    // Mostrar el árbol genealógico del usuario seleccionado
                    ArbolGenealogicoView vista = new ArbolGenealogicoView(controller, usuarioRaiz, this::volverAlMenuPrincipal);
                    Scene scene = new Scene(vista.getRoot(), 800, 600);
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

// Crear el botón "Volver" para regresar al menú principal
        Button btnVolver = new Button("Volver");
        btnVolver.getStyleClass().add("boton-volver");
        btnVolver.setOnAction(e -> volverAlMenuPrincipal());

        contenedorSeleccion.getChildren().addAll(lblTitulo, listViewUsuarios, btnSeleccionar, btnVolver);

        Scene escenaSeleccion = new Scene(contenedorSeleccion, 400, 400);
        aplicarEstilos(escenaSeleccion);

        primaryStage.setScene(escenaSeleccion);
    }

    private void volverAlMenuPrincipal() {
        mostrarMenu();
    }

    private void mostrarPendientesConfirmacion(Usuario usuario) {
        Lista<Usuario> pendientes = controller.obtenerFamiliaresPendientes(usuario);
        for (Usuario pendiente : pendientes) {
            System.out.println("Familiar pendiente: " + pendiente.getNombreCompleto());
        }
    }

    private void listarFamiliaresPorEdad() {
        if (controller.obtenerUsuariosRaiz().estaVacio()) {
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
            Usuario usuarioRaiz = controller.obtenerUsuariosRaiz().cabeza().getDato(); // Obtener el dato del primer nodo
            Lista<Usuario> familiares = controller.obtenerFamiliaresPorEdad(usuarioRaiz, generacion);

            if (familiares.estaVacio()) { // Verificar si la lista de familiares está vacía
                mostrarMensaje("No hay familiares en la generación especificada.");
            } else {
                VBox contenedorLista = new VBox(10);
                contenedorLista.setPadding(new Insets(20));
                contenedorLista.setAlignment(Pos.TOP_CENTER);

                Label lblFamiliares = new Label("Familiares de la Generación " + generacion);
                lblFamiliares.getStyleClass().add("titulo-familiares-generacion");

                ListView<String> listViewFamiliares = new ListView<>();
                for (Usuario usuario : familiares) { // Iterar sobre la lista personalizada
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
        if (controller.obtenerUsuariosRaiz().estaVacio()) {
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

            if (controller.obtenerUsuariosRaiz().estaVacio()) {
                mostrarMensaje("No hay usuarios raíz disponibles.");
                return;
            }
            Usuario usuarioRaiz = controller.obtenerUsuariosRaiz().obtenerElementoEnPosicion(0);
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

    private void gestionarCasillaDeCorreo() {
        // Obtener todos los usuarios disponibles
        Lista<Usuario> todosLosUsuarios = controller.obtenerTodosUsuarios();
        if (todosLosUsuarios.estaVacio()) {
            mostrarMensaje("No hay usuarios disponibles.");
            return;
        }

        Stage stageSeleccionarUsuario = new Stage();
        stageSeleccionarUsuario.setTitle("Seleccionar Usuario para Casilla de Correo");

        // ListView para mostrar los usuarios disponibles
        ListView<String> listViewUsuarios = new ListView<>();
        for (Usuario usuario : todosLosUsuarios) {
            listViewUsuarios.getItems().add(usuario.getNombreCompleto());
        }

        // Botón para seleccionar al usuario
        Button btnSeleccionarUsuario = new Button("Seleccionar Usuario");
        btnSeleccionarUsuario.setOnAction(event -> {
            int selectedIndex = listViewUsuarios.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                // Obtener el usuario seleccionado
                Usuario usuarioSeleccionado = todosLosUsuarios.obtenerElementoEnPosicion(selectedIndex);

                // Actualizar el usuario actual en el controlador
                controller.actualizarUsuarioActual(usuarioSeleccionado);

                // Mostrar la casilla de correo para el usuario seleccionado
                mostrarCasillaDeCorreo(usuarioSeleccionado);

                // Cerrar la ventana de selección
                stageSeleccionarUsuario.close();
            } else {
                // Mostrar mensaje de error si no se seleccionó un usuario
                mostrarMensaje("Debe seleccionar un usuario para continuar.");
            }
        });


        VBox vboxSeleccionar = new VBox(10, new Label("Seleccione un Usuario:"), listViewUsuarios, btnSeleccionarUsuario);
        vboxSeleccionar.setPadding(new Insets(20));
        vboxSeleccionar.setAlignment(Pos.CENTER);

        Scene sceneSeleccionar = new Scene(vboxSeleccionar, 400, 300);
        stageSeleccionarUsuario.setScene(sceneSeleccionar);
        stageSeleccionarUsuario.show();
    }

    private void mostrarCasillaDeCorreo(Usuario usuario) {
        Stage stage = new Stage();
        stage.setTitle("Casilla de Correo: " + usuario.getNombreCompleto());

        // ListView para mostrar los asuntos de los correos
        ListView<String> listViewCorreos = new ListView<>();
        // Cargar los asuntos de los correos en la bandeja de entrada del usuario
        usuario.getBandejaDeEntrada().forEach(correo -> listViewCorreos.getItems().add(correo.getAsunto()));

        // Área de texto para mostrar el contenido completo del correo seleccionado
        TextArea areaContenidoCorreo = new TextArea();
        areaContenidoCorreo.setEditable(false); // Solo lectura
        areaContenidoCorreo.setWrapText(true);  // Ajustar texto automáticamente
        areaContenidoCorreo.setPrefHeight(200);

        // Botón para marcar como leído
        Button btnMarcarLeido = new Button("Marcar como leído");
        btnMarcarLeido.setDisable(true); // Deshabilitado hasta que se seleccione un correo

        // Botón para confirmar acciones específicas
        Button btnConfirmar = new Button("Confirmar acción");
        btnConfirmar.setDisable(true); // Deshabilitado hasta que se seleccione un correo relevante

        // Listener para seleccionar correos
        listViewCorreos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = listViewCorreos.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Correo correoSeleccionado = usuario.getBandejaDeEntrada().obtenerElementoEnPosicion(selectedIndex);
                areaContenidoCorreo.setText(correoSeleccionado.getMensaje()); // Mostrar el contenido del correo
                btnMarcarLeido.setDisable(false); // Habilitar botón de marcar como leído

                // Verificar si el correo requiere confirmación
                String asuntoCorreo = correoSeleccionado.getAsunto().toLowerCase();
                if (asuntoCorreo.contains("menor") || asuntoCorreo.contains("confirmación requerida") ||
                        asuntoCorreo.contains("fallecimiento") || asuntoCorreo.contains("invitación")) {
                    btnConfirmar.setDisable(false); // Habilitar el botón "Confirmar"
                } else {
                    btnConfirmar.setDisable(true); // Deshabilitar si no aplica
                }
            }
        });

        // Acción para marcar como leído
        btnMarcarLeido.setOnAction(e -> {
            int selectedIndex = listViewCorreos.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Correo correo = usuario.getBandejaDeEntrada().obtenerElementoEnPosicion(selectedIndex);
                correo.marcarComoLeido();
                mostrarMensaje("Correo marcado como leído.");
                listViewCorreos.getItems().set(selectedIndex, correo.getAsunto() + " (Leído)"); // Actualizar estado
            }
        });

        // Acción para confirmar una notificación
        btnConfirmar.setOnAction(e -> {
            int selectedIndex = listViewCorreos.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Correo correo = usuario.getBandejaDeEntrada().obtenerElementoEnPosicion(selectedIndex);

                // Paso 1: Imprime el contenido del correo seleccionado para depuración
                System.out.println("Contenido del correo seleccionado: " + correo.getMensaje().toLowerCase());

                boolean confirmacionExitosa = false;

                String mensajeCorreo = correo.getMensaje();
                String id = mensajeCorreo.substring(
                        mensajeCorreo.indexOf("Id:") + 3,
                        mensajeCorreo.indexOf(")", mensajeCorreo.indexOf("Id:"))
                ).trim();
                Usuario usuarioAConfirmar = controller.buscarUsuarioPorId(Integer.parseInt(id));

                // Confirmar acciones según el mensaje del correo
                String asuntoCorreo = correo.getAsunto().toLowerCase();
                if (asuntoCorreo.contains("menor")) {
                    confirmacionExitosa = controller.confirmarRegistroMenor(usuarioAConfirmar, usuario);
                } else if (asuntoCorreo.contains("fallecimiento")) {
                    confirmacionExitosa = controller.confirmarRegistroFallecido(usuarioAConfirmar, usuario);
                } else if (asuntoCorreo.contains("invitación")) {
                    confirmacionExitosa = controller.confirmarDatosPersona(usuario);
                }

                if (confirmacionExitosa) {
                    mostrarMensaje("Acción confirmada correctamente.");
                    correo.marcarComoLeido(); // Marcar el correo como leído
                    listViewCorreos.getItems().set(selectedIndex, correo.getAsunto() + " (Procesado)"); // Actualizar estado
                    btnConfirmar.setDisable(true); // Deshabilitar el botón de confirmar después de procesar
                } else {
                    mostrarMensaje("No se pudo procesar la acción. Verifica los datos.");
                }
            }
        });

        // Botón para cerrar la ventana
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> stage.close());

        // Layout principal
        VBox vbox = new VBox(10, new Label("Correos de " + usuario.getNombreCompleto()), listViewCorreos, areaContenidoCorreo, btnMarcarLeido, btnConfirmar, btnCerrar);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        // Configuración de la escena
        Scene scene = new Scene(vbox, 600, 600);
        aplicarEstilos(scene); // Aplicar estilos personalizados
        stage.setScene(scene);
        stage.show();
    }

    private void seleccionarUsuarioActual() {
        Lista<Usuario> usuariosRaiz = controller.obtenerUsuariosRaiz();
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Usuario Actual");

        ListView<String> listViewUsuarios = new ListView<>();
        for (Usuario usuario : usuariosRaiz) {
            listViewUsuarios.getItems().add(usuario.getNombreCompleto());
        }

        Button btnSeleccionar = new Button("Seleccionar");
        btnSeleccionar.setOnAction(event -> {
            int selectedIndex = listViewUsuarios.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Usuario seleccionado = usuariosRaiz.obtenerElementoEnPosicion(selectedIndex); // Reemplazo de get
                controller.actualizarUsuarioActual(seleccionado);
                mostrarMensaje("Usuario actual: " + seleccionado.getNombreCompleto());
                stage.close();
            } else {
                mostrarMensaje("Seleccione un usuario.");
            }
        });

        VBox vbox = new VBox(10, listViewUsuarios, btnSeleccionar);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
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
}