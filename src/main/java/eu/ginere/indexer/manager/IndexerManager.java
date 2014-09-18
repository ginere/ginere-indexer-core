package eu.ginere.indexer.manager;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.test.TestResult;
import eu.ginere.indexer.dao.IndexerDAOInterface;
import eu.ginere.indexer.dao.TokenDAOInterface;
import eu.ginere.indexer.descriptor.IndexerElement;
import eu.ginere.indexer.descriptor.IndexerObjectDescriptor;

public class IndexerManager extends AbstractIndexerManager {
	static final Logger log = Logger.getLogger(IndexerManager.class);
	
	static private boolean indexing=false;
	
    IndexerManager(IndexerDAOInterface indexerDAO,TokenDAOInterface tokenDAO){
        super(indexerDAO,tokenDAO);
    }

    /**
	 * @return The test result.
	 */
	public TestResult test(){
        return super.testProtected();
    }

    
	public void index(Object object,
                      IndexerObjectDescriptor descriptor) throws DaoManagerException{
		
		List <IndexerElement>list=descriptor.getIndexerElements(object);

		if (log.isDebugEnabled()){
			log.debug("Index for object:"+object+" result:"+list.size());
		}
		
		for (IndexerElement indx:list){
			if (log.isDebugEnabled()){
				log.debug("Result:"+indx);
			}
			
//			actualizar tambien los tokens ....
//			TokenDAO.DAO.updateOrInsert(indx.getToken(),indx.getType());
			
			indexerDAO.insert(indx);
		}
	}

	/**
	 * Actualiza el numero de cada token para la completacion automatica.
	 * 
	 * @param type
	 * @throws DaoManagerException
	 */
	public void createTokenCount(String type) throws DaoManagerException{
		boolean allreadyIndexing=false;
		
		synchronized (IndexerManager.class) {
			if (indexing==false){
				indexing=true;
			} else {
				allreadyIndexing=true;
			}
		}

		
		if (allreadyIndexing){
			log.warn("There is another indexing running");
			
			return;
		} else {
			try {
				createTokenCountInner(type);
			}finally{
				indexing=false;
			}
		}
	}

	/**
	 * Actualiza el numero de cada token para la completacion automatica.
	 * 
	 * @param type
	 * @throws DaoManagerException
	 */
	public void updateTokenCount(String type) throws DaoManagerException{
		boolean allreadyIndexing=false;
		synchronized (IndexerManager.class) {
			if (indexing==false){
				indexing=true;
			} else {
				allreadyIndexing=true;
			}
		}

		
		if (allreadyIndexing){
			log.warn("There is another indexing running");
			
			return;
		} else {
			try {
				updateTokenCountInner(type);
			}finally{
				indexing=false;
			}
		}
	}


	
//	static class IteratorInsertToken implements IteratorOventTokenList{		
//		@Override
//		public void iterate(String type,String token, int number) {
//			try {
//				TokenDAO.DAO.insert(token,type,number);
//			} catch (DaoManagerException e) {			
//				log.error(" type:'"+type+
//						  "' token:'"+token+
//						  "' number:"+number
//						  , e);
//			}			
//		}	
//	}
//
//	static class IteratorUpdateToken implements IteratorOventTokenList{		
//		@Override
//		public void iterate(String type,String token, int number) {
//			try {
//				TokenDAO.DAO.updateOrInsert(token, type,number);
//			} catch (DaoManagerException e) {			
//				log.error(" type:'"+type+
//						  "' token:'"+token+
//						  "' number:"+number
//						  , e);
//			}			
//		}	
//	}
//
//	static IteratorInsertToken INSERT_ITERATOR=new IteratorInsertToken();
//	static IteratorUpdateToken UPDATE_ITERATOR=new IteratorUpdateToken();

	private void createTokenCountInner(String type) throws DaoManagerException{
//		TokenDAO.DAO.truncateTable();
//		log.warn("Table truncated, inserting tokens ...");		
//		log.info("Starting indexing tokens ...");
//		AbstractSQLDAO.startThreadLocal();
//		try {	
//			IndexerDAO.DAO.iterateOverTokenList(type,INSERT_ITERATOR);
//		}finally{
//			AbstractSQLDAO.endThreadLocal(true);
//		}
		
		indexerDAO.createTokenCountInner(type);
	}
	
	private void updateTokenCountInner(String type) throws DaoManagerException{
//		log.info("Starting indexing tokens ...");
//		AbstractSQLDAO.startThreadLocal();
//		try {	
//			IndexerDAO.DAO.iterateOverTokenList(type,UPDATE_ITERATOR);
//		}finally{
//			AbstractSQLDAO.endThreadLocal(true);
//		}
		indexerDAO.updateTokenCountInner(type);
	}
}
