package Ubicua;

import java.util.*;

public class Farola {
    private int posArrayHoraActual = -1;
    private ArrayList<Integer> aux_luces;
    private ArrayList<Integer> aux_sensores;
    private float luces[];
    private float sensores[];
    private int id;
    private int ldr;

    public Farola(int id){
        aux_luces = new ArrayList<>();
        aux_sensores = new ArrayList<>();
        luces = new float[48];
        sensores = new float[48];
        Arrays.fill(luces, 0);
        Arrays.fill(sensores, 0);
        this.id = id;
    }

    public void anadir(int posArrayHora, int movement, int ldr, boolean hacerMedia){
        if (hacerMedia) calcularMedias(posArrayHora);
        
        aux_luces.add(ldr);
        aux_sensores.add(movement);
    }

    public void cambiarLuz(int posArrayHora, float light) {
        luces[posArrayHora] = light;
    }

    public void calcularMedias(int pos) {
        float sumaAuxLuces = 0;
        float sumaAuxSensores = 0;

        for (int i = 0; i < aux_luces.size(); i++) {
            sumaAuxLuces += aux_luces.get(i);
            sumaAuxSensores += aux_sensores.get(i);
        }

        luces[pos] = sumaAuxLuces / aux_luces.size();
        sensores[pos] = sumaAuxSensores / aux_sensores.size();
    }
}