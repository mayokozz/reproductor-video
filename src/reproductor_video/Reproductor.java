/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reproductor_video;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

/**
 *
 * @author mafer
 */
public class Reproductor extends Application {
     private Listavideo lista = new Listavideo();
    private Nodo actual;

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    @Override
    public void start(Stage stage) {

        // 📂 Videos
        lista.agregar("C:/videos/video1.mp4");
        lista.agregar("C:/videos/video2.mp4");
        lista.agregar("C:/videos/video3.mp4");

        actual = lista.inicio;

        mediaView = new MediaView();

        reproducir();

        // 🎮 Botones
        Button btnInicio = new Button("⏮");
        Button btnAnterior = new Button("⏪");
        Button btnPausa = new Button("⏯");
        Button btnSiguiente = new Button("⏩");
        Button btnFinal = new Button("⏭");

        // 🔘 Acciones
        btnInicio.setOnAction(e -> {
            actual = lista.inicio;
            reproducir();
        });

        btnFinal.setOnAction(e -> {
            actual = lista.fin;
            reproducir();
        });

        btnAnterior.setOnAction(e -> {
            if (actual.anterior != null) {
                actual = actual.anterior;
                reproducir();
            }
        });

        btnSiguiente.setOnAction(e -> {
            if (actual.siguiente != null) {
                actual = actual.siguiente;
                reproducir();
            }
        });

        btnPausa.setOnAction(e -> {
            if (mediaPlayer != null) {
                MediaPlayer.Status estado = mediaPlayer.getStatus();

                if (estado == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.play();
                }
            }
        });

        // 🎨 Layout
        HBox controles = new HBox(10, btnInicio, btnAnterior, btnPausa, btnSiguiente, btnFinal);
        controles.setStyle("-fx-alignment: center; -fx-padding: 10;");

        BorderPane root = new BorderPane();
        root.setCenter(mediaView);
        root.setBottom(controles);

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Reproductor con Lista Enlazada");
        stage.setScene(scene);
        stage.show();
    }

    private void reproducir() {
        if (actual != null) {

            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            String ruta = new File(actual.ruta).toURI().toString();
            Media media = new Media(ruta);

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.play();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
