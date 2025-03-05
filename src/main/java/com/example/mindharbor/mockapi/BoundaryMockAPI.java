package com.example.mindharbor.mockapi;

import com.example.mindharbor.beans.DomandeTestBean;
import com.example.mindharbor.beans.TestBean;
import com.example.mindharbor.strumenti_utili.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wiremock.net.minidev.json.JSONArray;
import wiremock.net.minidev.json.JSONObject;
import wiremock.net.minidev.json.JSONValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BoundaryMockAPI {

    private BoundaryMockAPI(){}
    private static final Logger logger = LoggerFactory.getLogger(BoundaryMockAPI.class);
    private static final String BASE_URL = "http://localhost:8080"; // Indirizzo del server WireMock
    public static List<TestBean> testPiscologici() {

        MockBancaTestPsicologiciAPI.mockTestPiscologiciAPI();

        List<TestBean> nomiTest = new ArrayList<>();
        TestBean nomeTestBean;

        String jsonResponse = HttpUtil.makeHttpRequest(BASE_URL + "/test", "GET");

        try {

            JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonResponse);
            for (Object obj : jsonArray) {
                String nomeTest = (String) obj;
                nomeTestBean=new TestBean(nomeTest);
                
                nomiTest.add(nomeTestBean);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        MockBancaTestPsicologiciAPI.stopWireMockServer();

        return nomiTest;
    }

    public static DomandeTestBean domandeTest(String nomeTest) {

        MockBancaTestPsicologiciAPI.mockTestPiscologiciAPI();

        DomandeTestBean domande=new DomandeTestBean();
        String jsonResponse = HttpUtil.makeHttpRequest(BASE_URL + "/api/test-urls", "GET");
        String urlTest=null;

        try {

            JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonResponse);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                String key = entry.getKey(); // Ottieni la chiave
                String value = (String) entry.getValue(); // Ottieni il valore

                if (key.equalsIgnoreCase(nomeTest)) {
                    urlTest = value;
                    break; // Esci dal ciclo se trovato
                }
            }
            domande.setDomande(trovaDomande(urlTest));
            domande.setPunteggi(trovaPunteggio(nomeTest));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        MockBancaTestPsicologiciAPI.stopWireMockServer();

        return domande;
    }

    private static List<String> trovaDomande(String urlTest) {

        String jsonResponse = HttpUtil.makeHttpRequest(urlTest, "GET");
        List<String> domande= new ArrayList<>();

        try {

            JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonResponse);

            JSONArray domandeArray = (JSONArray) jsonObject.get("domande");
            for (Object domanda : domandeArray) {
                domande.add((String) domanda);
            }

        }catch (Exception e) {
            logger.error(e.getMessage());
        }

        return domande;
    }

    private static List<Integer> trovaPunteggio(String nomeTest) {
        String urlPunteggi;
        List<Integer> punteggi = new ArrayList<>(Arrays.asList(0, 0, 0));

        if (nomeTest.equalsIgnoreCase("Test di Personalità")) {
            urlPunteggi = BASE_URL + "/api/contenuti/punteggitest1";
        } else if (nomeTest.equalsIgnoreCase("Test di Ansia")) {
            urlPunteggi = BASE_URL + "/api/contenuti/punteggitest2";
        } else {
            urlPunteggi = BASE_URL + "/api/contenuti/punteggitest3";
        }

        String jsonResponse = HttpUtil.makeHttpRequest(urlPunteggi, "GET");

        try {
            JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonResponse);

            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                String key = entry.getKey(); // Ottieni la chiave
                String valoreStringa = (String) entry.getValue(); // Ottieni il valore come stringa
                Integer valoreIntero = Integer.parseInt(valoreStringa); // Converti il valore in un intero

                // Imposta i valori nella lista punteggi in base alla chiave
                if (key.equalsIgnoreCase("felice")) {
                    punteggi.set(0, valoreIntero);
                } else if (key.equalsIgnoreCase("triste")) {
                    punteggi.set(1, valoreIntero);
                } else {
                    punteggi.set(2, valoreIntero);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return punteggi;
    }

}

