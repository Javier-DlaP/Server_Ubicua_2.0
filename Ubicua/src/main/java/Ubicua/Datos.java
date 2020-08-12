package Ubicua;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.math.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
    private int temp_23_30[] = new int[n_farolas]; //valor de intensidad temporal
    private int margenActivacionLdr = 500; //ldr>500 activacion
    private float activacionCerca = 0.7f;
    private float activacionLejos = 0.4f;
    private Database database = new Database();
    private int idHora = 0;
    private ReentrantLock lock_intensidad = new ReentrantLock();
    private ReentrantLock lock_medias = new ReentrantLock();

    public Datos(){
        farolas = new Farola[n_farolas];
        for (int i=0; i<n_farolas; i++){
            farolas[i] = new Farola(i);
        }
    }

    public Farola[] getFarolas() {
        lock_medias.lock();
        lock_intensidad.lock();
        Farola[] farolas = this.farolas;
        lock_medias.unlock();
        lock_intensidad.unlock();
        return farolas;
    }

    public synchronized int getIdHora(){
        return idHora;
    }

    public synchronized void cambiarIdHora() {
        LocalDateTime now = LocalDateTime.now();
        int hora = now.getHour();
        int minuto = now.getMinute();
        idHora = hora*2 + minuto/30;
    }

    public void anadirDato(int posArrayHora, int id, int movement, int ldr){
        lock_medias.lock();
        farolas[id].anadir(posArrayHora, movement, ldr);
        lock_medias.unlock();
    }

    public void calcularMedias(int posArrayHora){
        lock_medias.lock();
        for(int i=0; i<farolas.length; i++){
            farolas[i].calcularMedias(posArrayHora);
        }
        lock_medias.unlock();
    }

    public int getIntensidad(int idFarola, int posArrayHora){
        lock_intensidad.lock();
        int x;
        if(posArrayHora == 47){
            x = temp_23_30[idFarola];
        }else{
            x = farolas[idFarola].getIntensidades()[posArrayHora];
        }
        lock_intensidad.unlock();
        return x;
    }

    public void setIntensidad(int idFarola, int posArrayHora, int new_intensidad){
        lock_intensidad.lock();
        if(posArrayHora == 47){
            temp_23_30[idFarola] = farolas[idFarola].getIntensidades()[47];
        }
        farolas[idFarola].setIntensidad(posArrayHora, new_intensidad);
        lock_intensidad.unlock();
    }

    public void actualizarFarolas(int posArrayHora, int idFarola, int ldr) throws SQLException { //Ver si hay que borrar
        if (ldr < margenActivacionLdr) {
            //Desactivar
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

    public synchronized int[][] getMatrizAdyacencia(){
        return matrizAdyacencia;
    }
}