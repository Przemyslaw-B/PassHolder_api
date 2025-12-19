package com.program.passholder.Database;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBConnection {
    private DataSource dataSource;

    public DBConnection(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection setConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
