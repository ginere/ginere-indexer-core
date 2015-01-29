package eu.ginere.indexer.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.properties.GlobalFileProperties;
import eu.ginere.base.util.test.TestResult;
import eu.ginere.indexer.dao.IndexerDAOInterface;
import eu.ginere.indexer.dao.TokenDAOInterface;
import eu.ginere.indexer.descriptor.AbstractIndexerObjectDescriptor;
import eu.ginere.indexer.descriptor.IndexerResult;
import eu.ginere.indexer.descriptor.ObjectDescriptorManager;
import eu.ginere.indexer.util.IndexerStringUtils;

/**
 * Main class to search the objects.
 * 
 * @author mendang
 *
 */
public class SearchManager extends AbstractIndexerManager{
	
	static final Logger log = Logger.getLogger(SearchManager.class);

    SearchManager(IndexerDAOInterface indexerDAO,TokenDAOInterface tokenDAO){
        super(indexerDAO,tokenDAO);
    }
	
    /**
	 * @return The test result.
	 */
	public TestResult test(){
        return super.testProtected();
    }


	/**
	 * Realiza una busqueda. Devuelve en primer lugar los elementos que mas se aproximan
	 * 
	 * @param tokenList Los tokens que identifican la busqueda
	 * @param pageSize El numero de elementos por pagina
	 * @param page El numero de Pagina
	 * @return devuelve los elementos de una pagina resultado de la busqueda
	 */
	public SearchResult search(String tokenList,
                               String type,
                               String style,
                               int pageSize,
                               int page){

		pageSize=verifyPageSize(pageSize);
		
		HashSet<String> tokensSet=IndexerStringUtils.getTokens(tokenList,AbstractIndexerObjectDescriptor.MIN_TOKEN_LENGTH_TO_INDEX);
		
		try {

            List <IndexerResult> list=search(tokensSet,type,(page*pageSize),pageSize);
            
            List <Object> objectList=new ArrayList<Object>(list.size());

            for (IndexerResult result:list){
                Object obj=ObjectDescriptorManager.get(result,style);
                if (obj!=null){
                    objectList.add(obj);
                } else {
                    log.warn("No object found for result:"+result);
                }
            }

            int total;
            if (list.size()<pageSize){
                total=(page*pageSize)+list.size();
            } else {
				total=getCount(tokensSet, type);
            }
				
            return new SearchResult(objectList,page,pageSize,total);


//			if (tokensSet.size() == 1) {
//				String token=tokensSet.iterator().next();
//				List <IndexerResult> list=indexerDAO.search(token,type,(page*pageSize),pageSize);
//				
//				List <Object> objectList=new ArrayList<Object>(list.size());
//				
//				for (IndexerResult result:list){
//					Object obj=ObjectDescriptorManager.get(result,style);
//					if (obj!=null){
//						objectList.add(obj);
//					} else {
//						log.warn("No object found for result:"+result);
//					}
//				}
//
//				int total=tokenDAO.getCount(token, type);
//
//				if (total==0){
//					total=objectList.size();
//				}
//				return new SearchResult(objectList,page,pageSize,total);
//				
//			} else {
//				int total;
//				Collection<String> sortedTokens=tokenDAO.sort(tokensSet,type);
//				
//				List <IndexerResult> list=indexerDAO.searchTypeBis(sortedTokens,type,(page*pageSize),pageSize);
//
//				List <Object> objectList=new ArrayList<Object>(list.size());
//		
//				for (IndexerResult result:list){
//					Object obj=ObjectDescriptorManager.get(result,style);
//					if (obj!=null){
//						objectList.add(obj);
//					} else {
//						log.warn("No object found for result:"+result);
//					}
//				}
//				
//				if (list.size()<pageSize){
//					total=(page*pageSize)+list.size();
//				} else {
//					total=TokenDAO.DAO.getCount(tokensSet, type);
//				}
//				
//				return new SearchResult(objectList,page,pageSize,total);
//			}
		}catch (DaoManagerException e) {
			String error = "Tokens:'"+tokenList+"' pageSize:"+pageSize+" page:"+page;

			log.error(error,e);

			return SearchResult.EMPTY_RESULT;
		}
	}

	private int getCount(HashSet<String> tokensSet, String type) throws DaoManagerException {
		if (tokensSet.size() == 1) {
			String token=tokensSet.iterator().next();
			return tokenDAO.getCount(token, type);
		} else {
			return tokenDAO.getCount(tokensSet, type);
		}
	}

	private List<IndexerResult> search(HashSet<String> tokensSet,
									   String type, 
									   int firstElement, 
									   int pageSize) throws DaoManagerException {
		if (tokensSet.size() == 1) {
			String token=tokensSet.iterator().next();
			return indexerDAO.search(token,type,firstElement,pageSize);
		} else {
			Collection<String> sortedTokens=tokenDAO.sort(tokensSet,type);
			
			if (sortedTokens.size() != 0) {
				return indexerDAO.searchTypeBis(sortedTokens,type,firstElement,pageSize);
			}else {
				log.warn("++++ Empty sorted list for tokens:"+StringUtils.join(tokensSet,'|'));
				return IndexerResult.EMPTY_LIST;
			}
		}
	}

	private int verifyPageSize(int pageSize) {
		int maxPageSize=GlobalFileProperties.getIntValue(SearchManager.class, "MaxPageSize", 100);
		if (pageSize==0 || pageSize>maxPageSize){
			return maxPageSize;
		} else {
			return pageSize;
		}
	}	
}
