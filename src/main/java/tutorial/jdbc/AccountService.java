package tutorial.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.language.XPath;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class AccountService {
	private static Logger log = Logger.getLogger(AccountService.class);
	private JdbcTemplate jdbc;

	public AccountService() {
	}

	public void setDataSource(DataSource ds) {
		jdbc = new JdbcTemplate(ds);
	}

	/**
	 * Adds a specific amount of money to a named account
	 * 
	 * @param name - account name
	 * @param amount - amount to add
	 */
	public void credit(@XPath("/transaction/transfer/receiver/text()") String name,
			@XPath("/transaction/transfer/amount/text()") String amount) {
		log.info("credit() called with args name = " + name + " and amount = " + amount);
		int origAmount = jdbc.queryForInt("select amount from accounts where name = ?", new Object[] { name });
		int newAmount = origAmount + Integer.parseInt(amount);
		jdbc.update("update accounts set amount = ? where name = ?", new Object[] { newAmount, name });
	}

	/**
	 * Subtract a specific amount of money from a named account.
	 * 
	 * @param name - account name
	 * @param amount - amount to add
	 */
	public void debit(@XPath("/transaction/transfer/sender/text()") String name, @XPath("/transaction/transfer/amount/text()") String amount) {
		log.info("debit() called with args name = " + name + " and amount = " + amount);
		int iamount = Integer.parseInt(amount);
		if (iamount > 100) {
			throw new IllegalArgumentException("Debit limit is 100");
		}
		int origAmount = jdbc.queryForInt("select amount from accounts where name = ?", new Object[] { name });
		int newAmount = origAmount - Integer.parseInt(amount);
		if (newAmount < 0) {
			throw new IllegalArgumentException("Not enough in account");
		}

		jdbc.update("update accounts set amount = ? where name = ?", new Object[] { newAmount, name });
	}
	public void dumpTable(Exchange ex) {
        log.info("dump() called");
        List<?> dump = jdbc.queryForList("select * from accounts");
        ex.getIn().setBody(dump.toString());
    }
}