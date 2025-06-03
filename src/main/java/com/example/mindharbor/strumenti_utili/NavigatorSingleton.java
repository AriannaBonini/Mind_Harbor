package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.beans.TestBean;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class NavigatorSingleton {
    private static NavigatorSingleton instance = null;
    protected Stage stg;

    protected NavigatorSingleton(Stage stg) {
        this.stg = stg;
    }

    protected NavigatorSingleton() {
        /*
          Costruttore per la CLI
         */
    }


    public static synchronized NavigatorSingleton getInstance(Stage stg){
        if(NavigatorSingleton.instance == null)
            NavigatorSingleton.instance = new NavigatorSingleton(stg);

        return instance;
    }

    public static synchronized NavigatorSingleton getInstance() {
        if (NavigatorSingleton.instance == null) {
            NavigatorSingleton.instance = new NavigatorSingleton();
        }
        return instance;
    }

    public Object gotoPage(String fxml) throws IOException {
        return gotoPage(fxml, null, null);
    }

    public Object gotoPage(String fxml, Object controllerApplicativo) throws IOException {
        return gotoPage(fxml, controllerApplicativo, null);
    }

    public Object gotoPage(String fxml, Object controllerApplicativo, Object parametri) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        Object controllerGrafico = loader.getController();

        if (controllerGrafico instanceof RicevitoreControllerApplicativo && controllerApplicativo != null) {
            ((RicevitoreControllerApplicativo) controllerGrafico).setControllerApplicativo(controllerApplicativo);
        }

        if (controllerGrafico instanceof RicevitoreParametri && parametri != null) {
            ((RicevitoreParametri) controllerGrafico).setParametri(parametri);
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Mind Harbor");
        stage.setResizable(false);
        stage.show();

        return controllerGrafico;
    }

    public void closeStage(javafx.scene.Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
