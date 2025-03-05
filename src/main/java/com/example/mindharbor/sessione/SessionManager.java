package com.example.mindharbor.sessione;


import com.example.mindharbor.eccezioni.EccezioneSessioneUtente;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.strumenti_utili.UtenteWrapper;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
        private static SessionManager instance = null;

        protected List<UtenteWrapper> utenteLoggato;
        protected Utente utenteCorrente;
        protected String usernamePsicologo; //questo attributo viene utilizzato soltanto quando utenteCorrente.UserType=Paziente.

        public Utente getCurrentUser(){
            return utenteCorrente;
        }
        public List<UtenteWrapper> getLoggedUsers(){
            return utenteLoggato;
        }
        public String getUsernamePsicologo() {return usernamePsicologo;}

    protected SessionManager() {
            utenteLoggato = new ArrayList<>();
            utenteCorrente = null;
            usernamePsicologo = null; // Inizializzazione dell'attributo aggiunto
        }

        public static synchronized SessionManager getInstance(){
            if(SessionManager.instance == null)
                SessionManager.instance = new SessionManager();

            return instance;
        }

        public synchronized void login(Utente utente,String usernamePsicologo) throws EccezioneSessioneUtente {
            try {
                for (UtenteWrapper wrapper : utenteLoggato) {
                    if (wrapper.utente().getUsername().equals(utente.getUsername())) {
                        throw new EccezioneSessioneUtente("Utente già loggato");
                    }
                }
                utenteLoggato.add(new UtenteWrapper(utente, usernamePsicologo));
            } finally {
                utenteCorrente = utente;
                this.usernamePsicologo = usernamePsicologo;
            }
        }

        public synchronized void logout(){
            utenteLoggato.removeIf(wrapper -> wrapper.utente().equals(utenteCorrente));
            utenteCorrente = null;
            usernamePsicologo = null;// Reset dell'usernamePsicologo quando viene effettuato il logout
        }

        public synchronized Utente changeCurrentUser(String username){
            for (UtenteWrapper wrapper : utenteLoggato) {
                if (wrapper.utente().getUsername().equals(username)) {
                    utenteCorrente = wrapper.utente();
                    usernamePsicologo = wrapper.usernamePsicologo();
                    return utenteCorrente;
                }
            }
            return null;
        }
}


