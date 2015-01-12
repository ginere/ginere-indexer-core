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
	 * Returns an object associated to a research.
	 * The style of the object to be returned. thats the reason why the result is not T because 
	 * depending on the style this can return a different type of object
	 * 
	 * @param result
	 * @param style
	 * @return
	 */
	public Object get(IndexerResult result,String style);
	
}
