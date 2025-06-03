package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLTerapiaDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.*;
import com.example.mindharbor.sessione.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TerapiaDAOMySql extends QuerySQLTerapiaDAO implements TerapiaDAO {

    @Override
    public void aggiungiTerapia(Terapia terapia) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.INSERISCI_TERAPIA)) {

            stmt.setString(1, terapia.getTestPsicologico().getPsicologo().getUsername());
            stmt.setString(2, terapia.getTestPsicologico().getPaziente().getUsername());
            stmt.setString(3, terapia.getTerapia());
            stmt.setDate(4, new java.sql.Date(terapia.getDataTerapia().getTime()));
            stmt.setDate(5, new java.sql.Date(terapia.getTestPsicologico().getData().getTime()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

    }

    @Override
    public List<Terapia> getTerapie(Utente utente) throws EccezioneDAO {
        List<Terapia> terapie = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.TERAPIE_PAZIENTE)) {

            stmt.setString(1, utente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Terapia terapia = new Terapia(
                            new TestPsicologico(new Psicologo(rs.getString(1))),
                            rs.getString(2),
                            rs.getDate(3)
                    );
                    terapie.add(terapia);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        aggiornaStatoNotificaPaziente(utente);

        return terapie;

    }

    private void aggiornaStatoNotificaPaziente(Utente utente) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.AGGIORNA_NOTIFICA_PAZIENTE)) {

            stmt.setString(1, utente.getUsername());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public Integer getNuoveTerapie(Utente paziente) throws EccezioneDAO {
        int count = 0;

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.NOTIFICHE_NUOVE_TERAPIE_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(TOTAL);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return count;
    }
    @Override
    public boolean controlloEsistenzaTerapiaPerUnTest(TestPsicologico testPsicologico) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.ESISTE_TERAPIA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, testPsicologico.getPaziente().getUsername());
            stmt.setString(2, testPsicologico.getPsicologo().getUsername());
            stmt.setDate(3, new java.sql.Date(testPsicologico.getData().getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }





}
