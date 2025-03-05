package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.beans.TestBean;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class NavigatorSingleton {
    private static NavigatorSingleton instance = null;
    protected Stage stg;
    private AppuntamentiBean appBean;
    private PazienteBean pazienteBean;
    private TestBean testBean;
    private PsicologoBean psicologoBean;

    protected NavigatorSingleton(Stage stg) {
        this.stg = stg;
    }

    protected NavigatorSingleton() {
        /**
         * Costruttore per la CLI
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

    /*public static synchronized NavigatorSingleton getInstanceForCLI() {
        if (NavigatorSingleton.instance == null) {
            NavigatorSingleton.instance = new NavigatorSingleton();
        }
        return instance;
    }*/

    public void gotoPage(String fxml) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Mind Harbor");
        stage.setResizable(false);
        stage.show();
    }

    public void setAppuntamentoBean(AppuntamentiBean app) {this.appBean =app;}
    public AppuntamentiBean getAppuntamentoBean() {return appBean;}
    public void eliminaAppuntamentoBean() {this.appBean =null;}


    public PazienteBean getPazienteBean() {return pazienteBean;}
    public void setPazienteBean(PazienteBean pazienteBean) {this.pazienteBean = pazienteBean;}
    //questo metodo viene utilizzato per caricare l'istanza di PazientiBean contenente il paziente selezionato dallo psicologo nella Lista dei suoi pazienti.
    public void eliminaPazienteBean() {this.pazienteBean=null;}


    public TestBean getTestBean() {return testBean;}
    public void setTestBean(TestBean testBean) {this.testBean=testBean;}
    public void eliminaTestBean(){this.testBean=null;}

    public PsicologoBean getPsicologoBean() {return psicologoBean;}
    public void setPsicologoBean(PsicologoBean psicologoBean) {this.psicologoBean=psicologoBean;}
    public void eliminaPsicologoBean(){this.psicologoBean=null;}
}
