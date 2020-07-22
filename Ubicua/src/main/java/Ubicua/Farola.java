package Ubicua;

import java.util.*;

public class Farola {
    private int posArrayHoraActual = -1;
    private ArrayList<Float> aux_luces[];
    private ArrayList<Float> aux_sensores[];
    private float luces[];
    private float sensores[];
    private int id;

    public Farola(int id){
        aux_luces = new ArrayList[48];
        aux_sensores = new ArrayList[48];
        luces = new float[48];
        sensores = new float[48];
        this.id = id;
    }

    public void anadir(int posArrayHora, float light, float movement){
        if(posArrayHoraActual==-1){
            posArrayHoraActual=posArrayHora;
        }else{
            if(posArrayHoraActual!=posArrayHora){
                //Crear hilo para hacer medias
            }
        }
        aux_luces[posArrayHora].add(light);
        aux_sensores[posArrayHora].add(movement);
    }
}