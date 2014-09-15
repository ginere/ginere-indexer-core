package eu.ginere.indexer.descriptor;

import java.util.List;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.indexer.indexer.IndexerElement;
import eu.ginere.indexer.indexer.IndexerResult;


/**
 * 
 * This is the main interface to develop to intregrate new indexer
 * 
 * @author Ginere
 *
 * @param <T>
 */
public interface IndexerDescriptor<T>  {

	public String getType();

	public List<IndexerElement> getIndexerElements(T object) throws DaoManagerException;
	
	public T get(IndexerResult result,String style);
	
}
