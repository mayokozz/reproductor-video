package reproductor_video;
public class Nodo {
 
    String ruta;    // Ruta absoluta del archivo de video
    String nombre;  // Nombre limpio para mostrar en la playlist
    Nodo siguiente;
    Nodo anterior;
 
    public Nodo(String ruta) {
        this.ruta      = ruta;
        this.nombre    = extraerNombre(ruta);
        this.siguiente = null;
        this.anterior  = null;
    }
 
    // Extrae el nombre del archivo sin ruta ni extensión
    // Ej: "C:/Videos/pelicula.mp4" → "pelicula"
    private String extraerNombre(String ruta) {
        String nom = new java.io.File(ruta).getName();
        int punto  = nom.lastIndexOf('.');
        return punto > 0 ? nom.substring(0, punto) : nom;
    }
 
    @Override
    public String toString() {
        return nombre;
    }
}