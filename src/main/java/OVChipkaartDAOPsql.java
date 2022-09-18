import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    private Connection connection;

    public OVChipkaartDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        String q = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pst = connection.prepareStatement(q);

        pst.setInt(1, ovChipkaart.getKaartNummer());
        pst.setDate(2, ovChipkaart.getGelidgTot());
        pst.setInt(3, ovChipkaart.getKlasse());
        pst.setDouble(4, ovChipkaart.getSaldo());
        pst.setInt(5, ovChipkaart.getReizigerId());

        pst.execute();
        return true;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        String q = "UPDATE ov_chipkaart SET " +
                "geldig_tot = ?," +
                "klasse = ?," +
                "saldo = ?," +
                "reiziger_id = ?" +
                "WHERE kaart_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);

        pst.setDate(1, ovChipkaart.getGelidgTot());
        pst.setInt(2, ovChipkaart.getKlasse());
        pst.setDouble(3, ovChipkaart.getSaldo());
        pst.setInt(4, ovChipkaart.getReizigerId());
        pst.setInt(5, ovChipkaart.getKaartNummer());

        pst.execute();
        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, ovChipkaart.getKaartNummer());

        pst.execute();
        return true;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        List<OVChipkaart> kaarten = new ArrayList<OVChipkaart>();

        String q = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";

        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1,reiziger.getId());
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            kaarten.add(new OVChipkaart(
                            result.getInt("kaart_nummer"),
                            result.getDate("geldig_tot"),
                            result.getInt("klasse"),
                            result.getDouble("saldo"),
                            result.getInt("reiziger_id")
                    )
            );
        }
        return kaarten;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> kaarten = new ArrayList<>();

        String q = "SELECT * FROM ov_chipkaart";
        PreparedStatement pst = connection.prepareStatement(q);
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            kaarten.add(new OVChipkaart(
                    result.getInt("kaart_nummer"),
                    result.getDate("geldig_tot"),
                    result.getInt("klasse"),
                    result.getDouble("saldo"),
                    result.getInt("reiziger_id")
                    )
            );
        }
        return kaarten;
    }

    @Override
    public List<OVChipkaart> findById(int id) throws SQLException {
        List<OVChipkaart> kaarten = new ArrayList<>();

        String q = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            kaarten.add(new OVChipkaart(
                            result.getInt("kaart_nummer"),
                            result.getDate("geldig_tot"),
                            result.getInt("klasse"),
                            result.getDouble("saldo"),
                            result.getInt("reiziger_id")
                    )
            );
        }
        return kaarten;
    }
}
