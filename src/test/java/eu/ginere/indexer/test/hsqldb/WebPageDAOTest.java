package eu.ginere.indexer.test.hsqldb;

import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.junit.Test;

import eu.ginere.indexer.hsqldb.page.WebPageDAO;
import eu.ginere.test.hsqldb.dao.DataBase;

public class WebPageDAOTest extends TestCase {
	static final Logger log = Logger.getLogger(WebPageDAOTest.class);
		
	@Test
	public void testInsert() throws Exception {
		try {
			setDataSource();
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
			
			WebPageDAO.DAO.createTable();
			WebPageDAO.DAO.test();
            for (String url:URL){
                WebPageDAO.DAO.insert(url);
            }
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


