//HEAD
package eu.ginere.indexer.manager;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.manager.AbstractManager;
import eu.ginere.base.util.test.TestInterface;
import eu.ginere.base.util.test.TestResult;
import eu.ginere.indexer.dao.IndexerDAOInterface;
import eu.ginere.indexer.dao.TokenDAOInterface;

/**
 * Use this to get the autocompletation list 
 * 
 * @author ginere
 *
 */
public class GlobalManager extends AbstractManager implements TestInterface{
	
	static final Logger log = Logger.getLogger(GlobalManager.class);

    private final IndexerDAOInterface indexerDAO;
    private final TokenDAOInterface tokenDAO;

    private final AutoCompleteManager autoCompleteManager;
    private final IndexerManager indexerManager;
    private final SearchManager searchManager;
    	
        
    public GlobalManager(IndexerDAOInterface indexerDAO,
                         TokenDAOInterface tokenDAO) throws DaoManagerException{
        
    	if (indexerDAO == null || tokenDAO == null){
    		throw new DaoManagerException("The IndexerDAOInterface:'"+indexerDAO+
                                          "' or the TokenDAOInterface:'"+tokenDAO+
                                          "' are null");
    	}
        this.indexerDAO=indexerDAO;
    	this.tokenDAO=tokenDAO;
        
    	this.autoCompleteManager=new AutoCompleteManager(indexerDAO,tokenDAO);
    	this.indexerManager=new IndexerManager(indexerDAO,tokenDAO);
    	this.searchManager=new SearchManager(indexerDAO,tokenDAO);
    	
    }

    /**
	 * @return The test result.
	 */
	public TestResult test(){
        TestResult ret=new TestResult(GlobalManager.class);

        if (indexerDAO==null){
        	ret.addError("The IndexerDAOInterface is null, the object has not been initialized properly");
        }

        if (tokenDAO==null){
        	ret.addError("The TokenDAOInterface is null, the object has not been initialized properly");
        }
        
        ret.add(indexerDAO.test());
        ret.add(tokenDAO.test());

        ret.add(autoCompleteManager.test());
        ret.add(indexerManager.test());
        ret.add(searchManager.test());
        
        return ret;
    }
		
	public void init(){	
		log.info("No initi method implemented for:"+getClass().getName());
	}

	public IndexerManager getIndexerManager() {
		return indexerManager;
	}

    public AutoCompleteManager getAutoCompleteManager(){
        return autoCompleteManager;
    }

	public SearchManager getSearchManager() {
		return searchManager;
	}

	public IndexerDAOInterface getIndexerDAO() {
		return indexerDAO;
	}
    
	public TokenDAOInterface getTokenDAO() {
		return tokenDAO;
	}
    
}

//package eu.ginere.indexer.manager;
//
//import org.apache.log4j.Logger;
//
//import eu.ginere.base.util.dao.DaoManagerException;
//import eu.ginere.base.util.manager.AbstractManager;
//import eu.ginere.base.util.test.TestInterface;
//import eu.ginere.base.util.test.TestResult;
//import eu.ginere.indexer.dao.IndexerDAOInterface;
//import eu.ginere.indexer.dao.TokenDAOInterface;
//
///**
// * Use this to get the autocompletation list 
// * 
// * @author ginere
// *
// */
//public class GlobalManager extends AbstractManager implements TestInterface{
//	
//	static final Logger log = Logger.getLogger(GlobalManager.class);
//
//    private final IndexerDAOInterface indexerDAO;
//    private final TokenDAOInterface tokenDAO;
//
//    private final AutoCompleteManager autoCompleteManager;
//    private final IndexerManager indexerManager;
//    private final SearchManager searchManager;
//    	
//        
//    public GlobalManager(IndexerDAOInterface indexerDAO,
//                         TokenDAOInterface tokenDAO) throws DaoManagerException{
//        
//    	if (indexerDAO == null || tokenDAO == null){
//    		throw new DaoManagerException("The IndexerDAOInterface:'"+indexerDAO+
//                                          "' or the TokenDAOInterface:'"+tokenDAO+
//                                          "' are null");
//    	}
//        this.indexerDAO=indexerDAO;
//    	this.tokenDAO=tokenDAO;
//        
//    	this.autoCompleteManager=new AutoCompleteManager(indexerDAO,tokenDAO);
//    	this.indexerManager=new IndexerManager(indexerDAO,tokenDAO);
//    	this.searchManager=new SearchManager(indexerDAO,tokenDAO);
//    	
//    }
//
//    /**
//	 * @return The test result.
//	 */
//	public TestResult test(){
//        TestResult ret=new TestResult(GlobalManager.class);
//
//        if (indexerDAO==null){
//        	ret.addError("The IndexerDAOInterface is null, the object has not been initialized properly");
//        }
//
//        if (tokenDAO==null){
//        	ret.addError("The TokenDAOInterface is null, the object has not been initialized properly");
//        }
//        
//        ret.add(indexerDAO.test());
//        ret.add(tokenDAO.test());
//
//        ret.add(autoCompleteManager.test());
//        ret.add(indexerManager.test());
//        ret.add(searchManager.test());
//        
//        return ret;
//    }
//
//	public IndexerManager getIndexerManager() {
//		return indexerManager;
//	}
//
//    public AutoCompleteManager getAutoCompleteManager(){
//        return autoCompleteManager;
//    }
//
//	public SearchManager getSearchManager() {
//		return searchManager;
//	}
//
//	public IndexerDAOInterface getIndexerDAO() {
//		return indexerDAO;
//	}
//    
//	public TokenDAOInterface getTokenDAO() {
//		return tokenDAO;
//	}
//    
//}
//branch 'master' of https://ginere@github.com/ginere/ginere-indexer-core.git
