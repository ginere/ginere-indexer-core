package eu.ginere.indexer.indexer;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import avem.jdbc.JdbcManager;
import avem.jdbc.backend.BackendManager;
import avem.jdbc.test.AbstractSQLDAOTest;


public class IndexerDAOTest extends AbstractSQLDAOTest {

	public IndexerDAOTest() {
//		super(IndexerDAO.DAO,AvemTest.CREATE_BACK_END);
		super(IndexerDAO.DAO,false);
	}

	static final Logger log = Logger.getLogger(IndexerDAOTest.class);


	static final String TYPE_A = "TYPE_A";
	static final String TYPE_B = "TYPE_B";
//	static final String FIELD = "FIELD";

	protected void setDataSource() throws Exception {
		String filePropertiesName=getFilePropertiesName();
		DataSource dataSource = JdbcManager.createMySQLDataSourceFromPropertiesFile(filePropertiesName);
		
		JdbcManager.setDataSource(dataSource);
		
		BackendManager.init();
	}

	protected String getFilePropertiesName() throws Exception {
		return "conf/jdbc.properties";
	}
	
	@Test
	public void testInsert() throws Exception {
		try {
			setDataSource();

			innerTestBackEnd();
			
			IndexerDAO.DAO.deleteAll();

			IndexerDAO.DAO.insert("pepe",TYPE_A,"1");
			IndexerDAO.DAO.insert("pepe",TYPE_A,"2");
			IndexerDAO.DAO.insert("pépé",TYPE_A,"3");
			IndexerDAO.DAO.insert("juan",TYPE_A,"4");
			IndexerDAO.DAO.insert("enrique",TYPE_A,"5");

			TestCase.assertEquals(3,IndexerDAO.DAO.count("pepe"));
			TestCase.assertEquals(4,IndexerDAO.DAO.count("%e%"));

			TestCase.assertEquals(1,IndexerDAO.DAO.count("enrique"));

			IndexerDAO.DAO.delete(TYPE_A, "1");
			TestCase.assertEquals(2,IndexerDAO.DAO.count("pepe"));
			TestCase.assertEquals(3,IndexerDAO.DAO.count("%e%"));

			TestCase.assertEquals(1,IndexerDAO.DAO.count("enrique"));

			
//			List<IndexerResult> list=IndexerDAO.DAO.search("pepe");
//			
//			TestCase.assertEquals(1, list.size());
			
		}catch (Exception e) {
			log.error("", e);
			throw e;
		}	
	}



}


