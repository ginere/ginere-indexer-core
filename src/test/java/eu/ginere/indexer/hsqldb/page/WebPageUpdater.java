package eu.ginere.indexer.hsqldb.page;

import java.util.Date;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.indexer.manager.GlobalManager;
import eu.ginere.indexer.updater.AbstractObectUpdater;

public class WebPageUpdater extends AbstractObectUpdater<WebPage>{
	static final Logger log = Logger.getLogger(WebPageUpdater.class);
	
		
	public WebPageUpdater(GlobalManager manager) {
		super(manager, WebPageDescriptor.SINGLETON.getType());
	}

	
	@Override
	protected void update(Date lastUpdate) throws DaoManagerException{
		
		if (lastUpdate!=null){
			WebPageDAO.DAO.iterateOnLastUpdate(lastUpdate, INDEXER_ITERATOR);
		} else {
			WebPageDAO.DAO.iterate(INDEXER_ITERATOR);
		}
	}

	public final WebPageDAO.WebPageIteratorInterface INDEXER_ITERATOR=new WebPageDAO.WebPageIteratorInterface() {
		
		@Override
		public boolean access(WebPage caseLaw) {
			try {
				manager.getIndexerManager().index(caseLaw, WebPageDescriptor.SINGLETON);
			}catch(Exception e){
				log.error("While indexing case law:"+caseLaw,e);
			}
			return true;
		}
	};


	@Override
	public String getType() {
		return WebPageDescriptor.SINGLETON.getType();
	}
	
}
