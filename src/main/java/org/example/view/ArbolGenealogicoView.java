package org.example.view;

import org.example.controller.ArbolController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ArbolGenealogicoView {
    private ArbolController controller;
    private ScrollPane root;
    private Pane pane;
    private Usuario usuarioRaiz;

    private Map<Usuario, Rectangle> nodosMapa;
    private Set<Usuario> nodosVisitados;

    public ArbolGenealogicoView(ArbolController controller, Usuario usuarioRaiz) {
        if (controller == null || usuarioRaiz == null) {
            throw new IllegalArgumentException("El controlador y el usuario raíz no pueden ser nulos.");
        }
        this.controller = controller;
        this.usuarioRaiz = usuarioRaiz;
        this.nodosMapa = new HashMap<>();
        this.nodosVisitados = new HashSet<>();

        pane = new Pane();
        root = new ScrollPane(pane);

        // Aplicar la hoja de estilo
        try {
            String css = getClass().getResource("/org/example/css/estilos.css").toExternalForm();
            root.getStylesheets().add(css);
        } catch (Exception ex) {
            System.err.println("No se pudo cargar la hoja de estilos CSS: " + ex.getMessage());
        }

        // Inicializar el árbol genealógico
        inicializarArbol();
    }

    public ScrollPane getRoot() {
        return root;
    }

    private void inicializarArbol() {
        nodosVisitados.clear();

        // Calcular la altura y el ancho del árbol
        int altura = calcularAltura(usuarioRaiz); // Niveles hacia abajo (hijos, nietos, etc.)
        int nivelesSuperiores = calcularAlturaHaciaArriba(usuarioRaiz); // Niveles hacia arriba (padres, abuelos, etc.)

        int maxNodosEnNivel = (int) Math.pow(2, altura - 1); // Máximo número de nodos en el último nivel

        // Espaciado entre nodos y niveles
        double espacioVertical = 250; // Espaciado entre niveles
        double espacioHorizontal = 550; // Espaciado mínimo entre nodos

        // Márgenes adicionales
        double margenHorizontal = 200; // Espacio adicional a la izquierda y derecha
        double margenVertical = 100;   // Espacio adicional arriba y abajo

        // Tamaño total del contenedor
        double ancho = maxNodosEnNivel * espacioHorizontal + 2 * margenHorizontal;
        double alto = (altura + nivelesSuperiores) * espacioVertical + 2 * margenVertical;

        // Establecer el tamaño del Pane
        pane.setMinSize(ancho, alto);
        pane.setPrefSize(ancho, alto);

        // Dibujar el nodo raíz centrado
        double startX = ancho / 2;
        double startY = (nivelesSuperiores + 1) * espacioVertical + margenVertical;
        dibujarNodo(usuarioRaiz, startX, startY, "Usuario Raíz");

        // Configurar el ScrollPane
        root.setFitToWidth(false);
        root.setFitToHeight(false);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Centrar la vista en el nodo raíz
        root.setVvalue(0.5);
        root.setHvalue(0.5);
    }
    private Set<String> posicionesOcupadas = new HashSet<>();

    private void dibujarNodo(Usuario usuario, double x, double y, String tipoParentesco) {
        if (nodosMapa.containsKey(usuario) || nodosVisitados.contains(usuario)) {
            return; // Evitar nodos duplicados o bucles infinitos
        }

        nodosVisitados.add(usuario); // Marca el nodo como visitado

        // Crear nodo visual
        Text nombreTexto = new Text(usuario.getNombreCompleto());
        nombreTexto.setFont(new Font("Montserrat", 14));
        nombreTexto.setFill(Color.BLACK);

        Text parentescoTexto = new Text(tipoParentesco);
        parentescoTexto.setFont(new Font("Montserrat", 12));
        parentescoTexto.setFill(Color.GRAY);

        Text fechaTexto = new Text(formatFecha(usuario.getFechaNacimiento(), usuario.getFechaDefuncion()));
        fechaTexto.setFont(new Font("Montserrat", 12));
        fechaTexto.setFill(Color.GRAY);

        double fixedWidth = 200; // Tamaño fijo reducido del nodo
        double fixedHeight = 60;

        Rectangle nodo = new Rectangle(fixedWidth, fixedHeight);
        nodo.setArcWidth(10);
        nodo.setArcHeight(10);
        nodo.setStroke(Color.LIGHTGRAY);
        nodo.setStrokeWidth(1.5);
        nodo.setFill("Usuario Raíz".equals(tipoParentesco) ? Color.web("#FF6F61") : Color.web("#FFDDCA"));

        nodo.setLayoutX(x - nodo.getWidth() / 2);
        nodo.setLayoutY(y - nodo.getHeight() / 2);
        nombreTexto.setLayoutX(x - nombreTexto.getBoundsInLocal().getWidth() / 2);
        nombreTexto.setLayoutY(y - 10);
        parentescoTexto.setLayoutX(x - parentescoTexto.getBoundsInLocal().getWidth() / 2);
        parentescoTexto.setLayoutY(y + 10);
        fechaTexto.setLayoutX(x - fechaTexto.getBoundsInLocal().getWidth() / 2);
        fechaTexto.setLayoutY(y + 25);

        // Agregar el nodo al mapa antes de conexiones
        nodosMapa.put(usuario, nodo);

        // Dibujar conexiones y padres
        double nivelOffset = 150; // Espaciado vertical entre niveles
        double ramaOffset = calcularAnchoRama(usuario) * 300; // Espaciado horizontal dinámico entre ramas

        if (usuario.getPadre() != null) {
            double padreX = x - ramaOffset / 2; // Colocar al padre hacia la izquierda
            double padreY = y - nivelOffset;   // Subir al nivel superior
            dibujarNodo(usuario.getPadre(), padreX, padreY, "Padre");
            dibujarConexionAnguloRecto(nodo, nodosMapa.get(usuario.getPadre()));
        }

        if (usuario.getMadre() != null) {
            double madreX = x + ramaOffset / 2; // Colocar a la madre hacia la derecha
            double madreY = y - nivelOffset;    // Subir al nivel superior
            dibujarNodo(usuario.getMadre(), madreX, madreY, "Madre");
            dibujarConexionAnguloRecto(nodo, nodosMapa.get(usuario.getMadre()));
        }

        // Dibujar hijos
        int numHijos = usuario.getHijos().tamaño();
        if (numHijos > 0) {
            double totalWidth = ramaOffset * (numHijos - 1); // Espacio total necesario para los hijos
            double hijoX = x - totalWidth / 2; // Punto inicial centrado
            double hijoY = y + nivelOffset;   // Bajar al nivel inferior

            for (Usuario hijo : usuario.getHijos()) {
                dibujarNodo(hijo, hijoX, hijoY, "Hijo/Hija");
                dibujarConexionAnguloRecto(nodo, nodosMapa.get(hijo));
                hijoX += ramaOffset; // Mover al siguiente hijo
            }
        }

        // Añadir nodo y textos al contenedor
        pane.getChildren().addAll(nodo, nombreTexto, parentescoTexto, fechaTexto);
    }

    private String generarClavePosicion(double x, double y) {
        // Generar una clave única para una posición x, y
        return Math.round(x) + ":" + Math.round(y);
    }

    private String formatFecha(LocalDate nacimiento, LocalDate defuncion) {
        if (nacimiento == null) {
            return "Fecha desconocida";
        }
        String nacimientoStr = nacimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (defuncion != null) {
            String defuncionStr = defuncion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return nacimientoStr + " - " + defuncionStr;
        }
        return nacimientoStr;
    }

    private void dibujarConexionAnguloRecto(Rectangle nodoInicio, Rectangle nodoFin) {
        if (nodoInicio == null || nodoFin == null) {
            System.err.println("No se pudo dibujar la conexión: nodoInicio o nodoFin es null.");
            return;
        }

        double startX = nodoInicio.getLayoutX() + nodoInicio.getWidth() / 2;
        double startY = nodoInicio.getLayoutY() + nodoInicio.getHeight() / 2;
        double endX = nodoFin.getLayoutX() + nodoFin.getWidth() / 2;
        double endY = nodoFin.getLayoutY() + nodoFin.getHeight() / 2;

        double midY = (startY + endY) / 2;

        Line lineaVertical1 = new Line(startX, startY, startX, midY);
        Line lineaHorizontal = new Line(startX, midY, endX, midY);
        Line lineaVertical2 = new Line(endX, midY, endX, endY);

        lineaVertical1.setStroke(Color.web("#FFB7A2"));
        lineaHorizontal.setStroke(Color.web("#FFB7A2"));
        lineaVertical2.setStroke(Color.web("#FFB7A2"));
        lineaVertical1.setStrokeWidth(1.5);
        lineaHorizontal.setStrokeWidth(1.5);
        lineaVertical2.setStrokeWidth(1.5);

        pane.getChildren().add(0, lineaVertical1);
        pane.getChildren().add(0, lineaHorizontal);
        pane.getChildren().add(0, lineaVertical2);
    }

    private void ajustarTamañoContenedor(double x, double y) {
        double padding = 200;
        if (x + padding > pane.getPrefWidth()) {
            pane.setPrefWidth(x + padding);
        }
        if (y + padding > pane.getPrefHeight()) {
            pane.setPrefHeight(y + padding);
        }
    }

    private int calcularAnchoRama(Usuario usuario) {
        if (usuario == null) {
            return 0;
        }

        // Ancho de la rama es el máximo entre las ramas del padre y la madre
        int anchoPadre = calcularAnchoRama(usuario.getPadre());
        int anchoMadre = calcularAnchoRama(usuario.getMadre());

        // Si no hay padres, el ancho mínimo es 1 (este nodo)
        return Math.max(anchoPadre + anchoMadre, 1);
    }


    private int calcularAltura(Usuario usuario) {
        if (usuario == null) {
            return 0;
        }

        // Calcular altura de padres
        int alturaPadre = calcularAltura(usuario.getPadre());
        int alturaMadre = calcularAltura(usuario.getMadre());

        // Retornar el nivel más alto de los padres + 1 (para el nivel actual)
        return Math.max(alturaPadre, alturaMadre) + 1;
    }

    private int calcularAlturaHaciaArriba(Usuario usuario) {
        if (usuario == null) {
            return 0;
        }

        // Calcular la altura hacia arriba (nivel más profundo entre padre y madre)
        int alturaPadre = usuario.getPadre() != null ? calcularAlturaHaciaArriba(usuario.getPadre()) : 0;
        int alturaMadre = usuario.getMadre() != null ? calcularAlturaHaciaArriba(usuario.getMadre()) : 0;

        return Math.max(alturaPadre, alturaMadre) + 1;
    }


}