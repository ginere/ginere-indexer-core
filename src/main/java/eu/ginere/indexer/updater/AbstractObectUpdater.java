// HEAD
package eu.ginere.indexer.updater;

import java.util.Date;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.indexer.manager.GlobalManager;


/**
 * Helper abstract class to implements Updater
 * 
 * @author ginere
 *
 * @param <T>
 */
public abstract class AbstractObectUpdater<T> implements ObectUpdaterInterface {
	static final Logger log = Logger.getLogger(AbstractObectUpdater.class);
	
	
    // protected final IndexerObjectDescriptor<T> descriptor;
    protected final GlobalManager manager;
    protected final String type;

    protected AbstractObectUpdater(GlobalManager manager,
                                   String type){
        this.manager=manager;
        this.type=type;
    }

    public void update() throws DaoManagerException{
        // this date can be null;
        Date lastUpdate=manager.getIndexerDAO().getLastUpdate(type);
        
        try {
            update(lastUpdate);
            manager.getTokenDAO().updateTokenCount(type);
        }catch(Throwable e){
            log.error("While update for type:'"+type+"'");
            throw new DaoManagerException("While update for type:'"+type+"'",e);
        }        
    }

	protected abstract void update(Date lastUpdate)throws DaoManagerException;

}

//package eu.ginere.indexer.updater;
//
//import java.util.Date;
//
//import org.apache.log4j.Logger;
//
//import eu.ginere.base.util.dao.DaoManagerException;
//import eu.ginere.indexer.manager.GlobalManager;
//
//
///**
// * Helper abstract class to implements Updater
// * 
// * @author ginere
// *
// * @param <T>
// */
//public abstract class AbstractObectUpdater<T> implements ObectUpdaterInterface {
//	static final Logger log = Logger.getLogger(AbstractObectUpdater.class);
//	
//	
//    // protected final IndexerObjectDescriptor<T> descriptor;
//    protected final GlobalManager manager;
//    protected final String type;
//
//    protected AbstractObectUpdater(GlobalManager manager,
//                                   String type){
//        this.manager=manager;
//        this.type=type;
//    }
//
//    public void update() throws DaoManagerException{
//        // this date can be null;
//        Date lastUpdate=manager.getIndexerDAO().getLastUpdate(type);
//        
//        try {
//            update(lastUpdate);
//            manager.getTokenDAO().updateTokenCount(type);
//        }catch(Throwable e){
//            log.error("While update for type:'"+type+"'");
//            throw new DaoManagerException("While update for type:'"+type+"'",e);
//        }        
//    }
//
//	protected abstract void update(Date lastUpdate)throws DaoManagerException;
//
//}
//
//branch 'master' of https://ginere@github.com/ginere/ginere-indexer-core.git
