package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.sessione.SessionManager;

public class SetInfoUtente {
    public InfoUtenteBean getInfo() {
        SessionManager sessionManager = SessionManager.getInstance();

        return new InfoUtenteBean(
                sessionManager.getCurrentUser().getNome(),
                sessionManager.getCurrentUser().getCognome()
        );
    }

}
