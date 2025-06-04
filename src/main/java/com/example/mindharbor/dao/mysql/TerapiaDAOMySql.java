package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLTerapiaDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.*;
import com.example.mindharbor.sessione.ConnectionFactory;
import java.sql.*;
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
            stmt.setDate(4, Date.valueOf(terapia.getDataTerapia()));
            stmt.setDate(5, Date.valueOf(terapia.getTestPsicologico().getData()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

    }

    @Override
    public List<Terapia> getTerapie(Utente paziente) throws EccezioneDAO {
        List<Terapia> terapie = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.TERAPIE_PAZIENTE)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Terapia terapia = new Terapia(
                            new TestPsicologico(new Psicologo(rs.getString(1))),
                            rs.getString(2),
                            (rs.getDate(3).toLocalDate())
                    );
                    terapie.add(terapia);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        aggiornaStatoNotificaPaziente(paziente);

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
    public Terapia getNuoveTerapie(Utente paziente) throws EccezioneDAO {
        Terapia terapia= new Terapia();
        int nuoveTerapie = 0;

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.NOTIFICHE_NUOVE_TERAPIE_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nuoveTerapie = rs.getInt(TOTAL);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        terapia.setNotificaPaziente(nuoveTerapie);
        return terapia;
    }
    @Override
    public boolean esistenzaTerapiaPerUnTest(TestPsicologico testPsicologico) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTerapiaDAO.ESISTE_TERAPIA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, testPsicologico.getPaziente().getUsername());
            stmt.setString(2, testPsicologico.getPsicologo().getUsername());
            stmt.setDate(3, Date.valueOf(testPsicologico.getData()));

            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next();
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }





}
