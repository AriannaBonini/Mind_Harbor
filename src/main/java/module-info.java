module com.example.mindharbor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires wiremock.jre8.standalone;
    requires com.opencsv;

    opens com.example.mindharbor to javafx.fxml;
    opens com.example.mindharbor.controller_grafici.gui.psicologo to javafx.fxml;
    opens com.example.mindharbor.controller_grafici.gui.paziente to javafx.fxml;

    exports com.example.mindharbor;
    exports com.example.mindharbor.controller_applicativi;
    opens com.example.mindharbor.controller_applicativi to javafx.fxml;
    exports com.example.mindharbor.beans;
    exports com.example.mindharbor.dao;
    exports com.example.mindharbor.eccezioni;
    exports com.example.mindharbor.model;
    exports com.example.mindharbor.tipo_utente;
    exports com.example.mindharbor.controller_applicativi.paziente;
    opens com.example.mindharbor.controller_applicativi.paziente to javafx.fxml;
    exports com.example.mindharbor.controller_applicativi.psicologo;
    opens com.example.mindharbor.controller_applicativi.psicologo to javafx.fxml;
    exports com.example.mindharbor.dao.mysql;

    exports com.example.mindharbor.controller_grafici.gui;
    opens com.example.mindharbor.controller_grafici.gui to javafx.fxml;

    exports com.example.mindharbor.controller_grafici.gui.psicologo;
    exports com.example.mindharbor.controller_grafici.gui.paziente;

    exports com.example.mindharbor.controller_grafici.cli.paziente;
    exports com.example.mindharbor.controller_grafici.cli.psicologo;
}