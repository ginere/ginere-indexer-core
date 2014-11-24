package eu.ginere.indexer.hsqldb.page;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.indexer.descriptor.AbstractIndexerObjectDescriptor;

public class WebPageDescriptor extends AbstractIndexerObjectDescriptor<WebPage>{
	static final Logger log = Logger.getLogger(WebPageDescriptor.class);
	
	private static final String FIELDS[]={
		"title",
        "content",
	};

	public static final WebPageDescriptor SINGLETON=new WebPageDescriptor();
	
	private WebPageDescriptor(){
		super(WebPage.class,FIELDS);
	}
	
	@Override
	protected String getObjectId(WebPage object) {
		return object.getKey();
	}

	@Override
	protected WebPage get(String key, String style) {
		try {
			return WebPageDAO.DAO.get(key);
		} catch (DaoManagerException e) {
			log.error("While getting case law id:"+key);
			
			return null;
		}
	}

}
