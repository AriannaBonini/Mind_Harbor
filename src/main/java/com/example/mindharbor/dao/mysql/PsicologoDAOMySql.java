package com.example.mindharbor.dao.mysql;

import com.example.mindharbor.dao.PsicologoDAO;
import com.example.mindharbor.dao.mysql.query_sql.QuerySQLPsicologoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.sessione.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PsicologoDAOMySql extends QuerySQLPsicologoDAO implements PsicologoDAO {

    @Override
    public Psicologo getInfoPsicologo(Psicologo psicologo) throws EccezioneDAO {
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(QuerySQLPsicologoDAO.INFO_PSICOLOGO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setString(1, psicologo.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    psicologo.setCostoOrario(rs.getInt(1));
                    psicologo.setNomeStudio(rs.getString(2));
                }
            }
        } catch (SQLException e) {
           throw new EccezioneDAO(e.getMessage());
        }

        return psicologo;
    }
}
