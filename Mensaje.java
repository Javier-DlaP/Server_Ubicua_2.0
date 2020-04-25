import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.CyclicBarrier;
import java.time.*;

public class Mensaje {
    private int id;
    private float ldr;
    private float movement;
    private Datos datos;
    private boolean actualizarSiempre;
    private int posArrayHora;
    private CyclicBarrier barreraDatos;

    public Mensaje(String mensaje, Datos datos, boolean actualizarSiempre, CyclicBarrier barreraDatos) throws NoSuchElementException{
        final StringTokenizer st = new StringTokenizer(mensaje, ",");
        id = Integer.valueOf(st.nextToken());
        ldr = Float.valueOf(st.nextToken());
        movement = Float.valueOf(st.nextToken());
        this.datos = datos;
        this.actualizarSiempre = actualizarSiempre;
        getPosArrayHora();
        this.barreraDatos = barreraDatos;
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
        datos.anadirDato(posArrayHora, id, light, movement);
        return Float.toString(light);
    }
}