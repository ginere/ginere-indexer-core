package eu.ginere.indexer.indexer;
//package avem.indexer.indexer;
//
//import javax.sql.DataSource;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//import org.junit.Test;
//
//import avem.jdbc.JdbcManager;
//import avem.jdbc.backend.BackendManager;
//import avem.jdbc.test.AbstractSQLDAOTest;
//import avem.jdbc.test.AvemTest;
//
//public class GlobalIndexerDAOTest extends AbstractSQLDAOTest {
//
//	public GlobalIndexerDAOTest() {
//		super(GlobalIndexerDAO.DAO,AvemTest.CREATE_BACK_END);
////		super(GlobalIndexerDAO.DAO,true);
//	}
//
//	static final Logger log = Logger.getLogger(GlobalIndexerDAOTest.class);
//
//
//	static final String TYPE_A = "TYPE_A";
//	static final String TYPE_B = "TYPE_B";
////	static final String FIELD = "FIELD";
//
//	protected void setDataSource() throws Exception {
//		String filePropertiesName=getFilePropertiesName();
//		DataSource dataSource = JdbcManager.createMySQLDataSourceFromPropertiesFile(filePropertiesName);
//		
//		JdbcManager.setDataSource(dataSource);
//		
//		BackendManager.init();
//	}
//
//	protected String getFilePropertiesName() throws Exception {
//		return "conf/jdbc.properties";
//	}
//	
//	@Test
//	public void testInsert() throws Exception {
//		try {
//			setDataSource();
//
//			GlobalIndexerDAO.DAO.deleteAll();
//
//			GlobalIndexerDAO.DAO.insert("pepe",TYPE_A,"1");
//			GlobalIndexerDAO.DAO.insert("pepe",TYPE_A,"2");
//			GlobalIndexerDAO.DAO.insert("pépé",TYPE_A,"3");
//			GlobalIndexerDAO.DAO.insert("juan",TYPE_A,"4");
//			GlobalIndexerDAO.DAO.insert("enrique",TYPE_A,"5");
//
//			TestCase.assertEquals(3,GlobalIndexerDAO.DAO.count("pepe"));
//			TestCase.assertEquals(4,GlobalIndexerDAO.DAO.count("%e%"));
//
//			TestCase.assertEquals(1,GlobalIndexerDAO.DAO.count("enrique"));
//
//			GlobalIndexerDAO.DAO.delete(TYPE_A, "1");
//			TestCase.assertEquals(2,GlobalIndexerDAO.DAO.count("pepe"));
//			TestCase.assertEquals(3,GlobalIndexerDAO.DAO.count("%e%"));
//
//			TestCase.assertEquals(1,GlobalIndexerDAO.DAO.count("enrique"));
//
//			
////			List<IndexerResult> list=GlobalIndexerDAO.DAO.search("pepe");
////			
////			TestCase.assertEquals(1, list.size());
//			
//		}catch (Exception e) {
//			log.error("", e);
//			throw e;
//		}	
//	}
//
//
//
//}
//

