package Ubicua;

import java.util.*;
import java.util.concurrent.*;
import java.math.*;

public class Datos {
    private Farola farolas[];
    private int matrizAdyacencia[][] = {{0,1,1,2,2,-1,-1,1,-1,-1},
                                        {1,0,-1,-1,-1,1,2,-1,-1,-1},
                                        {1,-1,0,1,-1,2,-1,-1,2,1},
                                        {2,-1,1,0,2,-1,1,-1,-1,-1},
                                        {2,-1,-1,2,0,-1,-1,-1,1,2},
                                        {-1,1,2,-1,-1,0,2,1,-1,-1},
                                        {-1,2,-1,1,-1,2,0,-1,1,1},
                                        {1,-1,-1,-1,-1,1,-1,0,1,-1},
                                        {-1,-1,2,-1,1,-1,1,1,0,-1},
                                        {-1,-1,1,-1,2,-1,1,-1,-1,0}};
    private int n_farolas = matrizAdyacencia.length;
    private float arraylight[] = new float[n_farolas];
    private int margenActivacionLdr = 900; //ldr>900 activacion
    private float activacionCerca = 0.7f;
    private float activacionLejos = 0.4f;

    public Datos(){
        farolas = new Farola[n_farolas];
        for (int i=0; i<n_farolas; i++){
            farolas[i] = new Farola(i);
        }
    }

    public void anadirDato(int posArrayHora, int id, float light, float movement){
        farolas[id].anadir(posArrayHora, light, movement);
    }

    public float actualizarSiempre(int id, float ldr, float movement, CyclicBarrier barreraHilo){
        try{
            setZeroArraylight(id); //Se pone a 0 el elemento de arraylight indicado para fijar a 0 todas sus posiciones
            barreraHilo.await();
            if(movement==1){
                modifyArraylight(id);
            }
            barreraHilo.await();
            if(ldr>margenActivacionLdr){
                return getArraylight()[id];
            }else{
                return 0f;
            }
        }catch(Exception e){
            System.out.println("Problema en actualizarSiempre");
            System.out.println(e.toString());
            return -1f;
        }
    }

    public float actualizar(int posArrayHora, int id, float ldr, float movement){
        return 0.1f; //Sin hacer
    }

    // revisar por el synchronized
    private synchronized void setZeroArraylight(int id){
        arraylight[id] = 0;
    }

    private synchronized void modifyArraylight(int id){
        int[] row = getMatrizAdyacencia()[id];
        for(int i=0; i<n_farolas; i++){
            switch(row[i]){
                case 0:
                    arraylight[i]=1;
                case 1:
                    arraylight[i]=Math.max(arraylight[i],activacionCerca);
                case 2:
                    arraylight[i]=Math.max(arraylight[i],activacionLejos);
            }
        }
    }

    private synchronized float[] getArraylight() {
        return arraylight;
    }

    public synchronized int[][] getMatrizAdyacencia(){
        return matrizAdyacencia;
    }
}