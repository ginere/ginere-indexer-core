package eu.ginere.indexer.manager;


import org.apache.log4j.Logger;

import eu.ginere.base.util.manager.AbstractManager;
import eu.ginere.base.util.test.TestInterface;
import eu.ginere.base.util.test.TestResult;
import eu.ginere.indexer.dao.IndexerDAOInterface;
import eu.ginere.indexer.dao.TokenDAOInterface;

/**
 * Main class to search the objects.
 * 
 * @author mendang
 *
 */
public abstract class AbstractIndexerManager extends AbstractManager implements TestInterface{
	
	static final Logger log = Logger.getLogger(AbstractManager.class);

    protected IndexerDAOInterface indexerDAO;
    protected TokenDAOInterface tokenDAO;
    

    protected AbstractIndexerManager(IndexerDAOInterface indexerDAO,
                                     TokenDAOInterface tokenDAO){
        this.indexerDAO=indexerDAO;
        this.tokenDAO=tokenDAO;
    }

    	
    /**
	 * @return The test result.
	 */
	protected TestResult testProtected(){
        TestResult ret=new TestResult(SearchManager.class);

        if (indexerDAO==null){
        	ret.addError("The IndexerDAOInterface is null, the object has not been initialized properly");
        }

        if (tokenDAO==null){
        	ret.addError("The TokenDAOInterface is null, the object has not been initialized properly");
        }
        
        ret.add(indexerDAO.test());
        ret.add(tokenDAO.test());
        
        return ret;
    }
}
