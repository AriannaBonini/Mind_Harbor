package com.example.mindharbor.patterns.factory;

import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.dao.csv.terapia_dao_csv.TerapiaDAOCsv;
import com.example.mindharbor.dao.mysql.TerapiaDAOMySql;
import com.example.mindharbor.patterns.facade.TipoPersistenza;

public class TerapiaDAOFactory {
    public TerapiaDAO getTerapiaDAO(TipoPersistenza tipoPersistenza) {
        return switch (tipoPersistenza) {
            case CSV -> creaTerapiaDAOCSV();
            case MYSQL -> creaTerapiaDAOMYSQL();
        };
    }
    public TerapiaDAO creaTerapiaDAOCSV() {return new TerapiaDAOCsv();}
    public TerapiaDAO creaTerapiaDAOMYSQL(){return new TerapiaDAOMySql();}
}
