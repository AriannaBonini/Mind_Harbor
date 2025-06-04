package com.example.mindharbor.dao.memoria;

import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.enumerazioni.TipoAppuntamento;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Utente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppuntamentoDAOMemoria implements AppuntamentoDAO {
    private final List<Appuntamento> appuntamentiInMemoria=new ArrayList<>();

    public List<Appuntamento> trovaAppuntamentiPaziente(Appuntamento appuntamento) throws EccezioneDAO {
        try {
            List<Appuntamento> listaAppuntamentiPaziente = new ArrayList<>();
            LocalDate oggi = LocalDate.now();

            boolean inProgramma = appuntamento.getTipoAppuntamento()== TipoAppuntamento.IN_PROGRAMMA;

            for (Appuntamento app : appuntamentiInMemoria) {
                boolean stessoPaziente = app.getPaziente().getUsername().equals(appuntamento.getPaziente().getUsername());
                boolean appuntamentoConfermato = app.getStatoAppuntamento() == 1;

                if (!(stessoPaziente && appuntamentoConfermato)) {
                    continue;
                }

                boolean appuntamentoValido = inProgramma != app.getData().isBefore(oggi);

                if (appuntamentoValido) {
                    Appuntamento appuntamentoTrovato = new Appuntamento(app.getData(), app.getOra());

                    listaAppuntamentiPaziente.add(appuntamentoTrovato);
                }
            }

            return listaAppuntamentiPaziente;
        } catch (NullPointerException e) {
            throw new EccezioneDAO("Errore durante la ricerca degli appuntamenti del paziente: " + e.getMessage());
        }
    }


    @Override
    public List<Appuntamento> trovaAppuntamentiPsicologo(Appuntamento appuntamento) throws EccezioneDAO {
        List<Appuntamento> listaAppuntamentiPsicologo = new ArrayList<>();
        LocalDate oggi = LocalDate.now();

        boolean inProgramma = appuntamento.getTipoAppuntamento()== TipoAppuntamento.IN_PROGRAMMA;

        try {

            for (Appuntamento app : appuntamentiInMemoria) {
                boolean stessoPsicologo = app.getPsicologo().getUsername().equals(appuntamento.getPsicologo().getUsername());
                boolean appuntamentoConfermato = app.getStatoAppuntamento() == 1;

                if (!(stessoPsicologo && appuntamentoConfermato)) {
                    continue;
                }

                boolean appuntamentoValido = inProgramma != app.getData().isBefore(oggi);

                if (appuntamentoValido) {

                    Appuntamento appuntamentoTrovato = new Appuntamento(app.getData(), app.getOra());
                    appuntamentoTrovato.setPaziente(new Paziente(app.getPaziente().getUsername()));

                    listaAppuntamentiPsicologo.add(appuntamentoTrovato);
                }
            }

            return listaAppuntamentiPsicologo;
        } catch (NullPointerException e) {
            throw new EccezioneDAO("Errore durante la ricerca degli appuntamenti dello psicologo: " + e.getMessage());
        }
    }


    @Override
    public void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO {
        try {
            appuntamento.setStatoAppuntamento(0);
            appuntamento.setStatoNotificaPaziente(0);
            appuntamento.setStatoNotificaPsicologo(1);
            appuntamento.setIdAppuntamento(trovaIdMax());

            appuntamentiInMemoria.add(appuntamento);
        } catch (NullPointerException e) {
            throw new EccezioneDAO("Appuntamento o dati mancanti: " + e.getMessage());
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'inserimento in memoria: " + e.getMessage());
        }
    }

    private Integer trovaIdMax(){
        int maxId = 0;
        for (Appuntamento app : appuntamentiInMemoria) {
            if (app.getIdAppuntamento() > maxId) {
                maxId = app.getIdAppuntamento();
            }
        }
        maxId++;
        return maxId;
    }

    @Override
    public Appuntamento notificheNuoviAppuntamentiPaziente(Utente paziente) throws EccezioneDAO {
        Appuntamento appuntamento = new Appuntamento();
        int notificheNuoviAppuntamenti = 0;

        try {
            for (Appuntamento app : appuntamentiInMemoria) {
                if (app.getPaziente().getUsername().equals(paziente.getUsername()) && app.getStatoNotificaPaziente() == 1) {
                    notificheNuoviAppuntamenti++;
                }
            }

            appuntamento.setStatoNotificaPaziente(notificheNuoviAppuntamenti);

        } catch (NullPointerException e) {
            throw new EccezioneDAO("Dati assenti: " + e.getMessage());
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il conteggio delle notifiche per il paziente: " + e.getMessage());
        }

        return appuntamento;
    }

    @Override
    public Appuntamento notificheNuoviAppuntamentiPsicologo(Utente psicologo) throws EccezioneDAO {
        Appuntamento appuntamento = new Appuntamento();
        int notificheNuoviAppuntamenti = 0;

        try {
            for (Appuntamento app : appuntamentiInMemoria) {
                if (app.getPsicologo().getUsername().equals(psicologo.getUsername()) && app.getStatoNotificaPsicologo() == 1) {
                    notificheNuoviAppuntamenti++;
                }
            }

            appuntamento.setStatoNotificaPsicologo(notificheNuoviAppuntamenti);

        } catch (NullPointerException e) {
            throw new EccezioneDAO("Dati mancanti: " + e.getMessage());
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il conteggio delle notifiche per lo psicologo: " + e.getMessage());
        }

        return appuntamento;
    }



    @Override
    public List<Appuntamento> trovaRichiesteAppuntamento(Utente psicologo) throws EccezioneDAO {
        try {
            List<Appuntamento> richiesteAppuntamento = new ArrayList<>();

            for (Appuntamento app : appuntamentiInMemoria) {
                if (app.getPsicologo().getUsername().equals(psicologo.getUsername())
                        && app.getStatoAppuntamento() == 0) {

                    Appuntamento richiesta = new Appuntamento(app.getIdAppuntamento());

                    richiesta.setPaziente(new Paziente(app.getPaziente().getUsername()));
                    richiesta.setStatoNotificaPsicologo(app.getStatoNotificaPsicologo());

                    richiesteAppuntamento.add(richiesta);
                }
            }

            return richiesteAppuntamento;
        } catch (NullPointerException e) {
            throw new EccezioneDAO("Dati mancanti: " + e.getMessage());
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca richieste: " + e.getMessage());
        }
    }

    @Override
    public void aggiornaStatoNotifica(Appuntamento richiestaAppuntamento) throws EccezioneDAO {
        try {
            boolean trovato = false;

            for (Appuntamento app : appuntamentiInMemoria) {
                if (Objects.equals(app.getIdAppuntamento(), richiestaAppuntamento.getIdAppuntamento())) {
                    app.setStatoNotificaPsicologo(0);
                    trovato = true;
                    break;
                }
            }

            if (!trovato) {
                throw new EccezioneDAO( richiestaAppuntamento.getIdAppuntamento() + " id non trovato.");
            }

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'aggiornamento dello stato notifica: " + e.getMessage());
        }
    }

    @Override
    public Appuntamento getInfoRichiesta(Appuntamento richiestaAppuntamento) throws EccezioneDAO {
        try {
            for (Appuntamento app : appuntamentiInMemoria) {
                if (Objects.equals(app.getIdAppuntamento(), richiestaAppuntamento.getIdAppuntamento())) {
                    return new Appuntamento(app.getData(), app.getOra());
                }
            }
            throw new EccezioneDAO("Id appuntamento: " + richiestaAppuntamento.getIdAppuntamento() + " non presente.");
        } catch (Exception e) {
            throw new EccezioneDAO("Errore nel recupero informazioni richiesta: " + e.getMessage());
        }
    }

    @Override
    public void accettaRichiesta(Appuntamento appuntamento) throws EccezioneDAO {
        try {
            boolean trovato = false;
            for (Appuntamento app : appuntamentiInMemoria) {
                if (Objects.equals(app.getIdAppuntamento(), appuntamento.getIdAppuntamento())) {
                    app.setStatoAppuntamento(1);
                    app.setStatoNotificaPaziente(1);
                    trovato = true;
                    break;
                }
            }
            if (!trovato) {
                throw new EccezioneDAO("Appuntamento con id " + appuntamento.getIdAppuntamento() + " non trovato.");
            }

        } catch (Exception e) {
            throw new EccezioneDAO("Errore nell'aggiornamento della richiesta: " + e.getMessage());
        }
    }

    @Override
    public void eliminaRichiesteDiAppuntamentoPerAltriPsicologi(Appuntamento appuntamento) throws EccezioneDAO {
        try {
            String usernamePaziente = appuntamento.getPaziente().getUsername();
            String usernamePsicologoDaEscludere = appuntamento.getPsicologo().getUsername();

            /* Rimuove tutte le richieste del paziente per psicologi diversi da quello indicato*/
            appuntamentiInMemoria.removeIf(app ->
                    app.getPaziente().getUsername().equals(usernamePaziente) &&
                            !app.getPsicologo().getUsername().equals(usernamePsicologoDaEscludere)
            );
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'eliminazione delle richieste: " + e.getMessage());
        }
    }


    @Override
    public void eliminaRichiesta(Appuntamento appuntamento) throws EccezioneDAO {
        try {
            int idDaEliminare = appuntamento.getIdAppuntamento();

            boolean rimosso = appuntamentiInMemoria.removeIf(app -> app.getIdAppuntamento() == idDaEliminare);

            if (!rimosso) {
                throw new EccezioneDAO("Appuntamento con id " + idDaEliminare + " non trovato.");
            }
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'eliminazione della richiesta: " + e.getMessage());
        }
    }

    @Override
    public boolean getDisp(Appuntamento appuntamento) throws EccezioneDAO {
        try {
            Appuntamento target = null;
            for (Appuntamento app : appuntamentiInMemoria) {
                if (Objects.equals(app.getIdAppuntamento(), appuntamento.getIdAppuntamento()) && app.getPsicologo().getUsername().equals(appuntamento.getPsicologo().getUsername())) {
                    target = app;
                    break;
                }
            }
            if (target == null) {
                return false;
            }

            /*Ora controllo se esiste un appuntamento confermato (statoAppuntamento == 1)
            / con stesso psicologo, stessa data e ora, diverso dall'id controllato*/
            for (Appuntamento app : appuntamentiInMemoria) {
                if (app.getStatoAppuntamento() == 1
                        && app.getPsicologo().getUsername().equals(appuntamento.getPsicologo().getUsername())
                        && app.getData().equals(target.getData())
                        && app.getOra().equals(target.getOra())
                        && !Objects.equals(app.getIdAppuntamento(), appuntamento.getIdAppuntamento())) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il controllo disponibilità: " + e.getMessage());
        }
    }

    @Override
    public void aggiornaStatoNotificaPaziente(Utente paziente) throws EccezioneDAO {
        try {
            for (Appuntamento app : appuntamentiInMemoria) {
                if (app.getPaziente().getUsername().equals(paziente.getUsername())
                        && app.getStatoNotificaPaziente() == 1) {
                    app.setStatoNotificaPaziente(0);
                }
            }
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'aggiornamento dello stato notifica paziente: " + e.getMessage());
        }
    }

}
