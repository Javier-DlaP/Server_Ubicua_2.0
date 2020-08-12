package Ubicua;

import java.time.LocalDateTime;

public class HiloHora extends Thread {
    private Datos datos;
    private int anterior_hora;
    private boolean _23_30 = false;
    private boolean _00_00 = false;

    public HiloHora(Datos datos) {
        this.datos = datos;
        LocalDateTime now = LocalDateTime.now();
        int hora = now.getHour();
        int minuto = now.getMinute();
        int idHora = hora * 2 + minuto / 30;
        anterior_hora = idHora;
    }

    public void run() {
        try {
            while(true){
                LocalDateTime now = LocalDateTime.now();
                int hora = now.getHour();
                int minuto = now.getMinute();
                int idHora = hora * 2 + minuto / 30;
                if(idHora != anterior_hora){
                    datos.calcularMedias(idHora);
                    if(idHora == 47){
                        //Generar intensidades del dia siguente
                    }else if(idHora == 0){
                        //Guardar datos en la base de datos
                    }
                }
                Thread.sleep(30000);  // Se ejecuta cada medio minuto
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}