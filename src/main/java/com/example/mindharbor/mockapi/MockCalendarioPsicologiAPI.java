package com.example.mindharbor.mockapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockCalendarioPsicologiAPI {
    private MockCalendarioPsicologiAPI(){}
    private static final WireMockServer wireMockServer = new WireMockServer(8080);

        public static void mockCalendarioAPI() {
            wireMockServer.start();
            stubFor(get(urlEqualTo("/disponibilita"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{\"disponibile\": \"si\"}")));

            stubFor(get(urlEqualTo("/disponibilita1"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{\"disponibile\": \"no\"}")));
        }

    public static void stopWireMockServer() {
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

}
