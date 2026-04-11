/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reproductor_video;

/**
 *
 * @author mafer
 */
public class Listavideo {
    private Nodo PTR;
    private Nodo actual;
    
    public void agregar(String ruta){
        Nodo nuevo = new Nodo(ruta);
        if(PTR == null){
            PTR = nuevo;
            actual = nuevo;
        }else{
            Nodo temp = PTR;
            while(temp.siguiente != null){
                temp = temp.siguiente;
            }
            temp.siguiente = nuevo;
            nuevo.anterior = temp;
        }
    }
    public Nodo getActual(){
        return actual;
    }
    public void siguiente(){
        if(actual != null && actual.siguiente!= null){
            actual = actual.siguiente;
        }
    }
    public void anterior(){
          if(actual != null && actual.anterior!= null){
            actual = actual.anterior;
        }
    }
}
