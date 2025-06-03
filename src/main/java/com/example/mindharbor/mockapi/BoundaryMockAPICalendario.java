package com.example.mindharbor.mockapi;


import com.example.mindharbor.strumenti_utili.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wiremock.net.minidev.json.JSONObject;
import wiremock.net.minidev.json.JSONValue;


public class BoundaryMockAPICalendario {

    private BoundaryMockAPICalendario() {}
    private static final String BASE_URL = "http://localhost:8080";
    private static final Logger logger = LoggerFactory.getLogger(BoundaryMockAPICalendario.class);

    public static boolean calendario() {

        boolean disponibile = false;

        MockCalendarioPsicologiAPI.mockCalendarioAPI();
        double randomNumber = Math.random();
        String endpoint = (randomNumber < 0.5) ? "/disponibilita" : "/disponibilita1";

        String jsonResponse = HttpUtil.makeHttpRequest(BASE_URL + endpoint, "GET");

        try {
            JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonResponse);
            String valore = jsonObject.getAsString("disponibile");
            return valore.equalsIgnoreCase("si");

        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            MockCalendarioPsicologiAPI.stopWireMockServer();
        }

        return disponibile;
    }
}
