package Data;

import Data.Interfaces.AdresDAO;
import Data.Interfaces.OVChipkaartDAO;
import Data.Interfaces.ReizigerDAO;
import Domain.OVChipkaart;
import Domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    Connection connection;
    AdresDAO adao;
    OVChipkaartDAO odao;

    public ReizigerDAOPsql(Connection connection, AdresDAO adao, OVChipkaartDAO odao) {
        this.connection = connection;
        this.adao = adao;
        this.odao = odao;
    }

    public boolean save(Reiziger reiziger) throws SQLException {
        String q = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pst = connection.prepareStatement(q);

        pst.setInt(1, reiziger.getId());
        pst.setString(2, reiziger.getVoorletters());
        pst.setString(3, reiziger.getTussenvoegsel());
        pst.setString(4, reiziger.getAchternaam());
        pst.setDate(5, reiziger.getGeboortedatum());

        pst.execute();

        adao.save(reiziger.getAdres());

        for (OVChipkaart o : reiziger.getOvChipkaartList()) {
            odao.save(o);
        }


        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String q = "UPDATE reiziger SET " +
                "voorletters = ?," +
                "tussenvoegsel = ?," +
                "achternaam = ?," +
                "geboortedatum = ?" +
                "WHERE reiziger_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);

        pst.setString(1, reiziger.getVoorletters());
        pst.setString(2, reiziger.getTussenvoegsel());
        pst.setString(3, reiziger.getAchternaam());
        pst.setDate(4, reiziger.getGeboortedatum());
        pst.setInt(5, reiziger.getId());

        pst.execute();
        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, reiziger.getId());

        adao.delete(adao.findByReiziger(reiziger));

        for (OVChipkaart o : reiziger.getOvChipkaartList()) {
            odao.delete(o);
        }

        pst.execute();
        return true;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet result = pst.executeQuery();

        result.next();

        Reiziger reiziger =  new Reiziger(
                result.getInt("reiziger_id"),
                result.getString("voorletters"),
                result.getString("tussenvoegsel"),
                result.getString("achternaam"),
                result.getDate("geboortedatum")
        );

        reiziger.setAdres(adao.findByReiziger(reiziger));
        reiziger.setOvChipkaartList(odao.findByReiziger(reiziger));

        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();

        String q = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setDate(1, Date.valueOf(datum));
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            Reiziger reiziger =  new Reiziger(
                    result.getInt("reiziger_id"),
                    result.getString("voorletters"),
                    result.getString("tussenvoegsel"),
                    result.getString("achternaam"),
                    result.getDate("geboortedatum")
            );

            reiziger.setAdres(adao.findByReiziger(reiziger));
            reiziger.setOvChipkaartList(odao.findByReiziger(reiziger));
            reizigers.add(reiziger);
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException{
        List<Reiziger> reizigers = new ArrayList<>();

        String q = "SELECT * FROM reiziger";
        PreparedStatement pst = connection.prepareStatement(q);
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            Reiziger reiziger =  new Reiziger(
                    result.getInt("reiziger_id"),
                    result.getString("voorletters"),
                    result.getString("tussenvoegsel"),
                    result.getString("achternaam"),
                    result.getDate("geboortedatum")
            );

            reiziger.setAdres(adao.findByReiziger(reiziger));
            reiziger.setOvChipkaartList(odao.findByReiziger(reiziger));
            reizigers.add(reiziger);
        }
        return reizigers;
    }
}
