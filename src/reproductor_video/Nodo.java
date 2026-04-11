/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reproductor_video;

/**
 *
 * @author mafer
 */
public class Nodo {
    String rutaVideo;
    Nodo siguiente;
    Nodo anterior;

    public Nodo(String rutaVideo) {
        this.rutaVideo = rutaVideo;
        this.siguiente = null;
        this.anterior = null;
    }
    
}
