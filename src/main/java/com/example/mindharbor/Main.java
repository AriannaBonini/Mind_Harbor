package com.example.mindharbor;

import com.example.mindharbor.controller_grafici_cli.ControllerGraficoLoginCLI;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.patterns.facade.TipoPersistenza;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        NavigatorSingleton navigator = NavigatorSingleton.getInstance(primaryStage);
        navigator.gotoPage("/com/example/mindharbor/Login.fxml");
    }


    public static void main(String[] args) {
        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();

        String tipoPersistenza = args.length > 0 ? args[0].toLowerCase() : "mysql";
        String tipoInterfaccia = args.length > 1 ? args[1].toLowerCase() : "gui";

        if ("mysql".equals(tipoPersistenza)) {
            daoFactoryFacade.setTipoPersistenza(TipoPersistenza.MYSQL);
        } else {
            daoFactoryFacade.setTipoPersistenza(TipoPersistenza.CSV);
        }

        if ("gui".equals(tipoInterfaccia)) {
            launch(args);
        } else {
            ControllerGraficoLoginCLI controllerGraficoLoginCLI= new ControllerGraficoLoginCLI();
            controllerGraficoLoginCLI.start();
        }
    }
}
