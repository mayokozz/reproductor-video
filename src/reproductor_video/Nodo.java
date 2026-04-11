package reproductor_video;

public class Nodo {
    String ruta;
    Nodo siguiente;
    Nodo anterior;

    public Nodo(String ruta) {
        this.ruta = ruta;
        this.siguiente = null;
        this.anterior = null;
    }
}