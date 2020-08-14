package Ubicua;

import java.time.LocalDateTime;

public class HiloHora extends Thread {
    private Datos datos;
    private int anterior_hora;
    private Database database = new Database();

    public HiloHora(Datos datos) {
        this.datos = datos;
        LocalDateTime now = LocalDateTime.now();
        int hora = now.getHour();
        int minuto = now.getMinute();
        int idHora = hora * 2 + minuto / 30;
        anterior_hora = idHora;
    }

    public void verIntesidadesActuales(){
        System.out.println(((int)datos.getIdHora()/2)+":"+(datos.getIdHora()%2)*30);
        for(int i=0; i<datos.getNFarolas(); i++){
            System.out.print(datos.getIntensidad(i, datos.getIdHora())+",");
        }
        System.out.println();
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
                        //Guardar en temporal las intensidades de las 23:30
                        int intensidades[][] = datos.generaIntensidadesFarolas();
                        datos.guardaIntensidades(intensidades);
                    }else if(idHora == 0){
                        Farola[] farolas = datos.getFarolas();
                        for (int i = 0; i < farolas.length; i++) {
                            database.insertDatosFecha(farolas[i]);
                        }
                    }
                }
                Thread.sleep(30000);  // Se ejecuta cada medio minuto
                verIntesidadesActuales();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}