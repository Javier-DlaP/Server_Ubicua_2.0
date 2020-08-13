package Ubicua;

import java.util.*;

public class Farola {
    private ArrayList<Float> aux_luces;
    private ArrayList<Integer> aux_sensores;
    private float luces[];
    private float sensores[];
    private int id;
    private int intensidades[] = new int[48]; //intensidades del día para la farola

    public Farola(int id){
        aux_luces = new ArrayList<>();
        aux_sensores = new ArrayList<>();
        luces = new float[48];
        sensores = new float[48];
        Arrays.fill(luces, 0);
        Arrays.fill(sensores, 0);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float[] getLuces() {
        return luces;
    }

    public float[] getSensores() {
        return sensores;
    }

    public void anadir(int posArrayHora, int movement, int ldr){
        aux_luces.add((((float) ldr)+(100/899))/(1/899)); //Pasa de valores entre 100-999 a 0-1 siguiendo una distribución lineal
        aux_sensores.add(movement);
    }

    public void cambiarLuz(int posArrayHora, float light) {
        luces[posArrayHora] = light;
    }

    public void setIntensidad(int pos, int intensidad){
        intensidades[pos] = intensidad;
    }

    public int[] getIntensidades(){
        return intensidades;
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
        aux_luces = new ArrayList<>();
        aux_sensores = new ArrayList<>();
    }
}