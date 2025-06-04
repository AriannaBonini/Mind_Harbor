package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLAppuntamentoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.enumerazioni.TipoAppuntamento;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.sessione.ConnectionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppuntamentoDAOMySql extends QuerySQLAppuntamentoDAO implements AppuntamentoDAO {

    @Override
    public List<Appuntamento>  trovaAppuntamentiPaziente(Appuntamento appuntamento) throws EccezioneDAO {
        List<Appuntamento> listaAppuntamentiPaziente = new ArrayList<>();
        String sql;

        if (appuntamento.getTipoAppuntamento()== TipoAppuntamento.IN_PROGRAMMA) {
            sql = QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PAZIENTE_IN_PROGRAMMA;
        } else {
            sql = QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PAZIENTE_PASSATI;
        }

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, appuntamento.getPaziente().getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appuntamento appuntamentoTrovato = new Appuntamento(
                            LocalDate.parse(rs.getString(1)),
                            LocalTime.parse(rs.getString(2))
                    );

                    listaAppuntamentiPaziente.add(appuntamentoTrovato);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return listaAppuntamentiPaziente;
    }

    @Override
    public List<Appuntamento> trovaAppuntamentiPsicologo(Appuntamento appuntamento) throws EccezioneDAO {
        List<Appuntamento> listaAppuntamentiPsicologo = new ArrayList<>();
        String sql;

        if (appuntamento.getTipoAppuntamento()==TipoAppuntamento.IN_PROGRAMMA) {
            sql = QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PSICOLOGO_IN_PROGRAMMA;
        } else {
            sql = QuerySQLAppuntamentoDAO.TROVA_APPUNTAMENTI_PSICOLOGO_PASSATI;
        }

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, appuntamento.getPsicologo().getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appuntamento appuntamentoTrovato = new Appuntamento(
                            LocalDate.parse(rs.getString(1)),
                            LocalTime.parse(rs.getString(2)));

                    appuntamentoTrovato.setPaziente(new Paziente(rs.getString(3)));

                    listaAppuntamentiPsicologo.add(appuntamentoTrovato);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return listaAppuntamentiPsicologo;
    }

    @Override
    public void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.INSERISCI_RICHIESTA_APPUNTAMENTO)) {

            stmt.setDate(1, Date.valueOf(appuntamento.getData()));
            stmt.setString(2, String.valueOf(appuntamento.getOra()));
            stmt.setString(3, appuntamento.getPaziente().getUsername());
            stmt.setString(4, appuntamento.getPsicologo().getUsername());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public Appuntamento notificheNuoviAppuntamentiPaziente(Utente paziente) throws EccezioneDAO {
        Appuntamento appuntamento = new Appuntamento();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.NUMERO_NUOVI_APPUNTAMENTI_DA_NOTIFICARE_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    appuntamento.setStatoNotificaPaziente(rs.getInt("Totale"));
                }
            }

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return appuntamento;
    }

    @Override
    public Appuntamento notificheNuoviAppuntamentiPsicologo(Utente psicologo) throws EccezioneDAO {
        Appuntamento appuntamento = new Appuntamento();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.NUMERO_RICHIESTE_APPUNTAMENTI_DA_NOTIFICARE_PSICOLOGO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, psicologo.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    appuntamento.setStatoNotificaPsicologo(rs.getInt("Totale"));
                }
            }

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return appuntamento;
    }




    @Override
    public List<Appuntamento> trovaRichiesteAppuntamento(Utente psicologo) throws EccezioneDAO {
        List<Appuntamento> listaRichiesteAppuntamento = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.TROVA_RICHIESTE_APPUNTAMENTI_PSICOLOGO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, psicologo.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appuntamento richiestaAppuntamento= new Appuntamento(rs.getInt(2));

                    richiestaAppuntamento.setPaziente(new Paziente(rs.getString(3)));
                    richiestaAppuntamento.setStatoNotificaPsicologo(rs.getInt(1));


                    listaRichiesteAppuntamento.add(richiestaAppuntamento);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return listaRichiesteAppuntamento;
    }

    @Override
    public void aggiornaStatoNotifica(Appuntamento richiestaAppuntamento) throws EccezioneDAO {

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
        Appuntamento richiesta = new Appuntamento();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.INFORMAZIONI_RICHIESTA_APPUNTAMENTO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, richiestaAppuntamento.getIdAppuntamento());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    richiesta = new Appuntamento(
                            LocalDate.parse(rs.getString(1)),
                            LocalTime.parse(rs.getString(2))
                    );
                }
            }

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return richiesta;
    }

    @Override
    public void accettaRichiesta(Appuntamento appuntamento) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.RICHIESTA_DI_APPUNTAMENTO_ACCETTATA)) {

            stmt.setInt(1, appuntamento.getIdAppuntamento());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

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
    public boolean getDisp(Appuntamento appuntamento) throws EccezioneDAO {
        boolean disponibile = false;

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.CONTROLLA_DISPONIBILITA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, appuntamento.getIdAppuntamento());
            stmt.setString(2, appuntamento.getPsicologo().getUsername());

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
    public void aggiornaStatoNotificaPaziente(Utente paziente) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLAppuntamentoDAO.UPDATE_STATO_NOTIFICA_PAZIENTE)) {

            stmt.setString(1, paziente.getUsername());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }
}
