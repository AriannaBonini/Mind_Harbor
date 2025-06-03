package com.example.mindharbor.patterns.factory;

import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.dao.csv.test_psicologico_dao_csv.TestPsicologicoDAOCsv;
import com.example.mindharbor.dao.memoria.TestPsicologicoDAOMemoria;
import com.example.mindharbor.dao.mysql.TestPsicologicoDAOMySql;
import com.example.mindharbor.patterns.facade.TipoPersistenza;

public class TestPsicologicoDAOFactory {

    public TestPsicologicoDAO getTestPsicologicoDAO(TipoPersistenza tipoPersistenza) {
        return switch (tipoPersistenza) {
            case CSV -> creaTestPsicologicoDAOCSV();
            case MYSQL -> creaTestPsicologicoDAOMYSQL();
            case MEMORIA -> creaTestPsicologicoDAOMEMORIA();
        };
    }
    public TestPsicologicoDAO creaTestPsicologicoDAOCSV() {return new TestPsicologicoDAOCsv();}
    public TestPsicologicoDAO creaTestPsicologicoDAOMYSQL(){return new TestPsicologicoDAOMySql();}
    public TestPsicologicoDAO creaTestPsicologicoDAOMEMORIA(){return new TestPsicologicoDAOMemoria();}
}
