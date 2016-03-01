package com.theironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by ericweidman on 3/1/16.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        Main.createTables(conn);
        return conn;

    }

    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE users");
        stmt.execute("DROP TABLE stats");
        conn.close();


    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Eric", "12345");
        User user = Main.selectUser(conn, "Eric");
        endConnection(conn);
        assertTrue(user != null);
    }

    @Test
    public void testStats() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Eric", "12345");
        User user = Main.selectUser(conn, "Eric");
        Main.insertStat(conn, user.id, "Nash", "Birdie", "Win");
        Stat stat = Main.selectStat(conn, 1);
        endConnection(conn);
        assertTrue(stat != null);
    }

    @Test
    public void testSelectStats() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Eric", "12345");
        Main.insertUser(conn, "Jake", "45567");
        User eric = Main.selectUser(conn, "Eric");
        User jake = Main.selectUser(conn, "Jake");
        Main.insertStat(conn, eric.id, "Nash", "Birdie", "Win");
        Main.insertStat(conn, jake.id, "Ryu", "Ken", "Loss");
        ArrayList<Stat> update = Main.selectStats(conn, 1);
        endConnection(conn);
        assertTrue(update.size() == 1);
    }

    @Test
    public void testDelete() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Eric", "12345");
        User james = Main.selectUser(conn, "Eric");
        Main.insertStat(conn, james.id, "Nash", "Ryu", "Loss");
        Stat loss = Main.selectStat(conn, 1);
        Main.deleteStat(conn, 1);
        assertTrue(loss == null);

    }
}



