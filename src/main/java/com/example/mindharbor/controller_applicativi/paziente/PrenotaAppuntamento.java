package com.example.mindharbor.controller_applicativi.paziente;


import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.PsicologoDAO;
import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.mockapi.BoundaryMockAPICalendario;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.costanti.OrariEDataValidiRichiestaAppuntamento;
import com.example.mindharbor.tipo_utente.UserType;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;

import java.util.ArrayList;
import java.util.List;

public class PrenotaAppuntamento {

    private final NavigatorSingleton navigator=NavigatorSingleton.getInstance();

    public InfoUtenteBean getInfoUtente() {return new SetInfoUtente().getInfo();}

    public String tooltipOra() {return "Le fasce orarie valide sono: " + OrariEDataValidiRichiestaAppuntamento.stampaFasceOrarie();}
    public String tooltipData() {return OrariEDataValidiRichiestaAppuntamento.INFO_DATA;}

    public boolean controlloFormatoAnni(String anni) {
        //controlla se la stringa anni è formata solo da cifre numeriche.
        return anni.matches("\\d+");
    }

    public Boolean controlloDataEOra(AppuntamentiBean richiestaAppuntamentoBean) {
        try {
            return new Appuntamento().controllaOrario(richiestaAppuntamentoBean.getOra()) && new Appuntamento().controllaData(richiestaAppuntamentoBean.getData());
        }catch (IllegalArgumentException e) {
            return false;
        }
    }


    public boolean controlloInformazioniPaziente(PazienteBean pazienteBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        PazienteDAO pazienteDAO= daoFactoryFacade.getPazienteDAO();
        try {
            if(!pazienteBean.getNome().equals(SessionManager.getInstance().getCurrentUser().getNome()) || !pazienteBean.getCognome().equals(SessionManager.getInstance().getCurrentUser().getCognome()))  {
                return false;
            }
            return pazienteDAO.checkAnniPaziente(new Paziente(SessionManager.getInstance().getCurrentUser().getUsername(),"","", UserType.PAZIENTE, pazienteBean.getAnni()));

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }


    public List<PsicologoBean> getListaPsicologi() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO= daoFactoryFacade.getUtenteDAO();

        try {
            List<PsicologoBean> listaPsicologiBean = new ArrayList<>();
            List<Psicologo> listaPsicologi = utenteDAO.listaUtentiDiTipoPsicologo(SessionManager.getInstance().getUsernamePsicologo());
            for(Psicologo psi : listaPsicologi) {
                PsicologoBean psicologoBean=new PsicologoBean(psi.getUsername(),psi.getNome(),psi.getCognome(),0,"",psi.getGenere());

                listaPsicologiBean.add(psicologoBean);
            }

            return listaPsicologiBean;

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void eliminaAppuntamentoSelezionato(){
        eliminaRichiestaAppuntamento();
        eliminaPsicologoSelezionato();
    }

    public void salvaRichiestaAppuntamento(AppuntamentiBean appuntamentiBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();

        appuntamentiBean.getPaziente().setUsername(SessionManager.getInstance().getCurrentUser().getUsername());
        Appuntamento appuntamento= new Appuntamento(appuntamentiBean.getData(),
                appuntamentiBean.getOra(),
                null,
                new Paziente(appuntamentiBean.getPaziente().getUsername()),
                new Psicologo(appuntamentiBean.getPsicologo().getUsername()));

        try {
            appuntamentoDAO.insertRichiestaAppuntamento(appuntamento);
            eliminaAppuntamentoSelezionato();

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }

    }

    public PsicologoBean getInfoPsicologo(PsicologoBean psicologoSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        PsicologoDAO psicologoDAO= daoFactoryFacade.getPsicologoDAO();
        try {
            Psicologo psicologo= psicologoDAO.getInfoPsicologo(new Psicologo(psicologoSelezionato.getUsername()));

            psicologoSelezionato.setCostoOrario(psicologo.getCostoOrario());
            psicologoSelezionato.setNomeStudio(psicologo.getNomeStudio());

        }catch (EccezioneDAO e){
            throw new EccezioneDAO(e.getMessage());
        }
        return psicologoSelezionato;
    }

    public List<AppuntamentiBean> getListaRichieste() throws EccezioneDAO {
        System.out.println("Sono qui ");
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        List<AppuntamentiBean> listaRichiesteBean=new ArrayList<>();

        try {
            List<Appuntamento> listaRichieste = appuntamentoDAO.trovaRichiesteAppuntamento(
                    SessionManager.getInstance().getCurrentUser());

            for(Appuntamento ric: listaRichieste) {
                AppuntamentiBean ricBean= new AppuntamentiBean(
                        new PazienteBean(ric.getPaziente().getUsername(),ric.getPaziente().getNome(),ric.getPaziente().getCognome(),ric.getPaziente().getGenere()),
                        ric.getIdAppuntamento(),
                        ric.getNotificaRichiesta());

                listaRichiesteBean.add(ricBean);
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return listaRichiesteBean;
    }

    public AppuntamentiBean aggiungiInfoRichiestaAppuntamento(AppuntamentiBean richiestaAppuntamento) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        Appuntamento richiesta;
        try {
            richiesta = appuntamentoDAO.getInfoRichiesta(new Appuntamento(richiestaAppuntamento.getIdAppuntamento()));

            richiestaAppuntamento.setOra(richiesta.getOra());
            richiestaAppuntamento.setData(richiesta.getData());

            return richiestaAppuntamento;
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void modificaStatoNotifica(AppuntamentiBean richiestaAppuntamento) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            appuntamentoDAO.updateStatoNotifica(new Appuntamento(richiestaAppuntamento.getIdAppuntamento()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public boolean verificaDisponibilita(Integer idAppuntamento) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            if(!appuntamentoDAO.getDisp(idAppuntamento,SessionManager.getInstance().getCurrentUser())) {
                return false;
            }
            return BoundaryMockAPICalendario.calendario();

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void richiestaAccettata(AppuntamentiBean richiestaAppuntamento) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();

        Appuntamento appuntamentoAccettato= new Appuntamento(richiestaAppuntamento.getIdAppuntamento(),new Psicologo(SessionManager.getInstance().getCurrentUser().getUsername()),new Paziente(richiestaAppuntamento.getPaziente().getUsername()));
        try {
            appuntamentoDAO.updateRichiesta(appuntamentoAccettato);

            /**
             * Eliminiamo tutte le altre richieste di appuntamento del paziente ad altri psicologi
             */

            appuntamentoDAO.eliminaRichiesteDiAppuntamentoPerAltriPsicologi(appuntamentoAccettato);

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void richiestaRifiutata(AppuntamentiBean richiestaAppuntamento) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            appuntamentoDAO.eliminaRichiesta(new Appuntamento(richiestaAppuntamento.getIdAppuntamento()));

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public AppuntamentiBean getRichiestaAppuntamento() {return navigator.getAppuntamentoBean();}
    public void setRichiestaAppuntamento(AppuntamentiBean appuntamento) {navigator.setAppuntamentoBean(appuntamento);}
    public void eliminaRichiestaAppuntamento() {navigator.eliminaAppuntamentoBean();}
    public void setPsicologoSelezionato(PsicologoBean psicologoSelezionato) {navigator.setPsicologoBean(psicologoSelezionato);}
    public PsicologoBean getPsicologoSelezionato(){ return navigator.getPsicologoBean();}
    public void eliminaPsicologoSelezionato(){navigator.eliminaPsicologoBean();}


}
