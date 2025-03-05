package com.example.mindharbor.beans;

import java.util.List;

public class DomandeTestBean {
    private List<String> domande;
    private List<Integer> punteggi;

    public DomandeTestBean() {//costruttore privato
    }

    public void setDomande(List<String> domande) {this.domande=domande;}

    public List<String> getDomande() {
        return domande;
    }

    public List<Integer> getPunteggi() {
        return punteggi;
    }
    public void setPunteggi(List<Integer> punteggi) {this.punteggi=punteggi;}

}
