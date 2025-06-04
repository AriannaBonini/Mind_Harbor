package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLTestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.sessione.ConnectionFactory;
import com.example.mindharbor.enumerazioni.TipoUtente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestPsicologicoDAOMySql extends QuerySQLTestPsicologicoDAO implements TestPsicologicoDAO {

    @Override
    public void assegnaTest(TestPsicologico test) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.ASSEGNA_TEST)) {

            stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            stmt.setString(2, test.getPsicologo().getUsername());
            stmt.setString(3, test.getPaziente().getUsername());
            stmt.setString(4, test.getTest());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public TestPsicologico getNotificaPazientePerTestAssegnato(Utente paziente) throws EccezioneDAO {
        /*Questo metodo ci ritorna il numero di test da notificare al paziente sulla sua Home.*/
        int numeroNuoviTestAssegnati;
        TestPsicologico testPsicologico= new TestPsicologico();

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.NOTIFICA_PAZIENTE_NUOVI_TEST, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    numeroNuoviTestAssegnati = rs.getInt(TOTAL);
                    testPsicologico.setStatoNotificaPaziente(numeroNuoviTestAssegnati);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return testPsicologico;
    }

    @Override
    public void modificaStatoNotificaTest(Utente utente, Paziente pazienteSelezionato) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try {
            String updateQuery;

            if (utente.getUserType().equals(TipoUtente.PAZIENTE)) {
                updateQuery = QuerySQLTestPsicologicoDAO.MODIFICA_STATO_NOTIFICA_PAZIENTE;

                try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, utente.getUsername());
                    stmt.executeUpdate();
                }

            } else {
                updateQuery = QuerySQLTestPsicologicoDAO.MODIFICA_STATO_NOTIFICA_PSICOLOGO;

                try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, utente.getUsername());
                    stmt.setString(2, pazienteSelezionato.getUsername());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public List<TestPsicologico> trovaListaTest(Utente paziente) throws EccezioneDAO {

        List<TestPsicologico> testPsicologicoList = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.TROVA_LISTA_TEST_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TestPsicologico test = new TestPsicologico(
                            (rs.getDate(3).toLocalDate()),
                            rs.getInt(1),
                            rs.getString(2));

                    test.setSvolto(rs.getInt(4));

                    testPsicologicoList.add(test);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return testPsicologicoList;
    }

    @Override
    public TestPsicologico trovaTestPassati(TestPsicologico testDaAggiungere) throws EccezioneDAO {
        TestPsicologico ultimoTestPsicologicoSvolto = new TestPsicologico();

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.TROVA_ULTIMO_TEST, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, testDaAggiungere.getPaziente().getUsername());
            stmt.setString(2, testDaAggiungere.getTest());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ultimoTestPsicologicoSvolto.setRisultato(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        aggiornaTestAppenaSvolto(testDaAggiungere);

        return ultimoTestPsicologicoSvolto;

    }

    private void aggiornaTestAppenaSvolto(TestPsicologico testDaAggiungere) throws EccezioneDAO {

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.AGGIORNA_TEST_SVOLTO)) {

            stmt.setInt(1, testDaAggiungere.getRisultato());
            stmt.setString(2, testDaAggiungere.getPaziente().getUsername());
            stmt.setDate(3, Date.valueOf(testDaAggiungere.getData()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    public TestPsicologico getNumTestSvoltiDaNotificare(Utente psicologo) throws EccezioneDAO {
        TestPsicologico testPsicologico= new TestPsicologico();
        int nuoviTestSvolti = 0;

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.NOTIFICA_PSICOLOGO_TEST_SVOLTI, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, psicologo.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nuoviTestSvolti = rs.getInt(TOTAL);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        testPsicologico.setStatoNotificaPsicologo(nuoviTestSvolti);
        return testPsicologico;
    }

    @Override
    public List<TestPsicologico> listaTestSvolti(TestPsicologico testPsicologico) throws EccezioneDAO {
        List<TestPsicologico> listaTestPsicologico= new ArrayList<>();
        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.NUMERO_TEST_SVOLTI_SENZA_PRESCRIZIONE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, testPsicologico.getPsicologo().getUsername());
            stmt.setString(2, testPsicologico.getPaziente().getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    TestPsicologico testPsicologicoTrovato= new TestPsicologico(new Psicologo(rs.getString(2)),new Paziente(rs.getString(3)));

                    testPsicologicoTrovato.setData((rs.getDate(1)).toLocalDate());

                    listaTestPsicologico.add(testPsicologicoTrovato);
                }
            }
        } catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return listaTestPsicologico;
    }

    @Override
    public List<TestPsicologico> listaTestSvoltiSenzaPrescrizione(TestPsicologico testPsicologico) throws EccezioneDAO {
        List<TestPsicologico> testSvolti = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.LISTA_TEST_SVOLTI_SENZA_PRESCRIZIONE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, testPsicologico.getPsicologo().getUsername());
            stmt.setString(2, testPsicologico.getPaziente().getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TestPsicologico testSvolto = new TestPsicologico(
                            (rs.getDate(1)).toLocalDate(),
                                rs.getInt(2),
                                rs.getString(3)
                        );
                        testSvolti.add(testSvolto);
                    }
                }
            } catch (SQLException e) {
                throw new EccezioneDAO(e.getMessage());
            }

        return testSvolti;
    }
    @Override
    public Paziente numTestSvoltiPerPaziente(Utente paziente) throws EccezioneDAO {
        Paziente numeroTestSvoltiPaziente=new Paziente();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.NUMERO_TEST_SVOLTI_PAZIENTE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, paziente.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    numeroTestSvoltiPaziente.setNumeroTest(rs.getInt(1));
                }

            }
        }catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return numeroTestSvoltiPaziente;
    }

    @Override
    public boolean getNumTestAssegnato(Utente paziente) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLTestPsicologicoDAO.TEST_ASSEGNATO_IN_DATA_ODIERNA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, paziente.getUsername());
            //Se la query restituisce TRUE, vuol dire che al paziente è stato già assegnato un test, quindi il metodo deve restituire FALSE.

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("test_assegnato") != 0;
                }
            }
        }catch (SQLException e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return false;
    }
}
