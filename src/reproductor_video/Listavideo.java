package reproductor_video;

public class Listavideo {
    Nodo inicio;
    Nodo fin;

    public void agregar(String ruta) {
        Nodo nuevo = new Nodo(ruta);

        if (inicio == null) {
            inicio = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            nuevo.anterior = fin;
            fin = nuevo;
        }
    }
}