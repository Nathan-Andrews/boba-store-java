import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Handler class responsible for managing login operations such as adding login
 * credentials and authenticating login attempts.
 */
public class loginHandler {

    /**
     * Default constructor for the loginHandler class.
     */
    loginHandler() {
    }

    /**
     * Adds login credentials to the database.
     *
     * @param loginKey The unique key used for login.
     * @param value    The type of account associated with the login key.
     * @return true if the login credentials are added successfully, false otherwise.
     */
    boolean addLoginToDatabase(String loginKey, int value) {
        String sql = """
            INSERT INTO Accounts (login_key, account_type)
            VALUES (?, ?);
                """;

        db database = db.getInstance();
        Connection conn = database.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, loginKey);
            preparedStatement.setInt(2, value);

            return preparedStatement.executeUpdate() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return false;
    }

    /**
     * Attempts to login by verifying the login key in the database.
     *
     * @param loginKey The unique key used for login.
     * @return The account type if the login is successful, -1 otherwise.
     */
    int tryLogin(String loginKey) {
        String sql = """
            SELECT account_type FROM Accounts
            WHERE login_key = ?;
                """;

        db database = db.getInstance();
        Connection conn = database.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, loginKey);

            ResultSet result = preparedStatement.executeQuery();
            if (!result.next())
                return -1;

            return result.getInt("account_type");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return -1;
    }

    /* 
    
    static {
        loginHandler l = new loginHandler();
        
        l.addLoginToDatabase("man1", 1);
        l.addLoginToDatabase("cash1", 0);
    }

    */
}
