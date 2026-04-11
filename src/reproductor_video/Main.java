/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reproductor_video;

import javafx.embed.swing.JFXPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author mafer
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    new JFXPanel();
    SwingUtilities.invokeLater(() -> new Reproductor().setVisible(true));
}
    
}
