package Ubicua;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Datos {
    private Farola farolas[];
    private int matrizAdyacencia[][] = {{ 0, 1, 1, 2, 2, -1, -1, 1, -1, -1 },
                                        { 1, 0, -1, -1, -1, 1, 2, -1, -1, -1 },
                                        { 1, -1, 0, 1, -1, 2, -1, -1, 2, 1 },
                                        { 2, -1, 1, 0, 2, -1, 1, -1, -1, -1 },
                                        { 2, -1, -1, 2, 0, -1, -1, -1, 1, 2 },
                                        { -1, 1, 2, -1, -1, 0, 2, 1, -1, -1 },
                                        { -1, 2, -1, 1, -1, 2, 0, -1, 1, 1 },
                                        { 1, -1, -1, -1, -1, 1, -1, 0, 1, -1 },
                                        { -1, -1, 2, -1, 1, -1, 1, 1, 0, -1 },
                                        { -1, -1, 1, -1, 2, -1, 1, -1, -1, 0 } };
    private int n_farolas = matrizAdyacencia.length;
    private int temp_23_30[] = new int[n_farolas]; // valor de intensidad temporal
    private int margenActivacionLdr = 500; // ldr>500 activacion
    private float activacionBaja = 0.1f;
    private float activacionMedia = 0.2f;
    private float activacionAlta = 0.3f;
    private Database database = new Database();
    private int idHora = 0;
    private ReentrantLock lock_intensidad = new ReentrantLock();
    private ReentrantLock lock_medias = new ReentrantLock();
    private ReentrantLock lock_intensidad_temporal = new ReentrantLock();
    private ReentrantLock lock_idHora = new ReentrantLock();
    private ReentrantLock lock_useAverage = new ReentrantLock();
    private boolean useAverage = true; // Modo de funcionamiento del sistema de alumbrado inteligente

    public Datos() {
        farolas = new Farola[n_farolas];
        for (int i = 0; i < n_farolas; i++) {
            farolas[i] = new Farola(i);
        }
    }

    public synchronized int getNFarolas(){
        return n_farolas;
    }

    public void setUseAverage(boolean bool){
        lock_useAverage.lock();
        useAverage = bool;
        lock_useAverage.unlock();
    }

    public boolean getUseAverage(){
        lock_useAverage.lock();
        boolean aux = useAverage;
        lock_useAverage.unlock();
        return aux;
    }

    public void guardarTemporalIntensidades(){
        lock_intensidad_temporal.lock();
        for(int i=0; i<n_farolas; i++){
            temp_23_30[i] = getIntensidad(i, 47);
        }
        lock_intensidad_temporal.lock();
    }

    public void guardaIntensidades(int[][] intensidades){
        for(int i=0; i<n_farolas; i++){
            for(int j=0; j<48; j++){
                setIntensidad(i, j, intensidades[i][j]);
            }
        }
    }

    public int[][] generaIntensidadesFarolas() throws SQLException, IOException, InterruptedException {
        int intensidades[][] = new int[n_farolas][48];
        float sensores[][] = new float[n_farolas][48];
        float luz[][] = new float[n_farolas][48];
        if(getUseAverage()){ //Usa la media de valores del mes anterior
            for(int i=0; i<n_farolas; i++){
                ArrayList<Float> aux_luz = database.selectDatosMediaLuz(i);
                ArrayList<Float> aux_sensor = database.selectDatosMediaSensor(i);
                for(int j=0; j<48; j++){
                    sensores[i][j] = aux_sensor.get(j);
                    luz[i][j] = aux_luz.get(j);
                }
            }
        }else{ //Usa la predicción realizada por la IA
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1); //Se guarda la fecha del dia siguiente
            Date fecha = cal.getTime();
            int dia = fecha.getDate();
            int mes = fecha.getMonth();
            int dia_semana = fecha.getDay();
            for(int i=0; i<n_farolas; i++){
                //Escribe en el CSV la informacion del dia del que se quiere la informacion
                FileWriter writer = new FileWriter("C:\\Users\\javir\\data_streetlights_input.csv");
                writer.append("id_streetlight,day,month,week_day\n");
                writer.append(String.valueOf(i)+","+String.valueOf(dia)+","+String.valueOf(mes)+","+String.valueOf(dia_semana)+"\n");
                writer.flush();
                writer.close();
                //Ejecuta la IA
                Runtime.getRuntime().exec("cd C:\\Users\\javir & activate fastai & python usetabulardatastreetlighting.py");
                Thread.sleep(5000); //Se esperan 5 segundos al tardar como mucho 4 segundos en ejecutarse
                //Lee el archivo de salida de la red neuronal
                ArrayList<Float> numeros = new ArrayList<Float>();
                File file = new File("C:\\Users\\javir\\predictions_streetlights.txt");
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String[] lista = sc.nextLine().split(",");
                    for (String numero: lista) {
                        numeros.add(Float.parseFloat(numero));
                    }
                }
                sc.close();
                ArrayList<Float> aux_luz = database.selectDatosMediaLuz(i);
                for(int j=0; j<48; j++){
                    luz[i][j] = aux_luz.get(j);
                    sensores[i][j] = numeros.get(j);
                }
            }
        }
        for(int j=0; j<48; j++){
            for(int i=0; i<n_farolas; i++){ //Elige la intesidad en función del movimiento previsto
                if(sensores[i][j]<activacionBaja){
                    intensidades[i][j] = 0;
                }else if(sensores[i][j]<activacionMedia){
                    intensidades[i][j] = 1;
                }else if(sensores[i][j]<activacionAlta){
                    intensidades[i][j] = 2;
                }else{
                    intensidades[i][j] = 3;
                }
            }
            for(int i=0; i<n_farolas; i++){ //Apaga las farolas donde se prevea luz
                if(luz[i][j]<margenActivacionLdr){
                    intensidades[i][j] = 0;
                }
            }
            for(int i=0; i<n_farolas; i++){ //Suaviza la diferencia en intensidad entre farolas
                for(int k=0; k<n_farolas; k++){
                    if(matrizAdyacencia[i][k] == 1){
                        intensidades[k][j] = Math.max(intensidades[i][j]-1, intensidades[k][j]);
                    }else if(matrizAdyacencia[i][k] == 2){
                        intensidades[k][j] = Math.max(intensidades[i][j]-2, intensidades[k][j]);
                    }
                }
            }
        }
        return intensidades;
    }

    public Farola[] getFarolas() {
        lock_medias.lock();
        lock_intensidad.lock();
        Farola[] farolas = this.farolas;
        lock_medias.unlock();
        lock_intensidad.unlock();
        return farolas;
    }

    public int getIdHora(){
        lock_idHora.lock();
        int aux = idHora;
        lock_idHora.unlock();
        return aux;
    }

    public void cambiarIdHora() {
        lock_idHora.lock();
        LocalDateTime now = LocalDateTime.now();
        int hora = now.getHour();
        int minuto = now.getMinute();
        idHora = hora*2 + minuto/30;
        lock_idHora.unlock();
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

    private void setIntensidad(int idFarola, int posArrayHora, int new_intensidad){
        lock_intensidad_temporal.lock();
        lock_intensidad.lock();
        if(posArrayHora == 47){
            temp_23_30[idFarola] = farolas[idFarola].getIntensidades()[47];
        }
        farolas[idFarola].setIntensidad(posArrayHora, new_intensidad);
        lock_intensidad.unlock();
        lock_intensidad_temporal.unlock();
    }

    public synchronized int[][] getMatrizAdyacencia(){
        return matrizAdyacencia;
    }
}