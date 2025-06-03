package com.example.mindharbor.patterns.facade;

import com.example.mindharbor.dao.*;
import com.example.mindharbor.patterns.factory.*;

public class DAOFactoryFacade {
    private static final DAOFactoryFacade istanza=new DAOFactoryFacade();
    private volatile TipoPersistenza tipoPersistenza;
    private AppuntamentoDAO appuntamentoDAO;
    private PazienteDAO pazienteDAO;
    private PsicologoDAO psicologoDAO;
    private TerapiaDAO terapiaDAO;
    private TestPsicologicoDAO testPsicologicoDAO;
    private UtenteDAO utenteDAO;

    private DAOFactoryFacade() {/*costruttore privato per evitare una new*/}

    public static DAOFactoryFacade getInstance() {return istanza;}

    public synchronized void setTipoPersistenza(TipoPersistenza tipoPersistenza) {
        this.tipoPersistenza=tipoPersistenza;
    }

    public synchronized AppuntamentoDAO getAppuntamentoDAO() {
        if(appuntamentoDAO==null) {
            synchronized (this)  {
            AppuntamentoDAOFactory appuntamentoDAOFactory=new AppuntamentoDAOFactory();
            appuntamentoDAO=appuntamentoDAOFactory.getAppuntamentoDAO(tipoPersistenza);
        }
        }
        return appuntamentoDAO;
    }

    public synchronized PazienteDAO getPazienteDAO(){
        if(pazienteDAO==null) {
            PazienteDAOFactory pazienteDAOFactory=new PazienteDAOFactory();
            pazienteDAO=pazienteDAOFactory.getPazienteDAO(tipoPersistenza);
        }
        return pazienteDAO;
    }

    public synchronized PsicologoDAO getPsicologoDAO() {
        if(psicologoDAO==null) {
            PsicologoDAOFactory psicologoDAOFactory=new PsicologoDAOFactory();
            psicologoDAO=psicologoDAOFactory.getPsicologoDAO(tipoPersistenza);
        }
        return psicologoDAO;
    }

    public synchronized TerapiaDAO getTerapiaDAO() {
        if(terapiaDAO==null) {
            TerapiaDAOFactory terapiaDAOFactory=new TerapiaDAOFactory();
            terapiaDAO=terapiaDAOFactory.getTerapiaDAO(tipoPersistenza);
        }
        return terapiaDAO;
    }

    public synchronized TestPsicologicoDAO getTestPsicologicoDAO() {
        if(testPsicologicoDAO==null) {
            TestPsicologicoDAOFactory testPsicologicoDAOFactory=new TestPsicologicoDAOFactory();
            testPsicologicoDAO=testPsicologicoDAOFactory.getTestPsicologicoDAO(tipoPersistenza);
        }
        return testPsicologicoDAO;
    }

    public synchronized UtenteDAO getUtenteDAO() {
        if(utenteDAO==null) {
            UtenteDAOFactory utenteDAOFactory=new UtenteDAOFactory();
            utenteDAO=utenteDAOFactory.getUtenteDAO(tipoPersistenza);
        }
        return utenteDAO;
    }
}
