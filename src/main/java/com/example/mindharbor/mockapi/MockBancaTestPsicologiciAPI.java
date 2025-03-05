package com.example.mindharbor.mockapi;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockBancaTestPsicologiciAPI {
    private MockBancaTestPsicologiciAPI(){}
    private static final WireMockServer wireMockServer = new WireMockServer(8080);
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    public static void mockTestPiscologiciAPI() {
        wireMockServer.start();
        stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                        .withBody("[\"Test di personalità\", \"Test di Ansia\",\"Test di memoria\"]")));

        stubFor(get(urlEqualTo("/api/test-urls"))
                .willReturn(aResponse()
                    .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                    .withStatus(200)
                    .withBody("{\"Test di Personalità\": \"http://localhost:8080/api/contenuti/test1\", " +
                          "\"Test di Ansia\": \"http://localhost:8080/api/contenuti/test2\", " +
                          "\"Test di Memoria\": \"http://localhost:8080/api/contenuti/test3\"}")));


        stubFor(get(urlEqualTo("/api/contenuti/test1"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                        .withStatus(200)
                        .withBody("{\"nome\": \"Test di Personalità\", \"domande\": [\"Come ti senti di solito quando ti svegli al mattino?\", \"Qual è la tua reazione principale quando incontri un problema?\", " +
                                "\"Cosa provi quando sei in compagnia dei tuoi amici più stretti?\", \"Come reagisci quando ricevi un complimento?\" ," +
                                "\"Cosa pensi quando qualcuno ti critica o ti fa arrabbiare?\", \"Qual è la tua emozione predominante quando ti trovi in situazioni di stress o pressione?\"]}")));

        stubFor(get(urlEqualTo("/api/contenuti/test2"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                        .withStatus(200)
                        .withBody("{\"nome\": \"Test di Ansia\", \"domande\": [\"Come ti senti quando sei sotto stress?\", \"Come ti senti quando pensi a situazioni che ti causano ansia?\" ," +
                                "\"Riesci a identificare momenti in cui ti senti sopraffatto dall'ansia? Come ti senti in quei momenti?\", \"C'è qualcosa che puoi fare per calmarti quando ti senti ansioso? Come ti fa sentire questa azione? \"" +
                                "\"Come ti senti generalmente in questo momento?\" ]}")));

        stubFor(get(urlEqualTo("/api/contenuti/test3"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                        .withStatus(200)
                        .withBody("{\"nome\": \"Test di Memoria\", \"domande\": [\"Come ti senti quando fai fatica a ricordare le cose?\", \"Come ti senti quando pensi alla tua infanzia?\" , " +
                                                               " \"Come ti senti quando pensi alle persone che ami?\", \"Come ti senti quando pensi al tuo passato? \"]}")));

        stubFor(get(urlEqualTo("/api/contenuti/punteggitest1"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                        .withStatus(200)
                        .withBody("{\"felice\": \"3\", \"triste\":\"-1\", \"arrabbiato\" : \"1\"}")));

        stubFor(get(urlEqualTo("/api/contenuti/punteggitest2"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                        .withStatus(200)
                        .withBody("{\"felice\": \"2\", \"triste\":\"-2\", \"arrabbiato\" : \"0\"}")));

        stubFor(get(urlEqualTo("/api/contenuti/punteggitest3"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                        .withStatus(200)
                        .withBody("{\"felice\": \"1\", \"triste\":\"-3\", \"arrabbiato\" : \"-2\"}")));
    }

    public static void stopWireMockServer() {
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

}
