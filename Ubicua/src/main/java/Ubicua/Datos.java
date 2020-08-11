package Ubicua;

import java.util.*;
import java.util.concurrent.*;
import java.math.*;
import java.sql.SQLException;

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
    private float arraylight[] = new float[n_farolas]; //intensidades de farolas del dia
    private int margenActivacionLdr = 900; //ldr>900 activacion
    private float activacionCerca = 0.7f;
    private float activacionLejos = 0.4f;
    private Database database = new Database();

    public Datos(){
        farolas = new Farola[n_farolas];
        for (int i=0; i<n_farolas; i++){
            farolas[i] = new Farola(i);
        }
    }

    public void anadirDato(int posArrayHora, int id, float movement){
        farolas[id].anadir(posArrayHora, movement);
    }

    public void actualizarFarolas(int posArrayHora, int idFarola, int ldr) throws SQLException {
        if (ldr > margenActivacionLdr) {
            //Activar
        } else {
            Float valorMedia = database.selectDatosMediaLuz(idFarola).get(posArrayHora);
            
            Float valorMaximoMatriz = 0F;
            for (int i = 0; i < n_farolas; i++) {
                int valorMatriz = matrizAdyacencia[i][idFarola];
                if (valorMatriz != -1) {
                    float valorFarolaMatriz = 0F;
                    switch (valorMatriz) {
                        // Recoger primero la luz de la farola i en la actualizacion anterior
                        // Disminuir x valor de valorFarolaMatriz dependiendo del caso
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    }
                    if (valorFarolaMatriz > valorMaximoMatriz) valorMaximoMatriz = valorFarolaMatriz;
                }
            }

            // Decidir cual de los dos (valorMedia o valorMaximoMatriz) es mayor y almacenarlo en el array de farolas
        }
    }

    public float actualizarSiempre(int id, float ldr, float movement){
        try{
            setZeroArraylight(id); //Se pone a 0 el elemento de arraylight indicado para fijar a 0 todas sus posiciones
            if(movement==1){
                modifyArraylight(id);
            }
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