/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package droneraio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Gustavo
 */
public class AcharCaminho extends JFrame{
    
    ArrayList<Ponto> pontos;
    private double auxDraw = 0.05;
    private Caminho cam = new Caminho();
    private double maxx = Double.MIN_VALUE;
    private double maxy = Double.MIN_VALUE;
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.blue);
        //Desenha os círculos e os pontos no centro dos mesmos
        this.pontos.forEach((p) -> {
            double x = p.px-(p.getRaio());
            double y = p.py-(p.getRaio());
            Shape circulo = new Ellipse2D.Double(x * auxDraw, y * auxDraw, p.getRaio() * 2 * auxDraw, p.getRaio() * 2 * auxDraw);
            g2d.draw(circulo);
            Shape ponto = new Line2D.Double(p.px * auxDraw, p.py * auxDraw, p.px * auxDraw, p.py * auxDraw);
            g2d.draw(ponto);
        });
        //Traça a linha do caminho 
        for(int i = 0; i < cam.size() - 1; i++){
            g2d.setColor(Color.red);
            Ponto p1 = cam.caminho.get(i);
            Ponto p2 = cam.caminho.get(i + 1);
            Shape linha = new Line2D.Double(p1.px * auxDraw, p1.py * auxDraw, p2.px * auxDraw, p2.py * auxDraw);
            g2d.draw(linha);
        }
        Ponto p1 = cam.caminho.get(cam.size() - 1);
        Ponto p2 = cam.caminho.get(0);
        Shape linha = new Line2D.Double(p1.px * auxDraw, p1.py * auxDraw, p2.px * auxDraw, p2.py * auxDraw);
        g2d.draw(linha);
    }
    
    public AcharCaminho(ArrayList<Ponto> pontos){
        this.setSize(900, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setBackground(Color.WHITE);
        this.pontos = pontos;
        this.acharLimites();
        double tempoInicial = System.currentTimeMillis();
        melhoresIniciais();
        //Depois de selecionar os dois pontos mais próximos, essa parte do algoritmo
        //irá procurando os próximos pontos também através da proximidade com o último adicionado
        //Com isso será gerada uma solução inicial
        while(this.cam.size() < this.pontos.size()){
            Ponto p1 = this.cam.ultimo();
            double menorDist = Double.MAX_VALUE;
            Ponto menorPonto = null;
            for(int i = 0; i < this.pontos.size(); i++){
                Ponto p2 = this.pontos.get(i);
                if(!p2.equals(p1) && !this.cam.contains(p2)){
                    if(distancia(p1,p2) < menorDist){
                        menorDist = distancia(p1,p2);
                        menorPonto = p2;
                    }
                }
            }
            this.cam.addPonto(menorPonto);
        }
        this.repaint();
        this.MelhorCaminho();
        System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial));
        this.setVisible(true);
    }
    
    //Usa a aleatoriedade pra melhorar o caminho percorrido
    public void MelhorCaminho(){
        
        
        Random gen = new Random();
        for(int i = 0; i < 100000; i++){
            boolean repaint = true;
            Ponto p1 = this.cam.caminho.get(gen.nextInt(this.cam.caminho.size()));      

            double x = 0;
            double y = 0;
            Ponto np = null;

            do{
                x = gen.nextDouble() * (this.maxx);
                y = gen.nextDouble() * (this.maxy); 
                np = new Ponto(x, y, p1.raio);
            }while(!verificaPonto(np));
            
            double antiDist = this.cam.somaDistancia();
            this.cam.subPonto(p1, np);
            this.verificaVisitados();
            
            if(this.cam.somaDistancia() > antiDist || !this.todosVisitados()){
                this.cam.subPonto(np, p1);
                this.verificaVisitados();
                repaint = false;
            }
            
            if(repaint){
                this.repaint();
//                try {
//                    TimeUnit.MILLISECONDS.sleep(400);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(AcharCaminho.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }  
        }
        System.out.println(this.cam.somaDistancia());
    }
    
    //Seleciona os dois pontos mais próximos entre si pra iniciar a primeira solução
    public void melhoresIniciais(){
        
        double menorDist = Double.MAX_VALUE;
        
        Ponto menorp1 = null;
        Ponto menorp2 = null;
        
        for(int i = 0; i < this.pontos.size() - 1; i++){
            for(int j = i + 1; j < this.pontos.size(); j++){
                Ponto p1 = pontos.get(i);
                Ponto p2 = pontos.get(j);
                double dist = distancia(p1, p2);
                if(dist < menorDist){
                    menorDist = dist;
                    menorp1 = p1;
                    menorp2 = p2;
                }
            }
        }
        
        this.cam.addPonto(menorp1);
        this.cam.addPonto(menorp2);
    }
    
    public double distancia(Ponto p1, Ponto p2){
        return  Math.abs(Math.sqrt(Math.pow((p2.px - p1.px), 2) + Math.pow((p2.py - p1.py), 2)));
    }
    
    //Verifica quais pontos foram visitados
    public void verificaVisitados(){
        this.pontos.forEach((p1) ->{
            boolean visitado = false;
            for(Ponto p2 : this.cam.caminho){
                if(distancia(p1,p2) <= p1.getRaio()){
                    visitado = true;
                    break;
                }
            }
            if(visitado){
                p1.visitou();
            }else{
                p1.naoVisitou();
            }
        });
    }
    
    //Verifica se todos os pontos foram visitados
    public boolean todosVisitados(){
        for(Ponto p : this.pontos){
            if(!p.visitado){
                return false;
            }
        }
        return true;
    }
    
    //Verifica se um determinado ponto criado faz parte do raio de um dos pontos pré-definidos
    public boolean verificaPonto(Ponto p1){
        for(Ponto p2 : this.pontos){
            if(distancia(p1,p2) <= p2.getRaio()){
                //System.out.println("O ponto ( " + p1.px + "," + p1.py + ") Pertence ao ponto ( " + p2.px + "," + p2.py + ")");
                return true;
            }
        }
        
        //System.out.println("Não pertence a nenhum");
        return false;
    }
    
    public void acharLimites(){
        Ponto maiory = null;
        Ponto maiorx = null;
        for(Ponto p : this.pontos){
            if(p.px > this.maxx){
                this.maxx = p.px;
                maiorx = p;
            }
            if(p.py > this.maxy){
                this.maxy = p.py;
                maiory = p;
            }
        }
        
        this.maxx += maiorx.getRaio();
        this.maxy += maiory.getRaio();
        
    }
    
}
