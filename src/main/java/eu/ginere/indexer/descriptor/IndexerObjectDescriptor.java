package eu.ginere.indexer.descriptor;

import java.util.List;

import eu.ginere.base.util.dao.DaoManagerException;


/**
 * 
 * This is the main interface to develop to intregrate a new object type into descriptor
 * 
 * @author Ginere
 *
 * @param <T>
 */
public interface IndexerObjectDescriptor<T>  {

	/**
	 * This return the Object type, by example Class.getName()
	 * @return
	 */
	public String getType();

	/**
	 * This return all the tokens for one element.
	 * 
	 * @param object
	 * @return
	 * @throws DaoManagerException
	 */
	public List<IndexerElement> getIndexerElements(T object) throws DaoManagerException;
	
	/**
	 * Returns the object associated to a research.
	 * The style of the object to be returned.
	 * 
	 * @param result
	 * @param style
	 * @return
	 */
	public T get(IndexerResult result,String style);
	
}
