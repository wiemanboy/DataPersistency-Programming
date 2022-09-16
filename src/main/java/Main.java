import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection;

    private static Connection getConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "new_password");
                System.out.println("/// Connection Opened ///");
            }
            else {
                System.out.println("/// Connection Already Opened ///");
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        try {
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
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static void testAdresDao(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        try {
            System.out.println("\n---------- Test AdresDAO -------------");

            // Haal alle adressen op uit de database
            List<Adres> adresList = adao.findAll();
            System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
            for (Adres a : adresList) {
                System.out.println(a);
            }
            System.out.println();

            // get adres from reiziger
            System.out.println("AdresDAO.findbyReiziger(1) gives: ");
            System.out.println(adao.findByReiziger(rdao.findById(1)) + "\n");

            // Maak een nieuwe reiziger aan en persisteer deze in de database
            adresList = adao.findAll();
            System.out.print("[Test] Eerst " + adresList.size() + " adressen, na AdresDAO.save() ");
            Adres adres = new Adres(69, "1212LK", "3A", "Brugweg", "Utrecht");

            String gbdatum = "1981-03-14";
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum), adres);
            rdao.save(sietske);

            adresList = adao.findAll();
            System.out.println(adresList.size() + " adressen\n");

            // Update Adres
            adres.setHuisnummer("3");
            System.out.println("[Test] adressen voor AdresDAO.Update(): ");
            System.out.println(adao.findById(sietske.getAdres().getId()));

            adao.update(adres);

            System.out.println("Adressen na update:");
            System.out.println(adao.findById(adres.getId()) + "\n");

            // Delete Adres
            adresList = adao.findAll();
            System.out.print("[Test] Eerst " + adresList.size() + " adressen, na AdresDAO.delete() ");

            // Delete Reiziger and Adres
            rdao.delete(sietske);

            adresList = adao.findAll();
            System.out.println(adresList.size() + " adressen\n");
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("/// Connection Closed ///");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("/// Connection Already Closed ///");
        }
    }

    public static void main(String[] args) throws SQLException {
        getConnection();

        AdresDAO adao= new AdresDAOPsql(connection);
        ReizigerDAO rdao= new ReizigerDAOPsql(connection, adao);

        testReizigerDAO(rdao);
        testAdresDao(adao,rdao);

        closeConnection(connection);
    }
}
