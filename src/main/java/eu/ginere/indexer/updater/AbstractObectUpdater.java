package eu.ginere.indexer.updater;

import java.util.Date;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.indexer.descriptor.IndexerObjectDescriptor;
import eu.ginere.indexer.manager.GlobalManager;


public abstract class AbstractObectUpdater<T>  {
	static final Logger log = Logger.getLogger(AbstractObectUpdater.class);
	
	
    protected final IndexerObjectDescriptor<T> descriptor;
    protected final GlobalManager manager;

    protected AbstractObectUpdater(GlobalManager manager,
                                   IndexerObjectDescriptor<T> descriptor){
        this.manager=manager;
        this.descriptor=descriptor;
    }

    public void update() throws DaoManagerException{
        String type=descriptor.getType();

        // this date can be null;
        Date lastUpdate=manager.getIndexerDAO().getLastUpdate(type);
        
        try {
            update(lastUpdate);
            manager.getTokenDAO().updateTokenCount(descriptor.getType());
        }catch(Throwable e){
            log.error("While update for type:'"+type+"'");
            throw new DaoManagerException("While update for type:'"+type+"'",e);
        }        
    }

	protected abstract void update(Date lastUpdate)throws DaoManagerException;

}

