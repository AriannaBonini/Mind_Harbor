package com.example.mindharbor.controller_grafici_cli;

import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;

import java.util.Scanner;

public abstract class AbsGestoreInput implements InterfacciaControllerGraficiCLI {

    protected int opzioneScelta(int min, int max){
        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            GestoreOutput.stampaMessaggio("\nInserisci la tua scelta: ");
            choice = input.nextInt();
            if (choice >= min && choice <= max) {
                break;
            }
            GestoreOutput.stampaMessaggio("Scelta non valida\n");
        }
        return choice;
    }
}


