package reproductor_video;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Reproductor extends JFrame {

    private final Listavideo lista = new Listavideo();
    private Nodo             actual;
    private MediaPlayer      mediaPlayer;

    private JFXPanel  jfxPanel;
    private MediaView mediaView;

    private JButton btnAgregar;
    private JButton btnPrimero, btnAnterior, btnPlay, btnPause, btnStop, btnSiguiente, btnUltimo;
    private JSlider sliderTiempo, sliderVolumen;
    private JLabel  lblTitulo, lblTiempo, lblVolumen, lblEstado;

    private DefaultListModel<String> modeloLista;
    private JList<String>            listaUI;

    public Reproductor() {
        setTitle("Reproductor de Video");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        new JFXPanel();
        initUI();
        cargarVideosIniciales();
    }

    private void initUI() {
        getContentPane().setBackground(new Color(25, 25, 25));
        setLayout(new BorderLayout(6, 6));
        add(panelVideo(),     BorderLayout.CENTER);
        add(panelLista(),     BorderLayout.EAST);
        add(panelControles(), BorderLayout.SOUTH);
    }

    private JLayeredPane panelVideo() {
        jfxPanel = new JFXPanel();
        jfxPanel.setBackground(Color.BLACK);
        jfxPanel.setBounds(0, 0, 660, 400);

        lblTitulo = new JLabel("  Sin video  ");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(0, 0, 0, 170));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        lblTitulo.setBounds(0, 370, 660, 30);

        JLayeredPane layered = new JLayeredPane() {
            @Override public Dimension getPreferredSize() { return new Dimension(660, 400); }
        };
        layered.add(jfxPanel,  JLayeredPane.DEFAULT_LAYER);
        layered.add(lblTitulo, JLayeredPane.PALETTE_LAYER);
        return layered;
    }

    private JPanel panelLista() {
        JPanel panel = new JPanel(new BorderLayout(4, 6));
        panel.setBackground(new Color(38, 38, 38));
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 8));

        JLabel titulo = new JLabel("Lista de videos", SwingConstants.CENTER);
        titulo.setForeground(new Color(160, 160, 160));
        titulo.setFont(new Font("Arial", Font.BOLD, 12));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panel.add(titulo, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        listaUI = new JList<>(modeloLista);
        listaUI.setBackground(new Color(48, 48, 48));
        listaUI.setForeground(Color.WHITE);
        listaUI.setSelectionBackground(new Color(60, 120, 180));
        listaUI.setFont(new Font("Arial", Font.PLAIN, 11));
        listaUI.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v,
                    int i, boolean sel, boolean focus) {
                super.getListCellRendererComponent(l, v, i, sel, focus);
                setBackground(sel ? new Color(60, 120, 180)
                        : i % 2 == 0 ? new Color(48, 48, 48) : new Color(42, 42, 42));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                return this;
            }
        });
        listaUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    reproducirDesdeIndice(listaUI.getSelectedIndex());
            }
        });
        panel.add(new JScrollPane(listaUI), BorderLayout.CENTER);

        btnAgregar = boton("+ Agregar video", new Color(45, 110, 45));
        btnAgregar.addActionListener(e -> agregarVideo());
        panel.add(btnAgregar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelControles() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 10, 10));

        JPanel panelSlider = new JPanel(new BorderLayout(6, 0));
        panelSlider.setOpaque(false);
        sliderTiempo = slider(0, 1000, 0);
        sliderTiempo.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent e) { buscarEnTiempo(); }
        });
        lblTiempo = etiqueta("0:00 / 0:00");
        panelSlider.add(sliderTiempo, BorderLayout.CENTER);
        panelSlider.add(lblTiempo,    BorderLayout.EAST);
        panel.add(panelSlider, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        panelBotones.setOpaque(false);

        btnPrimero   = boton("|<< Primero",  new Color(50, 50, 80));
        btnAnterior  = boton("<<  Anterior", new Color(50, 50, 80));
        btnPlay      = boton(">  Play",      new Color(35, 100, 35));
        btnPause     = boton("|| Pausa",     new Color(100, 90, 15));
        btnStop      = boton("[]  Stop",     new Color(110, 30, 30));
        btnSiguiente = boton("Siguiente >>", new Color(50, 50, 80));
        btnUltimo    = boton("Ultimo  >>|",  new Color(50, 50, 80));

        btnPrimero  .addActionListener(e -> irAlPrimero());
        btnAnterior .addActionListener(e -> anterior());
        btnPlay     .addActionListener(e -> play());
        btnPause    .addActionListener(e -> pausa());
        btnStop     .addActionListener(e -> stop());
        btnSiguiente.addActionListener(e -> siguiente());
        btnUltimo   .addActionListener(e -> irAlUltimo());

        panelBotones.add(btnPrimero);
        panelBotones.add(btnAnterior);
        panelBotones.add(btnPlay);
        panelBotones.add(btnPause);
        panelBotones.add(btnStop);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnUltimo);

        JPanel panelVol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
        panelVol.setOpaque(false);
        lblVolumen = etiqueta("Vol:");
        sliderVolumen = slider(0, 100, 80);
        sliderVolumen.setPreferredSize(new Dimension(90, 20));
        sliderVolumen.addChangeListener(e -> ajustarVolumen());
        panelVol.add(lblVolumen);
        panelVol.add(sliderVolumen);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(panelBotones, BorderLayout.CENTER);
        centro.add(panelVol,     BorderLayout.EAST);
        panel.add(centro, BorderLayout.CENTER);

        lblEstado = new JLabel("Listo", SwingConstants.LEFT);
        lblEstado.setForeground(new Color(110, 110, 110));
        lblEstado.setFont(new Font("Arial", Font.ITALIC, 10));
        panel.add(lblEstado, BorderLayout.SOUTH);
        return panel;
    }

    private JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 11));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(fondo.brighter()); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(fondo); }
        });
        return b;
    }

    private JSlider slider(int min, int max, int val) {
        JSlider s = new JSlider(min, max, val);
        s.setOpaque(false);
        s.setForeground(new Color(60, 120, 180));
        return s;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(new Color(170, 170, 170));
        l.setFont(new Font("Arial", Font.PLAIN, 11));
        return l;
    }

    // ── Lógica ────────────────────────────────────────────────────

    private void agregarVideo() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter(
                "Videos (mp4, avi, mkv, mov)", "mp4", "avi", "mkv", "mov"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File f : fc.getSelectedFiles()) {
                lista.agregar(f.getAbsolutePath());
                modeloLista.addElement(f.getName());
            }
            if (actual == null && lista.inicio != null) {
                actual = lista.inicio;
                listaUI.setSelectedIndex(0);
                cargarVideo(actual.ruta);
            }
            lblEstado.setText(modeloLista.size() + " video(s) en lista");
        }
    }

    private void cargarVideo(String ruta) {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            try {
                String uri   = new File(ruta).toURI().toString();
                Media  media = new Media(uri);
                mediaPlayer  = new MediaPlayer(media);
                mediaView    = new MediaView(mediaPlayer);
                mediaView.setFitWidth(660);
                mediaView.setFitHeight(400);
                mediaView.setPreserveRatio(true);

                BorderPane root = new BorderPane(mediaView);
                root.setStyle("-fx-background-color: black;");
                jfxPanel.setScene(new Scene(root, 660, 400));

                mediaPlayer.setVolume(sliderVolumen.getValue() / 100.0);

                mediaPlayer.currentTimeProperty().addListener((obs, ant, nuevo) -> {
                    if (!sliderTiempo.getValueIsAdjusting()) {
                        double total = mediaPlayer.getTotalDuration().toSeconds();
                        double curr  = nuevo.toSeconds();
                        SwingUtilities.invokeLater(() -> {
                            sliderTiempo.setValue((int)(curr / total * 1000));
                            lblTiempo.setText(formatTiempo(curr) + " / " + formatTiempo(total));
                        });
                    }
                });

                mediaPlayer.setOnEndOfMedia(this::siguiente);
                mediaPlayer.play();

                String nombre = new File(ruta).getName();
                SwingUtilities.invokeLater(() -> {
                    lblTitulo.setText("  " + nombre + "  ");
                    lblEstado.setText("Reproduciendo: " + nombre);
                    listaUI.setSelectedIndex(indiceActual());
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this,
                        "No se pudo cargar:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        });
    }

    private void reproducirDesdeIndice(int indice) {
        if (indice < 0) return;
        Nodo cursor = lista.inicio;
        for (int i = 0; i < indice && cursor != null; i++) cursor = cursor.siguiente;
        if (cursor != null) { actual = cursor; cargarVideo(actual.ruta); }
    }

    private void play() {
        if (mediaPlayer == null) { lblEstado.setText("Agrega un video primero"); return; }
        Platform.runLater(() -> mediaPlayer.play());
        lblEstado.setText("Reproduciendo");
    }

    private void pausa() {
        if (mediaPlayer == null) return;
        Platform.runLater(() -> mediaPlayer.pause());
        lblEstado.setText("Pausado");
    }

    private void stop() {
        if (mediaPlayer == null) return;
        Platform.runLater(() -> mediaPlayer.stop());
        sliderTiempo.setValue(0);
        lblTiempo.setText("0:00 / 0:00");
        lblEstado.setText("Detenido");
    }

    private void siguiente() {
        if (actual == null) return;
        if (actual.siguiente != null) {
            actual = actual.siguiente;
            cargarVideo(actual.ruta);
        } else {
            SwingUtilities.invokeLater(() -> lblEstado.setText("Ya estas en el ultimo video"));
        }
    }

    private void anterior() {
        if (actual == null) return;
        if (actual.anterior != null) {
            actual = actual.anterior;
            cargarVideo(actual.ruta);
        } else {
            SwingUtilities.invokeLater(() -> lblEstado.setText("Ya estas en el primer video"));
        }
    }

    // Salta directamente al nodo inicio de la lista
    private void irAlPrimero() {
        if (lista.inicio == null) { lblEstado.setText("No hay videos"); return; }
        actual = lista.inicio;
        cargarVideo(actual.ruta);
    }

    // Salta directamente al nodo fin de la lista
    private void irAlUltimo() {
        if (lista.fin == null) { lblEstado.setText("No hay videos"); return; }
        actual = lista.fin;
        cargarVideo(actual.ruta);
    }

    private void buscarEnTiempo() {
        if (mediaPlayer == null) return;
        Platform.runLater(() -> {
            double total   = mediaPlayer.getTotalDuration().toSeconds();
            double destino = total * sliderTiempo.getValue() / 1000.0;
            mediaPlayer.seek(javafx.util.Duration.seconds(destino));
        });
    }

    private void ajustarVolumen() {
        if (mediaPlayer == null) return;
        Platform.runLater(() -> mediaPlayer.setVolume(sliderVolumen.getValue() / 100.0));
    }

    private int indiceActual() {
        int i = 0;
        Nodo c = lista.inicio;
        while (c != null && c != actual) { c = c.siguiente; i++; }
        return c == actual ? i : -1;
    }

    private String formatTiempo(double segundos) {
        int m = (int) segundos / 60;
        int s = (int) segundos % 60;
        return String.format("%d:%02d", m, s);
    }

    private void cargarVideosIniciales() {
    // Obtiene la ruta de la carpeta videos relativa al proyecto
    String base = System.getProperty("user.dir") + File.separator + "videos";
    File carpeta = new File(base);
    
    if (!carpeta.exists()) {
        lblEstado.setText("Carpeta 'videos' no encontrada");
        return;
    }
    
    // Filtra solo archivos de video
    File[] archivos = carpeta.listFiles((dir, nombre) ->
        nombre.endsWith(".mp4") || nombre.endsWith(".avi") ||
        nombre.endsWith(".mkv") || nombre.endsWith(".mov")
    );
    
    if (archivos == null || archivos.length == 0) {
        lblEstado.setText("No hay videos en la carpeta 'videos'");
        return;
    }
    
    // Recorre los archivos y los agrega a la lista enlazada
    for (File f : archivos) {
        lista.agregar(f.getAbsolutePath());
        modeloLista.addElement(f.getName());
    }
    
    // Carga el primero automáticamente
    actual = lista.inicio;
    listaUI.setSelectedIndex(0);
    cargarVideo(actual.ruta);
    lblEstado.setText(modeloLista.size() + " video(s) cargados");
}
}