package com.example.mindharbor.dao.memoria;

import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Terapia;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;

import java.util.ArrayList;
import java.util.List;

public class TerapiaDaoMemoria implements TerapiaDAO {
    private final List<Terapia> terapiaInMemoria = new ArrayList<>();

    @Override
    public void aggiungiTerapia(Terapia terapia) throws EccezioneDAO {
        Terapia terapiaDaInserire = new Terapia();
        try {
            TestPsicologico testPsicologico= new TestPsicologico(new java.sql.Date(terapia.getTestPsicologico().getData().getTime()),terapia.getTestPsicologico().getPsicologo(),terapia.getTestPsicologico().getPaziente());
            terapiaDaInserire.setTestPsicologico(testPsicologico);
            terapiaDaInserire.setTerapia(terapia.getTerapia());
            terapiaDaInserire.setDataTerapia(new java.sql.Date(terapia.getDataTerapia().getTime()));
            terapiaDaInserire.setNotificaPaziente(1);


            terapiaInMemoria.add(terapiaDaInserire);
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'inserimento della terapia: " + e.getMessage());
        }
    }

    @Override
    public List<Terapia> getTerapie(Utente utente) throws EccezioneDAO {
        try {
            List<Terapia> result = new ArrayList<>();
            for (Terapia t : terapiaInMemoria) {
                if (t.getTestPsicologico().getPaziente().getUsername().equals(utente.getUsername())) {
                    Terapia terapia = new Terapia(
                            new TestPsicologico(new Psicologo(t.getTestPsicologico().getPsicologo().getUsername())),
                            t.getTerapia(),
                            t.getDataTerapia()
                    );

                    result.add(terapia);
                }
            }
            aggiornaStatoNotificaPaziente(utente);
            return result;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore nel recupero terapie: " + e.getMessage());
        }
    }

    private void aggiornaStatoNotificaPaziente(Utente utente) throws EccezioneDAO {
        try {
            for (Terapia t : terapiaInMemoria) {
                if (t.getTestPsicologico().getPaziente().getUsername().equals(utente.getUsername())) {
                    t.setNotificaPaziente(0);
                }
            }
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'aggiornamento notifiche: " + e.getMessage());
        }
    }

    @Override
    public Integer getNuoveTerapie(Utente paziente) throws EccezioneDAO {
        try {
            int count = 0;
            for (Terapia t : terapiaInMemoria) {
                if (t.getTestPsicologico().getPaziente().getUsername().equals(paziente.getUsername())
                        && t.getNotificaPaziente() == 1) {
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore nel conteggio nuove terapie: " + e.getMessage());
        }
    }

    @Override
    public boolean controlloEsistenzaTerapiaPerUnTest(TestPsicologico testPsicologico) throws EccezioneDAO {
        try {
            for (Terapia terapia : terapiaInMemoria) {
                if (terapia.getTestPsicologico().getPaziente().getUsername().equals(testPsicologico.getPaziente().getUsername()) &&
                        terapia.getTestPsicologico().getPsicologo().getUsername().equals(testPsicologico.getPsicologo().getUsername()) &&
                        terapia.getTestPsicologico().getData().equals(testPsicologico.getData())) {
                    return true;
                }
            }
            return false;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore nel controllo delle terapie assegnate: " + e.getMessage());
        }
    }

}
