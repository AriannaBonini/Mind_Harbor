package com.example.mindharbor.patterns.factory;

import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.csv.appuntamento_dao_csv.AppuntamentoDAOCsv;
import com.example.mindharbor.dao.memoria.AppuntamentoDAOMemoria;
import com.example.mindharbor.dao.mysql.AppuntamentoDAOMySql;
import com.example.mindharbor.patterns.facade.TipoPersistenza;

public class AppuntamentoDAOFactory {

    public AppuntamentoDAO getAppuntamentoDAO(TipoPersistenza tipoPersistenza) {
        return switch (tipoPersistenza) {
            case CSV -> creaAppuntamentoDAOCSV();
            case MYSQL -> creaAppuntamentoDAOMYSQL();
            case MEMORIA -> creaAppuntamentoDAOMEMORIA();
        };
    }

    public AppuntamentoDAO creaAppuntamentoDAOCSV() {return new AppuntamentoDAOCsv();}
    public AppuntamentoDAO creaAppuntamentoDAOMYSQL(){return new AppuntamentoDAOMySql();}
    public AppuntamentoDAO creaAppuntamentoDAOMEMORIA(){return new AppuntamentoDAOMemoria();}


}
