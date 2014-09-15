package eu.ginere.indexer.indexer;

import eu.ginere.indexer.descriptor.IndexerDescriptor;
import eu.ginere.indexer.indexer.IndexerDAO.IteratorOventTokenList;

import java.util.List;

import org.apache.log4j.Logger;

import avem.common.util.dao.DaoManagerException;
import avem.common.util.manager.AbstractManager;
import avem.jdbc.dao.AbstractSQLDAO;

public class IndexerManager extends AbstractManager {
	
	static final Logger log = Logger.getLogger(IndexerManager.class);
	static private boolean indexing=false;
	
	public static void index(Object object,IndexerDescriptor descriptor) throws DaoManagerException{
		
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
			
			IndexerDAO.DAO.insert(indx);
		}
	}

	/**
	 * Actualiza el numero de cada token para la completacion automatica.
	 * 
	 * @param type
	 * @throws DaoManagerException
	 */
	public static void createTokenCount(String type) throws DaoManagerException{
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
	public static void updateTokenCount(String type) throws DaoManagerException{
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

	
//	/**
//	 * Realiza una busqueda. Devuelve en primer lugar los elementos que mas se aproximan
//	 * 
//	 * @param tokens Los tokens que identifican la busqueda
//	 * @param pageSize El numero de elementos por pagina
//	 * @param page El numero de Pagina
//	 * @return devuelve los elementos de una pagina resultado de la busqueda
//	 */
//	public static SearchResult search(String tokens,int pageSize,int page){
//		HashSet<String> tokensSet=IndexerStringUtils.getTokens(tokens);
//
//		try {
//			int total=IndexerDAO.DAO.count(tokensSet);
//	
//			if (total>0){
//				List <IndexerResult> list=IndexerDAO.DAO.search(tokensSet,(page*pageSize),pageSize);
//				List <Object> objectList=new ArrayList<Object>(list.size());
//		
//				for (IndexerResult result:list){
//					Object obj=DescriptorManager.get(result);
//					if (obj!=null){
//						objectList.add(obj);
//					} else {
//						log.warn("No object found for result:"+result);
//					}
//				}
//				
//				return new SearchResult(objectList,page,pageSize,total);
//			} else {
//				return SearchResult.EMPTY_RESULT;
//			}
//		}catch (DaoManagerException e) {
//			String error = "Tokens:'"+tokens+"' pageSize:"+pageSize+" page:"+page;
//
//			log.error(error,e);
//
//			return SearchResult.EMPTY_RESULT;
//		}
//	}

	
	static class IteratorInsertToken implements IteratorOventTokenList{		
		@Override
		public void iterate(String type,String token, int number) {
			try {
				TokenDAO.DAO.insert(token,type,number);
			} catch (DaoManagerException e) {			
				log.error(" type:'"+type+
						  "' token:'"+token+
						  "' number:"+number
						  , e);
			}			
		}	
	}

	static class IteratorUpdateToken implements IteratorOventTokenList{		
		@Override
		public void iterate(String type,String token, int number) {
			try {
				TokenDAO.DAO.updateOrInsert(token, type,number);
			} catch (DaoManagerException e) {			
				log.error(" type:'"+type+
						  "' token:'"+token+
						  "' number:"+number
						  , e);
			}			
		}	
	}

	static IteratorInsertToken INSERT_ITERATOR=new IteratorInsertToken();
	static IteratorUpdateToken UPDATE_ITERATOR=new IteratorUpdateToken();

	private static void createTokenCountInner(String type) throws DaoManagerException{
		TokenDAO.DAO.truncateTable();
		log.warn("Table truncated, inserting tokens ...");		
		log.info("Starting indexing tokens ...");
		AbstractSQLDAO.startThreadLocal();
		try {	
			IndexerDAO.DAO.iterateOverTokenList(type,INSERT_ITERATOR);
		}finally{
			AbstractSQLDAO.endThreadLocal(true);
		}
	}
	
	private static void updateTokenCountInner(String type) throws DaoManagerException{
		log.info("Starting indexing tokens ...");
		AbstractSQLDAO.startThreadLocal();
		try {	
			IndexerDAO.DAO.iterateOverTokenList(type,UPDATE_ITERATOR);
		}finally{
			AbstractSQLDAO.endThreadLocal(true);
		}
	}


	
}
