package eu.ginere.indexer.test.hsqldb;

import java.sql.SQLException;

import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.junit.Test;

import eu.ginere.indexer.test.hsqldb.dao.IndexerDAO;
import eu.ginere.test.hsqldb.dao.DataBase;

public class IndexerDAOTest extends TestCase {
	static final Logger log = Logger.getLogger(IndexerDAOTest.class);
		
	@Test
	public void testInsert() throws Exception {
		try {
			setDataSource();
			IndexerDAO.DAO.createTable();
			IndexerDAO.DAO.test();
			
		}catch (Exception e) {
			log.error("", e);
			throw e;
		}	
	}

	private void setDataSource() throws NamingException  {
//		try {
//			Class.forName( );
//			} catch (Exception e) {
//			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
//			e.printStackTrace();
//			return;
//			}
		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		datasource.setUrl("jdbc:hsqldb:mem:testdb");
//		bds.setUsername(username);
//		bds.setPassword(password);
		
//		JDBCCommonDataSource datasource=new JDBCCommonDataSource();
		
//		Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "SA", "");
		
        DataBase.initDatasource("hsqldb-YQOL",datasource);
        
	}



}


