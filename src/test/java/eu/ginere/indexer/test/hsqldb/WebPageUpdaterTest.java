package eu.ginere.indexer.test.hsqldb;

import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.junit.Test;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.indexer.hsqldb.page.WebPageDAO;
import eu.ginere.indexer.hsqldb.page.WebPageUpdater;
import eu.ginere.indexer.manager.GlobalManager;
import eu.ginere.indexer.test.hsqldb.dao.IndexerDAO;
import eu.ginere.indexer.test.hsqldb.dao.TokenDAO;
import eu.ginere.test.hsqldb.dao.DataBase;

public class WebPageUpdaterTest extends TestCase {

	static final Logger log = Logger.getLogger(WebPageUpdaterTest.class);
	
	static {
		try {
		createDataBase();
		}catch (Exception e) {
			log.error("", e);
		}	
		
	}
	@Test
	public void testUpdateCount() throws Exception {
		try {
//			createDataBase();
//			
			
			TokenDAO.DAO.updateTokenCount("dgmarkt.indexer.yqol.dao.WebPage");
		}catch (Exception e) {
			log.error("", e);
			throw e;
		}	
	}
	
	@Test
	public void testUpdate() throws Exception {
		try {
//			createDataBase();
//			
			String URL[]={
	                "http://europa.eu/index_en.htm",
	                "http://europa.eu/about-eu/countries/index_en.htm",
	                "http://europa.eu/about-eu/facts-figures/index_en.htm",
	                "http://europa.eu/about-eu/institutions-bodies/index_en.htm",
	                "http://europa.eu/about-eu/basic-information/symbols/index_en.htm",
	                "http://europa.eu/about-eu/index_en.htm",
	                "http://europa.eu/eu-life/work-pensions/index_en.htm",                
	                "http://eur-lex.europa.eu/search.html?qid=1407938140285&text=2004/18/EC&scope=EURLEX&type=quick&lang=en"
				};
				
				WebPageDAO.DAO.test();
	            for (String url:URL){
	                WebPageDAO.DAO.insert(url);
	            }
			GlobalManager yqol=new GlobalManager(IndexerDAO.DAO, TokenDAO.DAO);
			
			WebPageUpdater updater=new WebPageUpdater(yqol);
			
			updater.update();
		}catch (Exception e) {
			log.error("", e);
			throw e;
		}	
	}

	private static void createDataBase() throws NamingException, DaoManagerException {
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
        
        TokenDAO.DAO.createTable();
        IndexerDAO.DAO.createTable();
        WebPageDAO.DAO.createTable();
	}
}
