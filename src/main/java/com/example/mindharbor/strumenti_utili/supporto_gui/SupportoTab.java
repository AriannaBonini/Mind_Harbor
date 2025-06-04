package com.example.mindharbor.strumenti_utili.supporto_gui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SupportoTab {

    private SupportoTab(){/*Costruttore vuoto*/}

    public static void aggiuntoListenerTab(TabPane tabPane, Tab tab1, Tab tab2, Runnable tab1Action, Runnable tab2Action) {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tab1) {
                tab1Action.run();
            } else if (newTab == tab2) {
                tab2Action.run();
            }
        });
    }
}
