import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {

    boolean save(Reiziger reiziger, AdresDAO adao) throws SQLException;
    boolean update(Reiziger reiziger) throws SQLException;
    boolean delete(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id, AdresDAO adao) throws SQLException;
    List<Reiziger> findByGbdatum(String datum, AdresDAO adao) throws SQLException;
    List<Reiziger> findAll(AdresDAO adao) throws SQLException;
}
