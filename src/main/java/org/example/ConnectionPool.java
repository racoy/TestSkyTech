package org.example;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.example.DatabaseParameters.*;

public enum ConnectionPool {
    INSTANCE;

    private final ComboPooledDataSource cpds;
    {
        cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(DB_URL);
        cpds.setUser(USER);
        cpds.setPassword(PASS);
    }

    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
}
