package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLPazienteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.sessione.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PazienteDAOMySql extends QuerySQLPazienteDAO implements PazienteDAO {

    @Override
    public List<Paziente> trovaPazienti(Utente psicologo) throws EccezioneDAO {
        /*
         * Recupera dalla persistenza i dati anagrafici del paziente associato allo psicologo:
         * username, nome, cognome e genere.
         * <p>
         * Questo metodo viene utilizzato dallo psicologo per ottenere la lista completa dei suoi pazienti.
         *
         * @return una lista di pazienti con informazioni anagrafiche essenziali
         * @throws EccezioneDAO se si verifica un errore durante l'accesso ai dati
         */
        List<Paziente> pazienteList = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.TROVA_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, psicologo.getUsername());


            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paziente paziente = new Paziente(rs.getString(1));

                    pazienteList.add(paziente);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return pazienteList;
    }




    @Override
    public Paziente getInfoSchedaPersonale(Paziente pazienteSelezionato) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.INFO_SCHEDA_PERSONALE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, pazienteSelezionato.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Paziente(rs.getInt(1),rs.getString(2));
                }
            }

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return pazienteSelezionato;
    }

    @Override
    public Paziente checkAnniPaziente(Paziente pazienteCorrente) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();
        Paziente paziente=new Paziente();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.CHECK_ANNI_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, pazienteCorrente.getUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    paziente.setAnni(rs.getInt(1));
                }
                return paziente;
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public Psicologo getUsernamePsicologo(Utente paziente) throws EccezioneDAO {
        Psicologo psicologo= new Psicologo();
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.USERNAME_PSICOLOGO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    psicologo.setUsername(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return psicologo;
    }

    @Override
    public void aggiungiPsicologoAlPaziente(Appuntamento appuntamento) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.AGGIUNGI_PSICOLOGO_AL_PAZIENTE)) {

            stmt.setString(1, appuntamento.getPsicologo().getUsername());
            stmt.setString(2, appuntamento.getPaziente().getUsername());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public void inserisciDatiPaziente(Paziente paziente) throws EccezioneDAO{
        /*
         * Questo metodo inserisce i dati relativi al paziente nel database.
         * I dati inseriti includono gli anni e il paziente_username.
         */
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.INSERISCI_DATI_PAZIENTE)) {

            stmt.setString(1, paziente.getUsername());
            stmt.setInt(2, paziente.getAnni());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO("Errore durante l'inserimento dei dati del paziente: " + e.getMessage(), e);
        }

    }

 }

