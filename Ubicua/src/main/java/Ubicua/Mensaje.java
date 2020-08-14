package Ubicua;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.CyclicBarrier;


public class Mensaje extends Thread {
    private int id;
    private int ldr;
    private int movement;
    private Datos datos;
    private int posArrayHora;
    private CyclicBarrier barreraMensajes;

    public Mensaje(String mensaje, Datos datos, CyclicBarrier barreraMensajes) throws NoSuchElementException {
        final StringTokenizer st = new StringTokenizer(mensaje, ",");
        id = Integer.valueOf(st.nextToken());
        movement = Integer.valueOf(st.nextToken());
        ldr = Integer.valueOf(st.nextToken());
        this.datos = datos;
        posArrayHora = datos.getIdHora();
        this.barreraMensajes = barreraMensajes;

        //System.out.println(id + "," + movement + "," + ldr);
    }

    @Override
    public void run() {
        try {
            // Almacenar datos en el objeto Datos
            datos.anadirDato(posArrayHora, id, movement, ldr); //hacerMedia

            // (Llamada funcion de Datos) Comprobar valores con la matriz de adyacencia, de lo que recibe del arduino y base de datos -> (almacenar resultado en Datos)

            barreraMensajes.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}