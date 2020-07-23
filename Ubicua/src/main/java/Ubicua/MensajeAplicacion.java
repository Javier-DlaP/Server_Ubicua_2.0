package Ubicua;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class MensajeAplicacion {
    private int idFarola;
    private Database database = new Database();
    private Date fecha;

    public MensajeAplicacion(int idFarola) {
        this.idFarola = idFarola;
    }

    public MensajeAplicacion(int idFarola, Date fecha) {
        this.idFarola = idFarola;
        this.fecha = fecha;
    }

    public String obtenerFechasHistorico() throws SQLException, ParseException {
        return "fechasminmax" + database.selectFechasHistorico(idFarola);
    }

    public String obtenerDatosMedia() throws SQLException {
        return "datosmedia" + database.selectDatosMedia(idFarola);
    }

    public String obtenerDatosFecha() throws SQLException {
        return "datos" + database.selectDatosFecha(idFarola, fecha);
    }
}