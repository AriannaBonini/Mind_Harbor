package com.example.mindharbor.dao.memoria;

import com.example.mindharbor.dao.PsicologoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Psicologo;

import java.util.HashMap;
import java.util.Map;

public class PsicologoDAOMemoria implements PsicologoDAO {
    private final Map<String, Psicologo> psicologiInMemoria = new HashMap<>();

    @Override
    public Psicologo getInfoPsicologo(Psicologo psicologo) throws EccezioneDAO {
        try {
            Psicologo p = psicologiInMemoria.get(psicologo.getUsername());
            if (p == null) {
                throw new EccezioneDAO("Psicologo non trovato: " + psicologo.getUsername());
            }

            return new Psicologo(p.getCostoOrario(),p.getNomeStudio());

        } catch (Exception e) {
            throw new EccezioneDAO("Errore nel recupero info psicologo: " + e.getMessage());
        }
    }

    @Override
    public void inserisciDatiPsicologo(Psicologo psicologo) throws EccezioneDAO {
        try {
            if (psicologiInMemoria.containsKey(psicologo.getUsername())) {
                throw new EccezioneDAO("Psicologo con username " + psicologo.getUsername() + " già presente");
            }
            Psicologo psicologoDaInserire= new Psicologo(psicologo.getUsername());
            psicologoDaInserire.setCostoOrario(psicologo.getCostoOrario());
            psicologoDaInserire.setNomeStudio(psicologo.getNomeStudio());

            psicologiInMemoria.put(psicologo.getUsername(), psicologoDaInserire);
        } catch (Exception e) {
            throw new EccezioneDAO("Errore nell'inserimento dei dati dello psicologo: " + e.getMessage(), e);
        }
    }


}
