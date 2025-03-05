package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLPazienteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
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
        //Questo metodo viene utilizzato per prendere dalla persistenza lo username, il nome, il cognome e il genere del Paziente.
        //Viene utilizzato dallo psicologo per ottenere la lista dei suoi pazienti.
        List<Paziente> pazienteList = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.TROVA_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, psicologo.getUsername());
            //Con questa query sql ottengo lo username di tutti i pazienti di un determinato psicologo.

            UtenteDAOMySql utenteDaoMySql =new UtenteDAOMySql();
            Utente utente;

            TestPsicologicoDAOMySql testPsicologicoDAOMySql =new TestPsicologicoDAOMySql();
            Paziente numeroTestPaziente;



            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paziente paziente = new Paziente(rs.getString(1));
                    utente= utenteDaoMySql.trovaInfoUtente(paziente);

                    paziente.setNome(utente.getNome());
                    paziente.setCognome(utente.getCognome());
                    paziente.setGenere(utente.getGenere());


                    numeroTestPaziente= testPsicologicoDAOMySql.numTestSvoltiPerPaziente(paziente);
                    //con questa chiamata otteniamo il numero dei test svolti dal paziente da notificare allo psicologo
                    paziente.setNumeroTest(numeroTestPaziente.getNumeroTest());

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
        //questo metodo viene utilizzato per prendere dalla persistenza la diagnosi e l'età del paziente.

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.INFO_SCHEDA_PERSONALE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, pazienteSelezionato.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pazienteSelezionato.setAnni(rs.getInt(1));
                    pazienteSelezionato.setDiagnosi(rs.getString(2));
                }
            }

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return pazienteSelezionato;
    }

    @Override
    public boolean checkAnniPaziente(Paziente paziente) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.CHECK_ANNI_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());
            stmt.setInt(2, paziente.getAnni());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public String getUsernamePsicologo(Utente paziente) throws EccezioneDAO {
        String usernamePsicologo = null;
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPazienteDAO.USERNAME_PSICOLOGO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usernamePsicologo = rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return usernamePsicologo;
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
 }

