import java.sql.*;
import java.util.Scanner;

public class EmployeeCRUD {
    private static final String JDBC_URL =
        "jdbc:mysql://localhost:3306/roshandb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root"; // change this

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found. Add mysql-connector jar.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            System.out.println("✅ Connected to MySQL Database.");

            boolean exit = false;
            while (!exit) {
                System.out.println("\n===== Employee Menu =====");
                System.out.println("1. Create Employee Table");
                System.out.println("2. Insert Employee");
                System.out.println("3. Retrieve by SSN");
                System.out.println("4. Update state MH -> TN");
                System.out.println("5. Delete employees from 'Gujrat'");
                System.out.println("6. Display all employees");
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        createTable(conn);
                        break;
                    case 2:
                        insertEmployee(conn, sc);
                        break;
                    case 3:
                        retrieveBySSN(conn, sc);
                        break;
                    case 4:
                        updateStateMHtoTN(conn);
                        break;
                    case 5:
                        deleteFromGujrat(conn);
                        break;
                    case 6:
                        displayAll(conn);
                        break;
                    case 7:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        sc.close();
        System.out.println("Program ended.");
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS Employee (" +
            "SSN VARCHAR(20) PRIMARY KEY, " +
            "Ename VARCHAR(100), " +
            "state VARCHAR(50), " +
            "salary DECIMAL(12,2))";
        Statement st = conn.createStatement();
        st.execute(sql);
        st.close();
        System.out.println("✅ Table created or already exists.");
    }

    private static void insertEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter SSN: ");
        String ssn = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter State: ");
        String state = sc.nextLine();
        System.out.print("Enter Salary: ");
        double salary = Double.parseDouble(sc.nextLine());

        String sql = "INSERT INTO Employee (SSN, Ename, state, salary) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, ssn);
        pst.setString(2, name);
        pst.setString(3, state);
        pst.setDouble(4, salary);
        pst.executeUpdate();
        pst.close();
        System.out.println("✅ Employee inserted successfully.");
    }

    private static void retrieveBySSN(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter SSN to search: ");
        String ssn = sc.nextLine();
        String sql = "SELECT * FROM Employee WHERE SSN = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, ssn);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            System.out.println("SSN: " + rs.getString("SSN"));
            System.out.println("Name: " + rs.getString("Ename"));
            System.out.println("State: " + rs.getString("state"));
            System.out.println("Salary: " + rs.getDouble("salary"));
        } else {
            System.out.println("No record found for SSN " + ssn);
        }
        rs.close();
        pst.close();
    }

    private static void updateStateMHtoTN(Connection conn) throws SQLException {
        String sql = "UPDATE Employee SET state = 'TN' WHERE state = 'MH'";
        PreparedStatement pst = conn.prepareStatement(sql);
        int rows = pst.executeUpdate();
        pst.close();
        System.out.println("✅ " + rows + " record(s) updated (MH -> TN).");
    }

    private static void deleteFromGujrat(Connection conn) throws SQLException {
        String sql = "DELETE FROM Employee WHERE state = 'Gujrat'";
        PreparedStatement pst = conn.prepareStatement(sql);
        int rows = pst.executeUpdate();
        pst.close();
        System.out.println("🗑️ " + rows + " record(s) deleted from Gujrat.");
    }

    private static void displayAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Employee";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\nAll Employee Records:");
        while (rs.next()) {
            System.out.printf("%s | %s | %s | %.2f%n",
                rs.getString("SSN"),
                rs.getString("Ename"),
                rs.getString("state"),
                rs.getDouble("salary"));
        }
        rs.close();
        st.close();
    }
}

