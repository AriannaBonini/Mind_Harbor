package com.example.mindharbor.strumenti_utili.cli_helper;

public class CodiciAnsi {

    private CodiciAnsi() {/*costruttore privato*/}
    public static final String ANSI_ROSSO = "\u001B[31m";
    public static final String ANSI_RIPRISTINA_ROSSO="\u001B[0m";
    public static final String ANSI_VERDE = "\u001B[32m";
    public static final String ANSI_VERDE_CHIARO = "\u001B[92m";
    public static final String ANSI_RIPRISTINA_VERDE_CHIARO = "\u001B[0m";

    public static final String ANSI_GRASSETTO = "\u001B[1m";

    public static final String ANSI_RIPRISTINA_GRASSETTO = "\033[0m";
    public static final String ANSI_SOTTOLINEATO = "\u001B[4m";
    public static final String TICCHETTA = "\u2714";

    public static final String ANSI_GRIGIO = "\u001B[37m";
    public static final String ANSI_RIPRISTINA_GRIGIO = "\u001B[0m";
    public static final String ANSI_QUADRATO_VERDE = ANSI_VERDE_CHIARO + "\u25A1" + ANSI_RIPRISTINA_VERDE_CHIARO;
    public static final String ANSI_QUADRATO_VERDE_CON_TICCHETTA = ANSI_VERDE_CHIARO + "\u2611" + ANSI_RIPRISTINA_VERDE_CHIARO;
    public static final String EMOJI_FELICE = "\uD83D\uDE00";
    public static final String EMOJI_TRISTE = "\uD83D\uDE22";
    public static final String EMOJI_ARRABBIATA = "\uD83D\uDE20";



}
