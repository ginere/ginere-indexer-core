package eu.ginere.indexer.updater;

import eu.ginere.base.util.dao.DaoManagerException;


/**
 * Interface to be implemented to updathe de insexes
 * 
 * @author ginere
 *
 * @param <T>
 */
public interface ObectUpdaterInterface<T>  {
    public void update() throws DaoManagerException;

	public String getType();
}

