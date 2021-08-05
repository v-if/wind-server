package com.github.tkpark.utils;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DBUtil {

    private Connection con = null;

    public DBUtil (DataSource dataSource) throws SQLException {
        this.con = dataSource.getConnection();
        this.con.setAutoCommit(false);
    }

    public Connection getCon() {
        return this.con;
    }

}
