package eu.ginere.indexer.indexer;

import eu.ginere.indexer.descriptor.DescriptorManager;
import eu.ginere.indexer.util.IndexerStringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import avem.common.util.dao.DaoManagerException;
import avem.common.util.manager.AbstractManager;

public class SearchManager extends AbstractManager {
	
	static final Logger log = Logger.getLogger(SearchManager.class);
	private static final int MAX_PAGE_SIZE = 50;

	/**
	 * Realiza una busqueda. Devuelve en primer lugar los elementos que mas se aproximan
	 * 
	 * @param tokens Los tokens que identifican la busqueda
	 * @param pageSize El numero de elementos por pagina
	 * @param page El numero de Pagina
	 * @return devuelve los elementos de una pagina resultado de la busqueda
	 */
	public static SearchResult search(String tokens,String type,String style,int pageSize,int page){
		pageSize=getPageSize(pageSize);
		
		HashSet<String> tokensSet=IndexerStringUtils.getTokens(tokens);
		
		try {
			if (tokensSet.size() == 1) {
				String token=tokensSet.iterator().next();
				List <IndexerResult> list=IndexerDAO.DAO.searchType(token,type,(page*pageSize),pageSize);
				
				List <Object> objectList=new ArrayList<Object>(list.size());
				
				for (IndexerResult result:list){
					Object obj=DescriptorManager.get(result,style);
					if (obj!=null){
						objectList.add(obj);
					} else {
						log.warn("No object found for result:"+result);
					}
				}

				int total=TokenDAO.DAO.getCount(token, type);

				if (total==0){
					total=objectList.size();
				}
				return new SearchResult(objectList,page,pageSize,total);
				
//				int total=TokenDAO.DAO.getCount(token, type);
//				if (total>0){			
//				
//				} else {
//					return SearchResult.EMPTY_RESULT;
//				}
			} else {
//				int total=IndexerDAO.DAO.count(tokensSet,type);
				int total;
					Collection<String> sortedTokens=TokenDAO.DAO.sort(tokensSet,type);
					
					List <IndexerResult> list=IndexerDAO.DAO.searchTypeBis(sortedTokens,type,(page*pageSize),pageSize);
//					if (type ==null){
//						list=IndexerDAO.DAO.search(tokensSet,(page*pageSize),pageSize);
//					} else {
//						list=IndexerDAO.DAO.searchType(tokensSet,type,(page*pageSize),pageSize);
//					}
					List <Object> objectList=new ArrayList<Object>(list.size());
			
					for (IndexerResult result:list){
						Object obj=DescriptorManager.get(result,style);
						if (obj!=null){
							objectList.add(obj);
						} else {
							log.warn("No object found for result:"+result);
						}
					}
					
					if (list.size()<pageSize){
						total=(page*pageSize)+list.size();
					} else {
						total=TokenDAO.DAO.getCount(tokensSet, type);
					}
					
					return new SearchResult(objectList,page,pageSize,total);
			}
		}catch (DaoManagerException e) {
			String error = "Tokens:'"+tokens+"' pageSize:"+pageSize+" page:"+page;

			log.error(error,e);

			return SearchResult.EMPTY_RESULT;
		}
	}

	private static int getPageSize(int pageSize) {
		if (pageSize==0 || pageSize>MAX_PAGE_SIZE){
			return MAX_PAGE_SIZE;
		} else {
			return pageSize;
		}
	}	
}
