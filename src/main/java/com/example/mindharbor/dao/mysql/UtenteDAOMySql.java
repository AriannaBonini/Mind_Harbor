package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLUtenteDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.enumerazioni.TipoUtente;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.sessione.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UtenteDAOMySql extends QuerySQLUtenteDAO implements UtenteDAO {

    @Override
    public Utente trovaUtente(Utente credenzialiUtenteLogin) throws EccezioneDAO {
        Utente utente=null;
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLUtenteDAO.CONTROLLO_CREDENZIALI, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, credenzialiUtenteLogin.getUsername());
            stmt.setString(2, credenzialiUtenteLogin.getPassword());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.first()) {
                    utente = getTipoUtente(rs);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return utente;
    }

    protected Utente getTipoUtente(ResultSet rs) throws SQLException {
        Utente utente;
        TipoUtente tipo;
        if (rs.getString(RUOLO).equals("Paziente")) {
            tipo = TipoUtente.PAZIENTE;
        } else {
            tipo = TipoUtente.PSICOLOGO;
        }
        utente = new Utente(
                rs.getString(USERNAME),
                rs.getString(NOME),
                rs.getString(COGNOME),
                tipo);
        return utente;
    }

    @Override
    public Utente trovaNomeCognome(Utente utente) throws EccezioneDAO {
        Utente infoUtente = null;

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLUtenteDAO.TROVA_NOME_COGNOME, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, utente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    infoUtente = new Utente("", rs.getString(1), rs.getString(2), "");
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return infoUtente;
    }

    @Override
    public List<Psicologo> listaUtentiDiTipoPsicologo(Utente psicologo) throws EccezioneDAO {
        /*
         * Questo metodo viene utilizzato nella prenotazione dell'appuntamento, quando il paziente deve visualizzare la lista degli psicologi, oppure, nel caso in cui
         *lui abbia già uno psicologo, solo quest'ultimo.
         *Il metodo ci ritorna il nome, il cognome, lo username e il genere dello psicologo o degli psicologi.
         */

        List<Psicologo> listaPsicologi = new ArrayList<>();
        String sql = QuerySQLUtenteDAO.LISTA_PSICOLOGI;

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = (psicologo!=null && psicologo.getUsername() != null)
                ? conn.prepareStatement(sql + " AND " + USERNAME + " = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                : conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            if (psicologo!=null && psicologo.getUsername() != null) {
                stmt.setString(1, psicologo.getUsername());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Psicologo psicologi = new Psicologo(rs.getString(3), rs.getString(1), rs.getString(2), rs.getString(4));
                    listaPsicologi.add(psicologi);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return listaPsicologi;
    }

    @Override
    public List<Appuntamento> richiestaAppuntamentiInfoPaziente(List<Appuntamento> richiesteAppuntamenti) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();
        try {
            for (Appuntamento appuntamento : richiesteAppuntamenti) {
                try (PreparedStatement stmt = conn.prepareStatement(QuerySQLUtenteDAO.TROVA_INFO_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

                    stmt.setString(1, appuntamento.getPaziente().getUsername());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            appuntamento.getPaziente().setNome(rs.getString(1));
                            appuntamento.getPaziente().setCognome(rs.getString(2));
                            appuntamento.getPaziente().setGenere(rs.getString(3));
                        }
                    }
                }
            }
        }catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return richiesteAppuntamenti;
    }

    @Override
    public Utente trovaInfoUtente(Utente paziente) throws EccezioneDAO {
        Utente infoUtente = null;

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLUtenteDAO.TROVA_INFO_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    infoUtente = new Utente("",rs.getString(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return infoUtente;
    }

    @Override
    public Boolean controllaUsernameERegistraNuovoUtente(Utente utente) throws EccezioneDAO{
        /*
         * Questo metodo viene utilizzato in fase di registrazione per verificare se lo username è già in uso.
         */
        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLUtenteDAO.CONTROLLA_USERNAME_FASE_REGISTRAZIONE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, utente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            }
            registraNuovoUtente(utente);
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return true;

    }

    private void registraNuovoUtente(Utente utente) throws EccezioneDAO {
        /*
         * Questo metodo viene utilizzato per inserire un nuovo utente nella tabella "Utente" nel database.
         */
        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLUtenteDAO.INSERISCI_NUOVO_UTENTE)) {

            stmt.setString(1, utente.getUsername());
            stmt.setString(2, utente.getPassword());
            stmt.setString(3, utente.getNome());
            stmt.setString(4, utente.getCognome());
            stmt.setString(5, utente.getUserType().toString());
            stmt.setString(6, utente.getGenere());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new EccezioneDAO("Errore durante l'inserimento del nuovo utente");
            }

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }


}
