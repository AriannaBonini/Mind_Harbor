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
            TestPsicologico testPsicologico= new TestPsicologico(terapia.getTestPsicologico().getPsicologo(),terapia.getTestPsicologico().getPaziente());
            testPsicologico.setData(terapia.getTestPsicologico().getData());

            terapiaDaInserire.setTestPsicologico(testPsicologico);
            terapiaDaInserire.setTerapia(terapia.getTerapia());
            terapiaDaInserire.setDataTerapia(terapia.getDataTerapia());
            terapiaDaInserire.setNotificaPaziente(1);


            terapiaInMemoria.add(terapiaDaInserire);
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'inserimento della terapia: " + e.getMessage());
        }
    }

    @Override
    public List<Terapia> getTerapie(Utente paziente) throws EccezioneDAO {
        try {
            List<Terapia> result = new ArrayList<>();
            for (Terapia t : terapiaInMemoria) {
                if (t.getTestPsicologico().getPaziente().getUsername().equals(paziente.getUsername())) {
                    Terapia terapia = new Terapia(
                            new TestPsicologico(new Psicologo(t.getTestPsicologico().getPsicologo().getUsername())),
                            t.getTerapia(),
                            t.getDataTerapia()
                    );

                    result.add(terapia);
                }
            }
            aggiornaStatoNotificaPaziente(paziente);
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
    public Terapia getNuoveTerapie(Utente paziente) throws EccezioneDAO {
        Terapia terapia= new Terapia();
        int nuoveTerapie = 0;
        try {
            for (Terapia t : terapiaInMemoria) {
                if (t.getTestPsicologico().getPaziente().getUsername().equals(paziente.getUsername())
                        && t.getNotificaPaziente() == 1) {
                    nuoveTerapie++;
                }
            }
        } catch (Exception e) {
            throw new EccezioneDAO("Errore nel conteggio nuove terapie: " + e.getMessage());
        }
        terapia.setNotificaPaziente(nuoveTerapie);
        return terapia;
    }

    @Override
    public boolean esistenzaTerapiaPerUnTest(TestPsicologico testPsicologico) throws EccezioneDAO {
        try {
            for (Terapia terapia : terapiaInMemoria) {
                if (terapia.getTestPsicologico().getPaziente().getUsername().equals(testPsicologico.getPaziente().getUsername()) &&
                        terapia.getTestPsicologico().getPsicologo().getUsername().equals(testPsicologico.getPsicologo().getUsername()) &&
                        terapia.getTestPsicologico().getData().equals(testPsicologico.getData())) {
                    return false;
                }
            }
            return true;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore nel controllo delle terapie assegnate: " + e.getMessage());
        }
    }

}
