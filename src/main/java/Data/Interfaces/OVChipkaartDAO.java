package Data.Interfaces;

import Domain.OVChipkaart;
import Domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    void setPdao(ProductDAO pdao);
    boolean save(OVChipkaart ovChipkaart) throws SQLException;
    boolean update(OVChipkaart ovChipkaart) throws SQLException;
    boolean delete(OVChipkaart ovChipkaart) throws SQLException;
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    List<OVChipkaart> findAll() throws SQLException;
    List<OVChipkaart> findById(int id) throws SQLException;

    List<OVChipkaart> findByProduct(int id) throws SQLException;
}
