import java.sql.*;

public class Main {

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "new_password");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printReiziger(Connection connection) {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();

                // get reizigers
                String q = "select * from reiziger";
                PreparedStatement pst = connection.prepareStatement(q);
                ResultSet result = pst.executeQuery();

                // print all reizigers
                System.out.println("Alle Reizigers:");
                while (result.next()) {
                    // check tussenvoegsel for null
                    String tussenvoegsel = result.getString("tussenvoegsel");
                    if (tussenvoegsel == null) {
                        tussenvoegsel = "";
                    }

                    // print info
                    System.out.println(
                            "\t#" + result.getString("reiziger_id") + " " +
                                    result.getString("voorletters") + ". " +
                                    tussenvoegsel + " " +
                                    result.getString("achternaam") + " " +
                                    "(" + result.getString("geboortedatum") + ")"
                    );
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection connection = getConnection();
        printReiziger(connection);
        closeConnection(connection);
    }
}
