import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "new_password");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Get reiziger by geboortedatum
        System.out.println("[Test] ReizigerDAO.findByGeboortedatum(2002-12-03) geeft de volgende reizigers:");
        System.out.println(rdao.findByGbdatum("2002-12-03") + "\n");

        // Get reiziger by id
        System.out.println("[Test] ReizigerDAO.findById(2) geeft de volgende reiziger:");
        System.out.println(rdao.findById(2) + "\n");

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");

        rdao.save(sietske);

        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reiziger\n");

        // Update
        sietske.setAchternaam("Koning");
        System.out.println("[Test] Reiziger voor ReizigerDAO.Update(): ");
        System.out.println(rdao.findById(sietske.getId()));

        rdao.update(sietske);

        System.out.println("Reiziger na update:");
        System.out.println(rdao.findById(sietske.getId()) + "\n");

        // Delete
        reizigers = rdao.findAll();
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");

        rdao.delete(sietske);

        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection();

        ReizigerDAO rdao= new ReizigerDAOPsql(connection);
        testReizigerDAO(rdao);

        closeConnection(connection);
    }
}
