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
            if (controller.obtenerUsuariosRaiz().isEmpty()) {
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

        // Acción del botón Guardar
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
                    mostrarMensaje("Por favor, complete todos los campos obligatorios marcados con '*'.");
                    return;
                }

                int cedula = leerCedula(cedulaTexto); // Método que valida y convierte la cédula

                Usuario familiar = new Usuario(
                        primerNombre,
                        segundoNombre,
                        apellidoPaterno,
                        apellidoMaterno,
                        fechaNacimiento,
                        fechaDefuncion,
                        cedula
                );

                // Asignar el familiar utilizando el método que procesa notificaciones automáticamente
                controller.agregarFamiliar(usuario, familiar);

                mostrarMensaje(tipoParentesco + " agregado exitosamente con notificaciones procesadas.");
                ventanaAgregarFamiliar(usuario); // Recargar la ventana para reflejar los cambios
            } catch (NumberFormatException nfe) {
                mostrarMensaje("La cédula debe ser un número válido de 8 dígitos.");
            } catch (Exception ex) {
                mostrarMensaje("Error al agregar el familiar: " + ex.getMessage());
            }
        });

        Button btnVolver = crearBotonVolver();

        // Lista de familiares existentes
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

    private void editarFamiliar() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Editar Familiar");
        lblTitulo.getStyleClass().add("titulo");

        // ListView para seleccionar un usuario a editar
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

        vbox.getChildren().addAll(lblTitulo, new Label("Seleccione un familiar para editar:"), listView, btnSeleccionar, btnVolver);

        Scene scene = new Scene(vbox, 800, 600);
        aplicarEstilos(scene); // Aplicar estilos definidos
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
        Usuario usuarioActual = controller.getUsuarioActual();
        if (usuarioActual == null) {
            mostrarMensaje("Debe seleccionar un usuario para listar pendientes.");
            return;
        }

        Lista<Usuario> pendientes = controller.obtenerPendientesDeConfirmacion(usuarioActual);
        if (pendientes.estaVacio()) {
            mostrarMensaje("No hay familiares pendientes de confirmación.");
        } else {
            System.out.println("Familiares pendientes de confirmación:");
            for (Usuario pendiente : pendientes) {
                System.out.println(pendiente.getNombreCompleto());
            }
        }
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

    private void gestionarCasillaDeCorreo() {
        // Obtener usuarios raíz disponibles
        List<Usuario> usuariosRaiz = controller.obtenerUsuariosRaiz();
        if (usuariosRaiz.isEmpty()) {
            mostrarMensaje("No hay usuarios raíz disponibles.");
            return;
        }

        // Crear una nueva ventana para seleccionar un usuario
        Stage stageSeleccionarUsuario = new Stage();
        stageSeleccionarUsuario.setTitle("Seleccionar Usuario para la Casilla de Correo");

        // ListView para mostrar los usuarios raíz disponibles
        ListView<String> listViewUsuarios = new ListView<>();
        for (Usuario usuario : usuariosRaiz) {
            listViewUsuarios.getItems().add(usuario.getNombreCompleto());
        }

        // Botón para seleccionar al usuario
        Button btnSeleccionarUsuario = new Button("Seleccionar Usuario");
        btnSeleccionarUsuario.setOnAction(event -> {
            int selectedIndex = listViewUsuarios.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Usuario usuarioSeleccionado = usuariosRaiz.get(selectedIndex);
                mostrarCasillaDeCorreo(usuarioSeleccionado); // Mostrar notificaciones del usuario seleccionado
                stageSeleccionarUsuario.close();
            } else {
                mostrarMensaje("Debe seleccionar un usuario para continuar.");
            }
        });

        // Diseño de la ventana de selección
        VBox vboxSeleccionar = new VBox(10, new Label("Seleccione un Usuario:"), listViewUsuarios, btnSeleccionarUsuario);
        vboxSeleccionar.setPadding(new Insets(20));
        vboxSeleccionar.setAlignment(Pos.CENTER);

        // Configurar y mostrar la escena
        Scene sceneSeleccionar = new Scene(vboxSeleccionar, 400, 300);
        stageSeleccionarUsuario.setScene(sceneSeleccionar);
        stageSeleccionarUsuario.show();
    }

    private void mostrarCasillaDeCorreo(Usuario usuario) {
        Stage stageCasilla = new Stage();
        stageCasilla.setTitle("Casilla de Correo: " + usuario.getNombreCompleto());

        ListView<String> listViewCorreos = new ListView<>();
        for (Correo correo : usuario.getBandejaDeEntrada()) {
            listViewCorreos.getItems().add(correo.toString());
        }

        Button btnLeerCorreo = new Button("Leer Correo");
        Button btnEliminarCorreo = new Button("Eliminar Correo");
        Button btnCerrar = new Button("Cerrar");

        btnLeerCorreo.setOnAction(e -> {
            int selectedIndex = listViewCorreos.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Correo correo = usuario.getBandejaDeEntrada().obtenerElementoEnPosicion(selectedIndex);
                correo.marcarComoLeido();
                mostrarMensaje("Correo leído: " + correo.getMensaje());
                listViewCorreos.getItems().set(selectedIndex, correo.toString()); // Actualizar estado
            } else {
                mostrarMensaje("Seleccione un correo para leer.");
            }
        });

        btnEliminarCorreo.setOnAction(e -> {
            int selectedIndex = listViewCorreos.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                usuario.getBandejaDeEntrada().removerEnPosicion(selectedIndex);
                listViewCorreos.getItems().remove(selectedIndex);
                mostrarMensaje("Correo eliminado.");
            } else {
                mostrarMensaje("Seleccione un correo para eliminar.");
            }
        });

        btnCerrar.setOnAction(e -> stageCasilla.close());

        VBox vboxCasilla = new VBox(10, listViewCorreos, btnLeerCorreo, btnEliminarCorreo, btnCerrar);
        vboxCasilla.setPadding(new Insets(20));
        vboxCasilla.setAlignment(Pos.CENTER);

        Scene sceneCasilla = new Scene(vboxCasilla, 600, 400);
        stageCasilla.setScene(sceneCasilla);
        stageCasilla.show();
    }

    private void seleccionarUsuarioActual() {
        List<Usuario> usuariosRaiz = controller.obtenerUsuariosRaiz();
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
                Usuario seleccionado = usuariosRaiz.get(selectedIndex);
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