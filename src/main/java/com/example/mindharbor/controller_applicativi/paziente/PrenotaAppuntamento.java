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
import com.example.mindharbor.strumenti_utili.ValidatoreSessione;
import com.example.mindharbor.strumenti_utili.costanti.OrariEDataValidiRichiestaAppuntamento;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.mindharbor.strumenti_utili.costanti.OrariEDataValidiRichiestaAppuntamento.*;
import static com.example.mindharbor.strumenti_utili.costanti.OrariEDataValidiRichiestaAppuntamento.FINE_PAUSA;

public class PrenotaAppuntamento {

    public InfoUtenteBean getInfoUtente() {return new SetInfoUtente().getInfo();}

    public String tooltipOra() {return OrariEDataValidiRichiestaAppuntamento.stampaFasceOrarie();}
    public String tooltipData() {return OrariEDataValidiRichiestaAppuntamento.INFO_DATA;}


    public Boolean controlloDataEOra(AppuntamentiBean richiestaAppuntamentoBean) {
        try {
            return controllaOrario(richiestaAppuntamentoBean.getOra()) && controllaData(richiestaAppuntamentoBean.getData());
        }catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean controllaOrario(String ora) {
        try {
            LocalTime time = LocalTime.parse(ora);

            boolean inFasciaMattutina = !time.isAfter(INIZIO_PAUSA) && !time.isBefore(ORARIO_APERTURA);
            boolean inFasciaPomeridiana = !time.isAfter(ORARIO_CHIUSURA) &&  !time.isBefore(FINE_PAUSA);

            return inFasciaMattutina || inFasciaPomeridiana;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(e);
        }
    }


    private boolean controllaData(String data) {
        try {
            LocalDate localDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (localDate.isBefore(LocalDate.now())) {
                return false;
            }
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            return !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
        }catch(DateTimeParseException e) {
            throw new IllegalArgumentException();
        }
    }


    public boolean controlloInformazioniPaziente(PazienteBean pazienteBean) throws EccezioneDAO {
        Paziente pazienteCorrente=SessionManager.getInstance().getPazienteCorrente();

        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        PazienteDAO pazienteDAO= daoFactoryFacade.getPazienteDAO();
        try {
            if(!pazienteBean.getNome().equals(pazienteCorrente.getNome()) || !pazienteBean.getCognome().equals(pazienteCorrente.getCognome()) || !controllaAnniPaziente(pazienteBean.getAnni()))  {
                return false;
            }
            if(pazienteCorrente.getAnni()!=null) {
                return Objects.equals(pazienteCorrente.getAnni(), pazienteBean.getAnni());
            }
            pazienteCorrente.setAnni((pazienteDAO.checkAnniPaziente(pazienteCorrente)).getAnni());
            return Objects.equals(pazienteCorrente.getAnni(), pazienteBean.getAnni());

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    private boolean controllaAnniPaziente(Integer anni) {return anni >= 18 && anni <= 100;}


    public List<PsicologoBean> getListaPsicologi() throws EccezioneDAO {
        Paziente pazienteCorrente = SessionManager.getInstance().getPazienteCorrente();
        Psicologo psicologoPazienteCorrente = pazienteCorrente.getPsicologo();

        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO = daoFactoryFacade.getUtenteDAO();

        try {
            if (ValidatoreSessione.psicologoDelPazienteCorrente(psicologoPazienteCorrente)) {
                if (ValidatoreSessione.controllaPresenzaDatiAnagraficiPsicologo(psicologoPazienteCorrente)) {
                    return popolaListaPsicologiBean(List.of(psicologoPazienteCorrente));
                } else {
                    List<Psicologo> psicologo = utenteDAO.listaUtentiDiTipoPsicologo(psicologoPazienteCorrente);
                    aggiornaSessioneConDatiAnagraficiPsicologo(psicologo.getFirst());
                    return popolaListaPsicologiBean(psicologo);
                }
            } else {
                List<Psicologo> listaPsicologi = utenteDAO.listaUtentiDiTipoPsicologo(null);
                return popolaListaPsicologiBean(listaPsicologi);
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }



    private void aggiornaSessioneConDatiAnagraficiPsicologo(Psicologo psicologo) {
        Paziente pazienteCorrente=SessionManager.getInstance().getPazienteCorrente();

        pazienteCorrente.getPsicologo().setNome(psicologo.getNome());
        pazienteCorrente.getPsicologo().setCognome(psicologo.getCognome());
        pazienteCorrente.getPsicologo().setGenere(psicologo.getGenere());
    }

    private void aggiornaSessioneConDatiPsicologo(Psicologo psicologo) {
        Paziente pazienteCorrente=SessionManager.getInstance().getPazienteCorrente();

        pazienteCorrente.getPsicologo().setNomeStudio(psicologo.getNomeStudio());
        pazienteCorrente.getPsicologo().setCostoOrario(psicologo.getCostoOrario());
    }

    private List<PsicologoBean> popolaListaPsicologiBean(List<Psicologo> listaPsicologi) {
        List<PsicologoBean> listaBeans = new ArrayList<>();
        for (Psicologo psi : listaPsicologi) {
            listaBeans.add(new PsicologoBean(
                    psi.getUsername(),
                    psi.getNome(),
                    psi.getCognome(),
                    psi.getGenere()
            ));
        }
        return listaBeans;
    }


    public void salvaRichiestaAppuntamento(AppuntamentiBean appuntamentiBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();

        appuntamentiBean.getPaziente().setUsername(SessionManager.getInstance().getPazienteCorrente().getUsername());
        Appuntamento appuntamento= new Appuntamento(appuntamentiBean.getData(),
                appuntamentiBean.getOra(),
                new Paziente(appuntamentiBean.getPaziente().getUsername()),
                new Psicologo(appuntamentiBean.getPsicologo().getUsername()));

        try {
            appuntamentoDAO.insertRichiestaAppuntamento(appuntamento);

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }

    }

    public PsicologoBean getInfoPsicologo(PsicologoBean psicologoSelezionato) throws EccezioneDAO {
        Paziente paziente= SessionManager.getInstance().getPazienteCorrente();
        Psicologo psicologoPazienteCorrente=paziente.getPsicologo();

        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        PsicologoDAO psicologoDAO= daoFactoryFacade.getPsicologoDAO();

        try {
            Psicologo psicologoDaUsare;

            if (ValidatoreSessione.psicologoDelPazienteCorrente(SessionManager.getInstance().getPazienteCorrente().getPsicologo())) {
                if (ValidatoreSessione.controllaPresenzaDatiStudioPsicologo( SessionManager.getInstance().getPazienteCorrente().getPsicologo())) {
                    psicologoDaUsare = psicologoPazienteCorrente;
                } else {
                    psicologoDaUsare = psicologoDAO.getInfoPsicologo(psicologoPazienteCorrente);
                    aggiornaSessioneConDatiPsicologo(psicologoDaUsare);
                }
            } else {
                psicologoDaUsare = psicologoDAO.getInfoPsicologo(new Psicologo(psicologoSelezionato.getUsername()));
            }

            psicologoSelezionato.setNomeStudio(psicologoDaUsare.getNomeStudio());
            psicologoSelezionato.setCostoOrario(psicologoDaUsare.getCostoOrario());

            return psicologoSelezionato;
        }catch (EccezioneDAO e){
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public List<AppuntamentiBean> getListaRichieste() throws EccezioneDAO,IllegalArgumentException {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        UtenteDAO utenteDAO=daoFactoryFacade.getUtenteDAO();
        List<AppuntamentiBean> listaRichiesteBean=new ArrayList<>();

        try {
            List<Appuntamento> listaRichieste = appuntamentoDAO.trovaRichiesteAppuntamento(SessionManager.getInstance().getPsicologoCorrente());
            listaRichieste= utenteDAO.richiestaAppuntamentiInfoPaziente(listaRichieste);

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
            appuntamentoDAO.aggiornaStatoNotifica(new Appuntamento(richiestaAppuntamento.getIdAppuntamento()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public boolean nonDisponibile(AppuntamentiBean richiestaAppuntamentoSelezionata) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            if(!appuntamentoDAO.getDisp(richiestaAppuntamentoSelezionata.getIdAppuntamento(),SessionManager.getInstance().getPsicologoCorrente())) {
                return true;
            }
            return !BoundaryMockAPICalendario.calendario();

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void richiestaAccettata(AppuntamentiBean richiestaAppuntamento) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        PazienteDAO pazienteDAO=daoFactoryFacade.getPazienteDAO();

        Appuntamento appuntamentoAccettato= new Appuntamento(richiestaAppuntamento.getIdAppuntamento(),SessionManager.getInstance().getPsicologoCorrente(),new Paziente(richiestaAppuntamento.getPaziente().getUsername()));
        try {
            appuntamentoDAO.accettaRichiesta(appuntamentoAccettato);
            pazienteDAO.aggiungiPsicologoAlPaziente(appuntamentoAccettato);

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

}
