package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLAppuntamentoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.tipo_utente.UserType;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.sessione.ConnectionFactory;
import com.example.mindharbor.strumenti_utili.HelperDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppuntamentoDAOMySql extends QuerySQLAppuntamentoDAO implements HelperDAO, AppuntamentoDAO {

    @Override
    public List<Appuntamento>  trovaAppuntamentiPaziente(Utente paziente, String selectedTabName) throws EccezioneDAO {
        List<Appuntamento> appuntamentoPazienteList = new ArrayList<>();

        String sql = QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PAZIENTE;

        if (selectedTabName.equals("IN PROGRAMMA")) {
            sql += QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_IN_PROGRAMMA;
        } else {
            sql += QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PASSATI;
        }

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appuntamento appuntamento = new Appuntamento(
                            rs.getString(1),
                            rs.getString(2)
                    );

                    appuntamentoPazienteList.add(appuntamento);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return appuntamentoPazienteList;
    }

    @Override
    public List<Appuntamento> trovaAppuntamentiPsicologo(Utente psicologo, String selectedTabName) throws EccezioneDAO {
        List<Appuntamento> appuntamentoPsicologoList = new ArrayList<>();

        String sql = QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PSICOLOGO;

        if (selectedTabName.equals("IN PROGRAMMA")) {
            sql += QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_IN_PROGRAMMA;
        } else {
            sql += QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PASSATI;
        }


        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, psicologo.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                UtenteDAOMySql utenteDAOMySql = new UtenteDAOMySql();
                while (rs.next()) {
                    Utente paziente = utenteDAOMySql.trovaNomeCognome(new Utente(rs.getString(3)));

                    Appuntamento appuntamento = new Appuntamento(
                            rs.getString(1),
                            rs.getString(2),
                            new Paziente(paziente.getUsername(), paziente.getNome(), paziente.getCognome())
                    );

                    appuntamentoPsicologoList.add(appuntamento);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return appuntamentoPsicologoList;
    }

    @Override
    public void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.INSERISCI_RICHIESTA_APPUNTAMENTO)) {

            stmt.setDate(1, Date.valueOf(appuntamento.getData()));
            stmt.setString(2, appuntamento.getOra());
            stmt.setString(3, appuntamento.getPaziente().getUsername());
            stmt.setString(4, appuntamento.getPsicologo().getUsername());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public Integer getNumRicAppDaNotificare(Utente utente) throws EccezioneDAO {
        int count = 0;
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = createPreparedStatement(conn,utente);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("Total");
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return count;
    }

    @Override
    public List<Appuntamento> trovaRichiesteAppuntamento(Utente utente) throws EccezioneDAO {
        List<Appuntamento> richiesteAppuntamento = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.TROVA_RICHIESTE_APPUNTAMENTI_PSICOLOGO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, utente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appuntamento richiesta = new Appuntamento(
                            rs.getInt(2),
                            new Paziente(rs.getString(3)),
                            rs.getInt(1)
                    );

                    richiesteAppuntamento.add(richiesta);
                }
            }
            richiesteAppuntamento=new UtenteDAOMySql().richiestaAppuntamentiInfoPaziente(richiesteAppuntamento);

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return richiesteAppuntamento;
    }

    @Override
    public void updateStatoNotifica(Appuntamento richiestaAppuntamento) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.UPDATE_STATO_NOTIFICA_PSICOLOGO)) {

            stmt.setInt(1, richiestaAppuntamento.getIdAppuntamento());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public Appuntamento getInfoRichiesta(Appuntamento richiestaAppuntamento) throws EccezioneDAO {
        Appuntamento richiesta = null;

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.INFORMAZIONI_RICHIESTA_APPUNTAMENTO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, richiestaAppuntamento.getIdAppuntamento());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    richiesta = new Appuntamento(
                            rs.getString(1),
                            rs.getString(2)
                    );
                }
            }

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return richiesta;
    }

    @Override
    public void updateRichiesta(Appuntamento appuntamento) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.RICHIESTA_DI_APPUNTAMENTO_ACCETTATA)) {

            stmt.setInt(1, appuntamento.getIdAppuntamento());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        // Chiamata al metodo per aggiungere lo psicologo al paziente fuori dal blocco try-with-resources
        new PazienteDAOMySql().aggiungiPsicologoAlPaziente(appuntamento);
    }

    @Override
    public void eliminaRichiesteDiAppuntamentoPerAltriPsicologi(Appuntamento appuntamento) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.ELIMINA_RICHIESTE_DI_APPUNTAMENTO)) {

            stmt.setString(1, appuntamento.getPaziente().getUsername());
            stmt.setString(2,appuntamento.getPsicologo().getUsername());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

    }

    @Override
    public void eliminaRichiesta(Appuntamento appuntamento) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.ELIMINA_RICHIESTA_DI_APPUNTAMENTO)) {

            stmt.setInt(1, appuntamento.getIdAppuntamento());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public boolean getDisp(Integer idAppuntamento, Utente utente) throws EccezioneDAO {
        boolean disponibile = false;

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.CONTROLLA_DISPONIBILITA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, idAppuntamento);
            stmt.setString(2, utente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    disponibile = true;
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return disponibile;
    }

    @Override
    public void aggiornaStatoNotificaPaziente(Utente utente) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.UPDATE_STATO_NOTIFICA_PAZIENTE)) {

            stmt.setString(1, utente.getUsername());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }


    public PreparedStatement createPreparedStatement(Connection conn, Utente utente) throws EccezioneDAO {
        String sql;
        if (utente.getUserType().equals(UserType.PSICOLOGO)) {
            sql = QuerySQLAppuntamentoDAO.NUMERO_RICHIESTE_APPUNTAMENTI_DA_NOTIFICARE_PSICOLOGO;
        } else {
            sql = QuerySQLAppuntamentoDAO.NUMERO_NUOVI_APPUNTAMENTI_DA_NOTIFICARE_PAZIENTE;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, utente.getUsername());
            return stmt;
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }
}
