/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package droneraio;

import javax.swing.JFrame;

/**
 *
 * @author Gustavo
 */
public class DroneRaio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        CarregarArquivo c = new CarregarArquivo("instancia_drone_1000.csv");
        JFrame frame = new AcharCaminho(c.getPontos());  
    }
    
}
