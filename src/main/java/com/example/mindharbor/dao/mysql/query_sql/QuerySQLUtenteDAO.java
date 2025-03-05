package com.example.mindharbor.dao.mysql.query_sql;

public abstract class QuerySQLUtenteDAO {
    protected QuerySQLUtenteDAO(){/*Costruttore vuoto*/}
    protected static final String USERNAME = "Username";
    protected static final String NOME = "Nome";
    protected static final String COGNOME = "Cognome";
    protected static final String GENERE = "Genere";
    protected static final String PSW = "Password";
    protected static final String RUOLO = "Categoria";
    protected static final String TABELLA_UTENTE = "Utente";

    protected static final String CONTROLLO_CREDENZIALI="SELECT * FROM utente WHERE " + USERNAME + " = ? AND " + PSW + " = ?;";

    protected static final String TROVA_NOME_COGNOME="SELECT " + NOME + ", " + COGNOME + " " +
            "FROM " + TABELLA_UTENTE + " " +
            "WHERE " + USERNAME + " = ?";

    protected static final String LISTA_PSICOLOGI="SELECT " + NOME + " ," + COGNOME + " ," + USERNAME + " , " + GENERE + " " +
            "FROM " + TABELLA_UTENTE + " " +
            "WHERE " + RUOLO + " = 'Psicologo' ";

    protected static final String TROVA_INFO_PAZIENTE=" SELECT " + NOME + ", " + COGNOME + " , " + GENERE + " " +
            "FROM " + TABELLA_UTENTE + " " +
            "WHERE " + USERNAME + " = ?";


}
