package tutorial.jdbc;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class CreateTable {
    private static Logger log = Logger.getLogger(CreateTable.class);

    protected DataSource dataSource;
    protected JdbcTemplate jdbc;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public CreateTable(DataSource ds) {
        log.info("CreateTable constructor called");
        setDataSource(ds);
        setUpTable();
    }
    
    public void setUpTable() {
        log.info("About to set up table...");
        jdbc = new JdbcTemplate(dataSource);
        jdbc.execute("create table accounts (name varchar(50), amount int)");
        jdbc.update("insert into accounts (name,amount) values (?,?)",
                new Object[] {"Major Clanger", 2000}
        );
        jdbc.update("insert into accounts (name,amount) values (?,?)",
                new Object[] {"Tiny Clanger", 100}
        );
        log.info("Table created");
    }
}