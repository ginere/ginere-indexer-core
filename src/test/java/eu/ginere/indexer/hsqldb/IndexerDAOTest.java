package eu.ginere.indexer.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;


public class IndexerDAOTest extends TestCase {
	static final Logger log = Logger.getLogger(IndexerDAOTest.class);
		
	@Test
	public void testInsert() throws Exception {
		try {
			setDataSource();

			
		}catch (Exception e) {
			log.error("", e);
			throw e;
		}	
	}

	private void setDataSource() throws SQLException {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			} catch (Exception e) {
			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return;
			}
		
		Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "SA", "");
		
	}



}


