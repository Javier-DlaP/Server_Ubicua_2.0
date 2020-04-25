import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.time.*;

public class Mensaje {
    private int id;
    private float ldr;
    private float movement;
    private Datos datos;
    private boolean actualizarSiempre;
    private int posArrayHora;

    public Mensaje(String mensaje, Datos datos, boolean actualizarSiempre) throws NoSuchElementException{
        final StringTokenizer st = new StringTokenizer(mensaje, ",");
        id = Integer.valueOf(st.nextToken());
        ldr = Float.valueOf(st.nextToken());
        movement = Float.valueOf(st.nextToken());
        this.datos = datos;
        this.actualizarSiempre = actualizarSiempre;
        getPosArrayHora();
    }

    private void getPosArrayHora(){
        LocalDateTime fecha = LocalDateTime.now();
        int hora = fecha.getHour();
        int minuto = fecha.getMinute();
        posArrayHora = (hora*2)+(minuto/30);
    }

    public String enviarArduino(){
        datos.anadirDato(posArrayHora, id, ldr, movement);
        float luz;
        if(actualizarSiempre){
            luz = datos.actualizarSiempre(id, ldr, movement);
        }else{
            luz = datos.actualizar(posArrayHora, id, ldr, movement);
        }
        return Float.toString(luz);
    }
}