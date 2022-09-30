import Data.*;
import Data.Interfaces.AdresDAO;
import Data.Interfaces.OVChipkaartDAO;
import Data.Interfaces.ProductDAO;
import Data.Interfaces.ReizigerDAO;
import Domain.Adres;
import Domain.OVChipkaart;
import Domain.Product;
import Domain.Reiziger;

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
            System.out.println("\n---------- Test Data.Interfaces.ReizigerDAO -------------");

            // Haal alle reizigers op uit de database
            List<Reiziger> reizigers = rdao.findAll();
            System.out.println("[Test] Data.Interfaces.ReizigerDAO.findAll() geeft de volgende reizigers:");
            for (Reiziger r : reizigers) {
                System.out.println(r);
            }
            System.out.println();

            // Get reiziger by geboortedatum
            System.out.println("[Test] Data.Interfaces.ReizigerDAO.findByGeboortedatum(2002-12-03) geeft de volgende reizigers:");
            System.out.println(rdao.findByGbdatum("2002-12-03") + "\n");

            // Get reiziger by id
            System.out.println("[Test] Data.Interfaces.ReizigerDAO.findById(2) geeft de volgende reiziger:");
            System.out.println(rdao.findById(2) + "\n");
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static void testAdresDao(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        try {
            System.out.println("\n---------- Test Data.Interfaces.AdresDAO -------------");

            // Haal alle adressen op uit de database
            List<Adres> adresList = adao.findAll();
            System.out.println("[Test] Data.Interfaces.AdresDAO.findAll() geeft de volgende adressen:");
            for (Adres a : adresList) {
                System.out.println(a);
            }
            System.out.println();

            // get adres from reiziger
            System.out.println("Data.Interfaces.AdresDAO.findbyReiziger(1) gives: ");
            System.out.println(adao.findByReiziger(rdao.findById(1)) + "\n");

            // Maak een nieuwe reiziger aan en persisteer deze in de database
            adresList = adao.findAll();
            System.out.print("[Test] Eerst " + adresList.size() + " adressen, na Data.Interfaces.AdresDAO.save() ");
            Adres adres = new Adres(69, "1212LK", "3A", "Brugweg", "Utrecht");

            String gbdatum = "1981-03-14";
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum), adres);
            rdao.save(sietske);

            adresList = adao.findAll();
            System.out.println(adresList.size() + " adressen\n");

            // Update domain.Adres
            adres.setHuisnummer("3");
            System.out.println("[Test] adressen voor Data.Interfaces.AdresDAO.Update(): ");
            System.out.println(adao.findById(sietske.getAdres().getId()));

            adao.update(adres);

            System.out.println("Adressen na update:");
            System.out.println(adao.findById(adres.getId()) + "\n");

            // Delete domain.Adres
            adresList = adao.findAll();
            System.out.print("[Test] Eerst " + adresList.size() + " adressen, na Data.Interfaces.AdresDAO.delete() ");

            // Delete domain.Reiziger and domain.Adres
            rdao.delete(sietske);

            adresList = adao.findAll();
            System.out.println(adresList.size() + " adressen\n");
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static void testOVChipDao(OVChipkaartDAO odao, ReizigerDAO rdao) throws SQLException {
        try {
            System.out.println("\n---------- Test Data.Interfaces.OVChipkaartDAO -------------");

            // get all ovchipkaart
            List<OVChipkaart> kaarten = odao.findAll();
            System.out.println("[Test] Data.Interfaces.OVChipkaartDAO.findAll() geeft de volgende kaarten:");
            for (OVChipkaart o : kaarten) {
                System.out.println(o);
            }
            System.out.println();

            // get ovchipkaarten from reiziger
            System.out.println("Data.Interfaces.OVChipkaartDAO.findbyReiziger(2) gives: ");
            System.out.println(odao.findByReiziger(rdao.findById(2)) + "\n");

            // Maak een nieuwe reiziger aan en persisteer deze in de database
            Adres adres = new Adres(69, "1212LK", "3A", "Brugweg", "Utrecht");

            String gbdatum = "1981-03-14";
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum), adres);

            kaarten = odao.findAll();
            System.out.print("[Test] Eerst " + kaarten.size() + " kaarten, na Data.Interfaces.OVChipkaartDAO.save() ");

            String geldig = "2023-03-14";
            OVChipkaart ovChip = new OVChipkaart(12, Date.valueOf(geldig) , 2, 0 ,sietske.getId());

            OVChipkaart ovChip2 = new OVChipkaart(13, Date.valueOf(geldig) , 2, 0 ,sietske.getId());


            sietske.addOVChipkaart(ovChip);
            sietske.addOVChipkaart(ovChip2);

            rdao.save(sietske);

            kaarten = odao.findAll();
            System.out.println(kaarten.size() + " kaarten\n");

            // Update domain.Adres
            ovChip.setSaldo(5);
            System.out.println("[Test] ovChip voor Data.Interfaces.OVChipkaartDAO.Update(): ");
            System.out.println(odao.findById(ovChip.getKaartNummer()));

            odao.update(ovChip);

            System.out.println("ovChip na update:");
            System.out.println(odao.findById(ovChip.getKaartNummer()) + "\n");

            // delete ovchip
            kaarten = odao.findAll();
            System.out.print("[Test] Eerst " + kaarten.size() + " kaarten, na Data.Interfaces.OVChipkaartDAO.delete() ");

            odao.delete(ovChip);

            kaarten = odao.findAll();
            System.out.println(kaarten.size() + " kaarten\n");

            // delete reiziger
            kaarten = odao.findAll();
            System.out.print("[Test] Eerst " + kaarten.size() + " kaarten, na Data.Interfaces.ReizigerDAO.delete() ");

            rdao.delete(sietske);

            kaarten = odao.findAll();
            System.out.println(kaarten.size() + " kaarten\n");

        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static void testProductDAO(ProductDAO pdao, ReizigerDAO rdao){
        try {
            // make objects
            Adres adres = new Adres(69, "1212LK", "3A", "Brugweg", "Utrecht");
            String gbdatum = "1981-03-14";
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum), adres);
            rdao.save(sietske);
            String geldig = "2023-03-14";
            OVChipkaart ovChip = new OVChipkaart(1, Date.valueOf(geldig) , 2, 0 ,sietske.getId());
            OVChipkaart ovChip2 = new OVChipkaart(2, Date.valueOf(geldig) , 2, 0 ,sietske.getId());
            OVChipkaart ovChip3 = new OVChipkaart(2, Date.valueOf(geldig) , 2, 22 ,sietske.getId());
            Product product = new Product(7,"testKaart","test",0.0);

            product.addOvChip(ovChip);
            product.addOvChip(ovChip3);

            System.out.println("\n---------- Test Data.Interfaces.ProductDAO -------------");

            // get all products
            List<Product> products = pdao.findAll();
            System.out.println("[Test] Data.Interfaces.ProductDAO.findAll() geeft de volgende products:");
            for (Product o : products) {
                System.out.println(o);
            }
            System.out.println();

            //save
            products = pdao.findAll();
            System.out.print("[Test] Eerst " + products.size() + " products, na Data.Interfaces.ProductDAO.save() ");

            pdao.save(product);

            products = pdao.findAll();
            System.out.println(products.size() + " products\n");

            // get products from ovChipKaart
            products = pdao.findByOvChipkaart(ovChip);
            System.out.println("[Test] Data.Interfaces.ProductDAO.findByOvChipkaart() geeft de volgende products:");
            for (Product o : products) {
                System.out.println(o);
            }
            System.out.println();

            // Update
            product.setPrijs(7);
            System.out.println("[Test] product voor Data.Interfaces.ProductDAO.Update(): ");
            System.out.println(pdao.findById(product.getProductNummer()));

            product.addOvChip(ovChip2);
            ovChip.setSaldo(20);
            product.removeOvChip(ovChip3);
            pdao.update(product);

            System.out.println("product na update:");
            System.out.println(pdao.findById(product.getProductNummer()));
            System.out.println();

            //delete
            products = pdao.findAll();
            System.out.print("[Test] Eerst " + products.size() + " products, na Data.Interfaces.ProductDAO.delete() ");

            pdao.delete(product);

            products = pdao.findAll();
            System.out.println(products.size() + " products\n");


            rdao.delete(sietske);
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

        OVChipkaartDAO odao = new OVChipkaartDAOPsql(connection);
        ProductDAO pdao = new ProductDAOPsql(connection, odao);
        AdresDAO adao = new AdresDAOPsql(connection);
        ReizigerDAO rdao = new ReizigerDAOPsql(connection, adao, odao);

        //testReizigerDAO(rdao);
        //testAdresDao(adao,rdao);
        //testOVChipDao(odao,rdao);
        testProductDAO(pdao, rdao);

        closeConnection(connection);
    }
}
