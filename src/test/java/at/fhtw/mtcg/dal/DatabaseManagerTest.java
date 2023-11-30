package at.fhtw.mtcg.dal;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @Test
    void getConnection() throws SQLException {
        Connection connection = DatabaseManager.INSTANCE.getConnection();
        assertTrue(connection.isValid(0));
    }
}