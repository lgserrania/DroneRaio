/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package droneraio;

import java.util.LinkedList;

/**
 *
 * @author Gustavo
 */
public class Caminho {
    
    public LinkedList<Ponto> caminho;
    
    public Caminho(){
        caminho = new LinkedList<>();
    }
    
    public void addPonto(Ponto p){
        caminho.add(p);
    }
    
    //Substitui um ponto por outro em uma posição
    public void subPonto(Ponto p1, Ponto p2){
        int pos1 = caminho.indexOf(p1);
        caminho.set(pos1, p2);
    }
    
    //Soma a distância total do caminho
    public double somaDistancia(){
        double total = 0;
        double dist = 0;
        
        for(int i = 0; i < this.caminho.size() - 1; i++){
            Ponto p1 = this.caminho.get(i);
            Ponto p2 = this.caminho.get(i + 1);
            
            dist = Math.sqrt(Math.pow((p2.px - p1.px), 2) + Math.pow((p2.py - p1.py), 2));
            total += dist;
        }
        
        Ponto p1 = this.caminho.get(this.caminho.size() - 1);
        Ponto p2 = this.caminho.get(0);
        dist = Math.sqrt(Math.pow((p2.px - p1.px), 2) + Math.pow((p2.py - p1.py), 2));
        total += dist;
        
        return total;
    }
    
    public int size(){
        return caminho.size();
    }
    
    //Retorna o último ponto da lista
    public Ponto ultimo(){
        return caminho.get(caminho.size() - 1);
    }
    
    //Verifica se o caminho contém um determinado ponto
    public boolean contains(Ponto p1){
        for(Ponto p2 : this.caminho){
            if(p2.equals(p1)){
                return true;
            }
        }
        return false;
    }
    
}
