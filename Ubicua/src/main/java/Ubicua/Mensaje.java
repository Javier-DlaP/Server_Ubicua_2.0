package Ubicua;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.CyclicBarrier;
import java.time.*;


public class Mensaje extends Thread {
    private int id;
    private int ldr;
    private float movement;
    private Datos datos;
    private boolean actualizarSiempre;
    private int posArrayHora;
    private CyclicBarrier barreraDatos;
    private CyclicBarrier barreraMensajes;

    public Mensaje(String mensaje, Datos datos, CyclicBarrier barreraDatos, CyclicBarrier barreraMensajes) throws NoSuchElementException {
        final StringTokenizer st = new StringTokenizer(mensaje, ",");
        id = Integer.valueOf(st.nextToken());
        movement = Float.valueOf(st.nextToken());
        ldr = Integer.valueOf(st.nextToken());
        this.datos = datos;
        getPosArrayHora();
        this.barreraDatos = barreraDatos;
        this.barreraMensajes = barreraMensajes;
    }

    @Override
    public void run() {
        try {
            // Almacenar datos en el objeto Datos
            datos.anadirDato(posArrayHora, id, movement);
            
            barreraDatos.await();

            // (Llamada funcion de Datos) Comprobar valores con la matriz de adyacencia, de lo que recibe del arduino y base de datos -> (almacenar resultado en Datos)

            barreraMensajes.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPosArrayHora(){
        LocalDateTime fecha = LocalDateTime.now();
        int hora = fecha.getHour();
        int minuto = fecha.getMinute();
        posArrayHora = (hora*2)+(minuto/30);
    }

    public String enviarArduino(){
        float light;
        if(actualizarSiempre){
            light = datos.actualizarSiempre(id, ldr, movement, barreraDatos);
        }else{
            light = datos.actualizar(posArrayHora, id, ldr, movement);
        }
        //datos.anadirDato(posArrayHora, id, light, movement, ldr);
        return Float.toString(light);
    }
}