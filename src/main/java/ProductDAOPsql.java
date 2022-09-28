import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO{
    private Connection connection;

    public ProductDAOPsql(Connection connection) {
        this.connection = connection;
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
        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        String q = "DELETE FROM product WHERE product_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);
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
            products.add(new Product(
                            result.getInt("product_nummer"),
                            result.getString("naam"),
                            result.getString("beschrijving"),
                            result.getDouble("prijs")
                    )
            );
        }
        return products;
    }

    @Override
    public Product findById(int id) throws SQLException {
        String q = "SELECT * FROM product WHERE product_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet result = pst.executeQuery();

        result.next();

        return (new Product(
                result.getInt("product_nummer"),
                result.getString("naam"),
                result.getString("beschrijving"),
                result.getDouble("prijs")
        )
        );
    }
}
