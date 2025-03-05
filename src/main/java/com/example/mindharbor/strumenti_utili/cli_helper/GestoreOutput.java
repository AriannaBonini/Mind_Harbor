package com.example.mindharbor.strumenti_utili.cli_helper;

public class GestoreOutput {

    private GestoreOutput(){/*costruttore privato per evitare istanze*/}

    public static void stampaMessaggio(String messaggio){System.out.println(messaggio);}

    public static void stampaLogoLogin(){
        String asciiartMind = """
               /$$                 /$$
              |__/                | $$
 /$$$$$$/$$$$  /$$ /$$$$$$$   /$$$$$$$
| $$_  $$_  $$| $$| $$__  $$ /$$__  $$
| $$ \\ $$ \\ $$| $$| $$  \\ $$| $$  | $$
| $$ | $$ | $$| $$| $$  | $$| $$  | $$
| $$ | $$ | $$| $$| $$  | $$|  $$$$$$$
|__/ |__/ |__/|__/|__/  |__/ \\_______/
""";


        String asciiartHarbor = """
     /$$                           /$$
    | $$                          | $$
    | $$$$$$$   /$$$$$$   /$$$$$$ | $$$$$$$   /$$$$$$   /$$$$$$
    | $$__  $$ |____  $$ /$$__  $$| $$__  $$ /$$__  $$ /$$__  $$
    | $$  \\ $$  /$$$$$$$| $$  \\__/| $$  \\ $$| $$  \\ $$| $$  \\__/
    | $$  | $$ /$$__  $$| $$      | $$  | $$| $$  | $$| $$
    | $$  | $$|  $$$$$$$| $$      | $$$$$$$/|  $$$$$$/| $$
    |__/  |__/ \\_______/|__/      |_______/  \\______/ |__/
    """;
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_VERDE + asciiartMind + "\n" + CodiciAnsi.ANSI_VERDE_CHIARO + asciiartHarbor + CodiciAnsi.ANSI_RIPRISTINA_VERDE_CHIARO);
    }

    public static void pulisciPagina(){
        for (int i = 0; i < 100; i++){
            GestoreOutput.stampaMessaggio("\b");
        }
    }

    public static void separatore() {
        stampaMessaggio("");
        stampaMessaggio("-------------------------------------------------");
        stampaMessaggio("");
    }

    public static String stampaMessaggioConNotifica(String messaggio, boolean notifica) {
        if(notifica) {
            messaggio=CodiciAnsi.ANSI_VERDE_CHIARO + messaggio + CodiciAnsi.ANSI_RIPRISTINA_VERDE_CHIARO;
        }
        return messaggio;
    }
}

