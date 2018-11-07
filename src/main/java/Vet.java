import java.sql.*;

import static input.InputUtils.*;


public class Vet {


    private static final String url = "jdbc:sqlite:vet.sqlite"; // Where is database


    public static void main(String[] args) {

        int choice;
        String mainMenu =
                "Main Menu\n" +
                "---------\n" +
                "1: Add a dog\n" +
                "2: Search for a dog by name\n" +
                "3: Search for a dog by ID\n" +
                "4: Edit a dog's vaccination status\n" +
                "5: Delete a dog\n" +
                "6: Quit\n";

        do {

            choice = intInput(mainMenu);

            if (choice == 1) {
                addDog();
            } else if (choice == 2) {
                searchDogByName();
            } else if (choice == 3) {
                searchDogByID();
            } else if (choice == 4) {
                updateVax();
            } else if (choice == 5) {
                deleteDog();
            } else if (choice == 6) {
                System.out.println("Goodbye!");
            } else {
                System.out.println("Please enter 1, 2, 3, 4, 5, or 6!");
            }
        } while (choice != 6);

    }

    private static void addDog() {

        final String addSql = "insert into dogs (name, age, weight, vax) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(addSql)){

            String name = stringInput("enter dog name: ");
            int age = intInput("Enter dog's age: ");
            double weight = doubleInput("Enter dog's weight");
            boolean vax = yesNoInput("Is dog vaccinated? ");

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setDouble(3, weight);
            ps.setBoolean(4, vax);

            ps.execute();

        }

         catch (SQLException sqle) {
            System.out.println("Error adding new dog.");
            System.out.println(sqle.toString());
            sqle.printStackTrace();

        }

    }

    private static void searchDogByName() {

        final String searchSql = "SELECT * FROM dogs WHERE UPPER(name) LIKE UPPER(?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement searchStatement = connection.prepareStatement(searchSql)){

            String searchName = stringInput("Enter name to search for: ");

            searchStatement.setString(1, "%" + searchName + "%");
            ResultSet dogRs = searchStatement.executeQuery();

            if (!dogRs.isBeforeFirst()) {   // Returns false if no results
                System.out.println("Sorry, no dogs by that name found");
            } else {
                while (dogRs.next()) {
                    printDogInfo(dogRs);
                }

            }

        }

        catch (SQLException sqle) {
            System.out.println("Error adding new dog.");
            System.out.println(sqle.toString());
            sqle.printStackTrace();

        }

    }

    private static void searchDogByID() {

        final String searchSql = "SELECT * FROM dogs WHERE id LIKE ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement searchStatement = connection.prepareStatement(searchSql)){

            int searchID = intInput("Enter dog's ID: ");

            searchStatement.setInt(1, searchID);
            ResultSet dogRs = searchStatement.executeQuery();

            if (!dogRs.isBeforeFirst()) {   // Returns false if no results
                System.out.println("Sorry, no dogs by that ID found");
            } else {
                while (dogRs.next()) {
                    printDogInfo(dogRs);
                }

            }

        }

        catch (SQLException sqle) {
            System.out.println("Error adding new dog.");
            System.out.println(sqle.toString());
            sqle.printStackTrace();

        }

    }

    private static void updateVax() {

        final String updateVaxSql = "UPDATE dogs set VAX = ? WHERE name LIKE ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement ps = connection.prepareStatement(updateVaxSql)) {

            String name = stringInput("Enter Dog's name");
            boolean vax = yesNoInput("Is this dog vaccinated? ");

            ps.setBoolean(1, vax);
            ps.setString(2, name);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("Sorry, that dog was not found");
            } else {
                System.out.println("Database updated");
            }

        } catch (SQLException sqle) {
            System.out.println("Error adding new dog.");
            System.out.println(sqle.toString());
            sqle.printStackTrace();
        }
    }

        private static void deleteDog() {

            final String deleteDog = "DELETE FROM dogs WHERE name LIKE ?";

            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement ps = connection.prepareStatement(deleteDog)){

                String name = stringInput("Enter dog's name: ");

                ps.setString(1, name);

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated == 0) {
                    System.out.printf("Sorry, %s was not found!\n", name);
                } else {
                    System.out.println("Dog deleted.");
                }


            } catch (SQLException sqle){
                System.out.println("Error adding new dog.");
                System.out.println(sqle.toString());
                sqle.printStackTrace();
            }


        }


        private static void printDogInfo(ResultSet dogRs ) throws SQLException {
            int dogID = dogRs.getInt("id");
            String name = dogRs.getString("name");
            int age = dogRs.getInt("age");
            double weight = dogRs.getDouble("weight");
            boolean vax = dogRs.getBoolean("vax");
            System.out.printf("ID: %d Name %s, age %d, weight %.2f, vaccinated %s\n", dogID, name, age, weight, vax);

        }

}
