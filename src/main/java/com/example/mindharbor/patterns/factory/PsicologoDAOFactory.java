package com.example.mindharbor.patterns.factory;

import com.example.mindharbor.dao.PsicologoDAO;
import com.example.mindharbor.dao.csv.psicologo_dao_csv.PsicologoDAOCsv;
import com.example.mindharbor.dao.memoria.PsicologoDAOMemoria;
import com.example.mindharbor.dao.mysql.PsicologoDAOMySql;
import com.example.mindharbor.patterns.facade.TipoPersistenza;

public class PsicologoDAOFactory {
    public PsicologoDAO getPsicologoDAO(TipoPersistenza tipoPersistenza) {
        return switch (tipoPersistenza) {
            case CSV -> creaPsicologoDAOCSV();
            case MYSQL -> creaPsicologoDAOMYSQL();
            case MEMORIA -> creaPsicologoDAOMEMORIA();
        };
    }
    public PsicologoDAO creaPsicologoDAOCSV() {return new PsicologoDAOCsv();}
    public PsicologoDAO creaPsicologoDAOMYSQL(){return new PsicologoDAOMySql();}
    public PsicologoDAO creaPsicologoDAOMEMORIA(){return new PsicologoDAOMemoria();}

}
