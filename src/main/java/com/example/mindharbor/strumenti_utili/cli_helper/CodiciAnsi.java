package com.example.mindharbor.strumenti_utili.cli_helper;

public class CodiciAnsi {

    private CodiciAnsi() {/*costruttore privato*/}
    public static final String ANSI_ROSSO = "\u001B[31m";
    public static final String ANSI_VERDE = "\u001B[32m";
    public static final String ANSI_VERDE_CHIARO = "\u001B[92m";
    public static final String ANSI_RIPRISTINA_VERDE_CHIARO = "\u001B[0m";

    public static final String ANSI_GRASSETTO = "\u001B[1m";

    public static final String ANSI_RIPRISTINA_GRASSETTO = "\033[0m";
    public static final String ANSI_SOTTOLINEATO = "\u001B[4m";
}
