package Data;

import Data.Interfaces.OVChipkaartDAO;
import Data.Interfaces.ProductDAO;
import Domain.OVChipkaart;
import Domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection connection;
    private OVChipkaartDAO odao;

    public ProductDAOPsql(Connection connection, OVChipkaartDAO odao) {
        this.connection = connection;
        this.odao = odao;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String q = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";

        PreparedStatement pst = connection.prepareStatement(q);

        pst.setInt(1, product.getProductNummer());
        pst.setString(2, product.getNaam());
        pst.setString(3, product.getBeschrijving());
        pst.setDouble(4, product.getPrijs());

        pst.execute();

        for (OVChipkaart o : product.getOvChipkaartList()) {
            odao.save(o);
            q = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";

            pst = connection.prepareStatement(q);
            pst.setInt(1, o.getKaartNummer());
            pst.setInt(2, product.getProductNummer());

            pst.execute();
        }
        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String q = "UPDATE product SET " +
                "naam = ?," +
                "beschrijving = ?," +
                "prijs = ?" +
                "WHERE product_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);

        pst.setString(1, product.getNaam());
        pst.setString(2, product.getBeschrijving());
        pst.setDouble(3, product.getPrijs());
        pst.setInt(4, product.getProductNummer());

        pst.execute();

        List<OVChipkaart> OList = product.getOvChipkaartList();
        List<OVChipkaart> DBList = findById(product.getProductNummer()).getOvChipkaartList();


        for (OVChipkaart o : DBList){
            if (!OList.contains(o)){
                //delete
                q = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ? AND kaart_nummer = ?";
                pst = connection.prepareStatement(q);
                pst.setInt(1, product.getProductNummer());
                pst.setInt(2, o.getKaartNummer());

                pst.execute();

                odao.delete(o);
            }
        }

        for (OVChipkaart o : OList){
            if (!DBList.contains(o)){
                //save
                odao.save(o);
                q = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";

                pst = connection.prepareStatement(q);
                pst.setInt(1, o.getKaartNummer());
                pst.setInt(2, product.getProductNummer());

                pst.execute();
            }
        }

        //update
        for (OVChipkaart o : OList) {
            odao.update(o);
        }

        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        String q = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, product.getProductNummer());

        pst.execute();
        for (OVChipkaart o : product.getOvChipkaartList()) {
            odao.delete(o);
        }

        q = "DELETE FROM product WHERE product_nummer = ?";
        pst = connection.prepareStatement(q);
        pst.setInt(1, product.getProductNummer());

        pst.execute();

        return true;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();

        String q = "SELECT * FROM product";
        PreparedStatement pst = connection.prepareStatement(q);
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            Product product = new Product(
                    result.getInt("product_nummer"),
                    result.getString("naam"),
                    result.getString("beschrijving"),
                    result.getDouble("prijs")
            );
            products.add(product);

            q = "SELECT o.* FROM ov_chipkaart o\n" +
                    "JOIN ov_chipkaart_product op on o.kaart_nummer = op.kaart_nummer\n" +
                    "WHERE op.product_nummer = ?;";

            pst = connection.prepareStatement(q);
            pst.setInt(1, product.getProductNummer());
            ResultSet resulto = pst.executeQuery();

            while (resulto.next()){
                product.addOvChip(new OVChipkaart(
                        resulto.getInt("kaart_nummer"),
                        resulto.getDate("geldig_tot"),
                        resulto.getInt("klasse"),
                        resulto.getDouble("saldo"),
                        resulto.getInt("reiziger_id")
                ));
            }
        }
        return products;
    }

    @Override
    public Product findById(int id) throws SQLException {
        String q = "SELECT * FROM product WHERE product_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet resultp = pst.executeQuery();

        resultp.next();

        Product product = new Product(
                resultp.getInt("product_nummer"),
                resultp.getString("naam"),
                resultp.getString("beschrijving"),
                resultp.getDouble("prijs"));


        q = "SELECT o.* FROM ov_chipkaart o\n" +
        "JOIN ov_chipkaart_product op on o.kaart_nummer = op.kaart_nummer\n" +
        "WHERE op.product_nummer = ?;";

        pst = connection.prepareStatement(q);
        pst.setInt(1, resultp.getInt("product_nummer"));
        ResultSet resulto = pst.executeQuery();

        while (resulto.next()){
            product.addOvChip(new OVChipkaart(
                    resulto.getInt("kaart_nummer"),
                    resulto.getDate("geldig_tot"),
                    resulto.getInt("klasse"),
                    resulto.getDouble("saldo"),
                    resulto.getInt("reiziger_id")
            ));
        }

        return product;
    }

    public List<Product> findByOvChipkaart(OVChipkaart ovChip) throws SQLException {
        List<Product> products = new ArrayList<>();

        String q = "SELECT p.* FROM product p\n" +
                "JOIN ov_chipkaart_product op on op.product_nummer = p.product_nummer\n" +
                "JOIN ov_chipkaart o on o.kaart_nummer = op.kaart_nummer\n" +
                "WHERE op.kaart_nummer = ?;";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, ovChip.getKaartNummer());
        ResultSet result = pst.executeQuery();

        while (result.next()) {
            Product product = new Product(
                            result.getInt("product_nummer"),
                            result.getString("naam"),
                            result.getString("beschrijving"),
                            result.getDouble("prijs")
                    );
            products.add(product);

            q = "SELECT o.* FROM ov_chipkaart o\n" +
                    "JOIN ov_chipkaart_product op on o.kaart_nummer = op.kaart_nummer\n" +
                    "WHERE op.product_nummer = ?;";

            pst = connection.prepareStatement(q);
            pst.setInt(1, product.getProductNummer());
            ResultSet resulto = pst.executeQuery();

            while (resulto.next()){
                product.addOvChip(new OVChipkaart(
                        resulto.getInt("kaart_nummer"),
                        resulto.getDate("geldig_tot"),
                        resulto.getInt("klasse"),
                        resulto.getDouble("saldo"),
                        resulto.getInt("reiziger_id")
                ));
            }
        }
        return products;
    }
}
