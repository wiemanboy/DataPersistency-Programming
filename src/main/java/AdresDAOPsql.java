import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{

    Connection connection;

    public AdresDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        String q = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = connection.prepareStatement(q);

        pst.setInt(1, adres.getId());
        pst.setString(2, adres.getPostcode());
        pst.setString(3, adres.getHuisnummer());
        pst.setString(4, adres.getStraat());
        pst.setString(5, adres.getWoonplaats());
        pst.setInt(6, adres.getReizigerId());

        pst.execute();
        return true;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        String q = "UPDATE adres SET " +
                "postcode = ?," +
                "huisnummer = ?," +
                "straat = ?," +
                "woonplaats = ?" +
                "WHERE adres_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);

        pst.setString(1, adres.getPostcode());
        pst.setString(2, adres.getHuisnummer());
        pst.setString(3, adres.getStraat());
        pst.setString(4, adres.getWoonplaats());
        pst.setInt(5, adres.getId());

        pst.execute();
        return true;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        String q = "DELETE FROM adres WHERE adres_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, adres.getId());

        pst.execute();
        return true;
    }

    @Override
    public Adres findById(int id) throws SQLException {
        String q = "SELECT * FROM adres WHERE adres_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet result = pst.executeQuery();

        result.next();

        return new Adres(
                result.getInt("adres_id"),
                result.getString("postcode"),
                result.getString("huisnummer"),
                result.getString("straat"),
                result.getString("woonplaats"),
                result.getInt("reiziger_id")
        );
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String q = "SELECT * FROM adres WHERE reiziger_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1,reiziger.getId());
        ResultSet result = pst.executeQuery();
        result.next();
        return new Adres(result.getInt("adres_id"),
                result.getString("postcode"),
                result.getString("huisnummer"),
                result.getString("straat"),
                result.getString("woonplaats"),
                result.getInt("reiziger_id")
        );
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adresen = new ArrayList<>();

        String q = "SELECT * FROM adres";
        PreparedStatement pst = connection.prepareStatement(q);
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            adresen.add(new Adres(
                    result.getInt("adres_id"),
                    result.getString("postcode"),
                    result.getString("huisnummer"),
                    result.getString("straat"),
                    result.getString("woonplaats"),
                    result.getInt("reiziger_id")
                    )
            );
        }
        return adresen;
    }
}
