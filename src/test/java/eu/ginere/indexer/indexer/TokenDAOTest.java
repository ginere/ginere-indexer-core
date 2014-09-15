package eu.ginere.indexer.indexer;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import avem.dao.perfil.PerfilIndexerDescriptor;
import avem.jdbc.JdbcManager;
import avem.jdbc.backend.BackendManager;
import avem.jdbc.test.AbstractSQLDAOTest;

public class TokenDAOTest extends AbstractSQLDAOTest {

	public TokenDAOTest() {
		super(TokenDAO.DAO,false);
//		super(TokenDAO.DAO,true);
	}

	static final Logger log = Logger.getLogger(TokenDAOTest.class);


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
			
			TokenDAO.DAO.truncateTable();

			TokenDAO.DAO.insert("pepe",TYPE_A,1);
//			TokenDAO.DAO.insert("pepe",TYPE_A,2);
//			TokenDAO.DAO.insert("pépé",TYPE_A,3);
			TokenDAO.DAO.insert("juan",TYPE_A,4);
			TokenDAO.DAO.insert("enrique",TYPE_A,5);

			TestCase.assertEquals(1,TokenDAO.DAO.getCount("pepe",TYPE_A));
//			TestCase.assertEquals(4,TokenDAO.DAO.count("%e%"));
//
//			TestCase.assertEquals(1,TokenDAO.DAO.count("enrique"));
//
//			TokenDAO.DAO.delete(TYPE_A, "1");
//			TestCase.assertEquals(2,TokenDAO.DAO.count("pepe"));
//			TestCase.assertEquals(3,TokenDAO.DAO.count("%e%"));
//
//			TestCase.assertEquals(1,TokenDAO.DAO.count("enrique"));
//
//			
////			List<IndexerResult> list=TokenDAO.DAO.search("pepe");
////			
////			TestCase.assertEquals(1, list.size());
			
			IndexerManager.updateTokenCount(PerfilIndexerDescriptor.DESCRIPTOR.getType());
			
		}catch (Exception e) {
			log.error("", e);
			throw e;
		}	
	}



}


