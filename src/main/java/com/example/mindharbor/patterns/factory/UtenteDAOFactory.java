package com.example.mindharbor.patterns.factory;

import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.dao.csv.utente_dao_csv.UtenteDAOCsv;
import com.example.mindharbor.dao.memoria.UtenteDAOMemoria;
import com.example.mindharbor.dao.mysql.UtenteDAOMySql;
import com.example.mindharbor.patterns.facade.TipoPersistenza;

public class UtenteDAOFactory {
    public UtenteDAO getUtenteDAO(TipoPersistenza tipoPersistenza) {
        return switch (tipoPersistenza) {
            case CSV -> creaUtenteDAOCSV();
            case MYSQL -> creaUtenteDAOMYSQL();
            case MEMORIA -> creaUtenteDAOMEMORIA();
        };
    }
    public UtenteDAO creaUtenteDAOCSV() {return new UtenteDAOCsv();}
    public UtenteDAO creaUtenteDAOMYSQL(){return new UtenteDAOMySql();}
    public UtenteDAO creaUtenteDAOMEMORIA(){return new UtenteDAOMemoria();}
}
