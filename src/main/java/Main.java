import java.sql.*;

public class Main {
    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "new_password");
            Statement statement = connection.createStatement();

            String q = "select * from reiziger";
            PreparedStatement pst = connection.prepareStatement(q);
            ResultSet result = pst.executeQuery();


            System.out.println("Alle Reizigers:");
            while (result.next()) {
                String tussenvoegsel = result.getString("tussenvoegsel");
                if (tussenvoegsel == null) {
                    tussenvoegsel = "";
                }

                System.out.println(
                        "\t#" + result.getString("reiziger_id") + " " +
                                result.getString("voorletters") + ". " +
                                tussenvoegsel + " " +
                                result.getString("achternaam") + " " +
                                "(" + result.getString("geboortedatum") + ")"
                );
            }
            connection.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }
}
