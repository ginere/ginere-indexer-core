package eu.ginere.indexer.manager;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

	public void delete(Object object,
            	IndexerObjectDescriptor descriptor) throws DaoManagerException{
		
		String type=descriptor.getType();
		String key=descriptor.getObjectId(object);

		HashSet <String> tokensToBeDeleted=indexerDAO.getTokens(type,key);
		indexerDAO.delete(type,key,tokensToBeDeleted);
		tokenDAO.decreaseTokenCount(type,tokensToBeDeleted);
		
		return;
	}

    
	public void index(Object object,
                      IndexerObjectDescriptor descriptor) throws DaoManagerException{

		String type=descriptor.getType();
		String key=descriptor.getObjectId(object);

		
		List <IndexerElement>newIndexerList=descriptor.getIndexerElements(object);
		
//		if (log.isDebugEnabled()){
//			log.debug("Index for object:"+object+" result:"+newIndexerList.size());
//		}
		
		
		if (indexerDAO.exists(type,key)) {
			// update the existing element
			HashSet<String> oldTokenList=indexerDAO.getTokens(type,key);
			HashSet<String> newInsertedTokens=new HashSet<String>(newIndexerList.size());

			for (IndexerElement newIndexElement:newIndexerList){
				String newToken=newIndexElement.token;

//				if (log.isDebugEnabled()){
//					log.debug("Result:"+newIndexElement);
//				}
				
				if (StringUtils.isBlank(newToken)){
					log.warn("Empty toke:"+newToken);
					continue;
				}

				if (!oldTokenList.contains(newToken)){
					// we have a new token, insert and increase
					newInsertedTokens.add(newToken);
					indexerDAO.insert(newIndexElement);
				} else {
					// already existing token
					// remove the old token from the old token list
					oldTokenList.remove(newToken);
				}
			}
			
			// in the old token list only stay the tokens that are not in the new list
			// then remove and decrease
			indexerDAO.delete(type, key,oldTokenList);
			tokenDAO.decreaseTokenCount(type,oldTokenList);
		
			// increase the token count of the new created
			tokenDAO.increaseTokenCount(type,newInsertedTokens);
			
		} else {
			// create a new element
			HashSet<String> tokensToIncrease=new HashSet<String>(newIndexerList.size());

			for (IndexerElement indx:newIndexerList){
//				if (log.isDebugEnabled()){
//					log.debug("Result:"+indx);
//				}
				
				if (StringUtils.isBlank(indx.token)){
					log.warn("Empty toke:"+indx);
				} else {
					indexerDAO.insert(indx);
					tokensToIncrease.add(indx.getToken());
				}
			}
			
			// update the tokens count
			tokenDAO.increaseTokenCount(type,tokensToIncrease);
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
