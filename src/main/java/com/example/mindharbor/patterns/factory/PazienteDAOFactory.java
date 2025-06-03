package com.example.mindharbor.patterns.factory;

import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.csv.paziente_dao_csv.PazienteDAOCsv;
import com.example.mindharbor.dao.memoria.PazienteDAOMemoria;
import com.example.mindharbor.dao.mysql.PazienteDAOMySql;
import com.example.mindharbor.patterns.facade.TipoPersistenza;

public class PazienteDAOFactory {

    public PazienteDAO getPazienteDAO(TipoPersistenza tipoPersistenza) {
        return switch (tipoPersistenza) {
            case CSV -> creaPazienteDAOCSV();
            case MYSQL -> creaPazienteDAOMYSQL();
            case MEMORIA -> creaPazienteDAOMEMORIA();
        };
    }
    public PazienteDAO creaPazienteDAOCSV() {return new PazienteDAOCsv();}
    public PazienteDAO creaPazienteDAOMYSQL(){return new PazienteDAOMySql();}
    public PazienteDAO creaPazienteDAOMEMORIA(){return new PazienteDAOMemoria();}
}
