package com.example.mindharbor.patterns.facade;

import com.example.mindharbor.dao.*;
import com.example.mindharbor.patterns.factory.*;

public class DAOFactoryFacade {
    private static DAOFactoryFacade istanza;
    private TipoPersistenza tipoPersistenza;
    private AppuntamentoDAO appuntamentoDAO;
    private PazienteDAO pazienteDAO;
    private PsicologoDAO psicologoDAO;
    private TerapiaDAO terapiaDAO;
    private TestPsicologicoDAO testPsicologicoDAO;
    private UtenteDAO utenteDAO;

    private DAOFactoryFacade() {/*costruttore privato per evitare una new*/}

    public static synchronized DAOFactoryFacade getInstance() {
        if(istanza==null) {
            istanza=new DAOFactoryFacade();
        }
        return istanza;
    }

    public void setTipoPersistenza(TipoPersistenza tipoPersistenza) {
        this.tipoPersistenza=tipoPersistenza;
    }

    public AppuntamentoDAO getAppuntamentoDAO() {
        if(appuntamentoDAO==null) {
            AppuntamentoDAOFactory appuntamentoDAOFactory=new AppuntamentoDAOFactory();
            appuntamentoDAO=appuntamentoDAOFactory.getAppuntamentoDAO(tipoPersistenza);
        }
        return appuntamentoDAO;
    }

    public PazienteDAO getPazienteDAO(){
        if(pazienteDAO==null) {
            PazienteDAOFactory pazienteDAOFactory=new PazienteDAOFactory();
            pazienteDAO=pazienteDAOFactory.getPazienteDAO(tipoPersistenza);
        }
        return pazienteDAO;
    }

    public PsicologoDAO getPsicologoDAO() {
        if(psicologoDAO==null) {
            PsicologoDAOFactory psicologoDAOFactory=new PsicologoDAOFactory();
            psicologoDAO=psicologoDAOFactory.getPsicologoDAO(tipoPersistenza);
        }
        return psicologoDAO;
    }

    public TerapiaDAO getTerapiaDAO() {
        if(terapiaDAO==null) {
            TerapiaDAOFactory terapiaDAOFactory=new TerapiaDAOFactory();
            terapiaDAO=terapiaDAOFactory.getTerapiaDAO(tipoPersistenza);
        }
        return terapiaDAO;
    }

    public TestPsicologicoDAO getTestPsicologicoDAO() {
        if(testPsicologicoDAO==null) {
            TestPsicologicoDAOFactory testPsicologicoDAOFactory=new TestPsicologicoDAOFactory();
            testPsicologicoDAO=testPsicologicoDAOFactory.getTestPsicologicoDAO(tipoPersistenza);
        }
        return testPsicologicoDAO;
    }

    public UtenteDAO getUtenteDAO() {
        if(utenteDAO==null) {
            UtenteDAOFactory utenteDAOFactory=new UtenteDAOFactory();
            utenteDAO=utenteDAOFactory.getUtenteDAO(tipoPersistenza);
        }
        return utenteDAO;
    }
}
