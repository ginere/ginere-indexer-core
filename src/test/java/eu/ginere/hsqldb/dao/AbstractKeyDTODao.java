package eu.ginere.hsqldb.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.ginere.base.util.dao.DaoManagerException;

/**
 * 
 *
 */
public abstract class AbstractKeyDTODao<I extends KeyDTO> extends AbstractKeyDao<I,I>{

	
	protected AbstractKeyDTODao(String tableName,
			 				String createQuery[][], 
							 String keyColumnName,
							 
							 String columnsArrayMinusKeyColumnName[]){
		super(tableName,createQuery,keyColumnName,columnsArrayMinusKeyColumnName);
	}
	

	/**
	 * Tiene que leer todas las columnas menos la columna de la primary key
	 */
	protected abstract I createFromResultSet(String id,ResultSet rset) throws SQLException, DaoManagerException;

	
	/**
	 * Vuelca todos los 
	 * @param pstmInsert
	 * @param obj
	 * @param query
	 * @throws DaoManagerException
	 */
	protected abstract int updateStament(PreparedStatement pstmInsert,I obj,String query,int argumentIndex) throws DaoManagerException;

	protected I insert(I obj,String secuenceName) throws DaoManagerException {
		String id=super.insertFromSecuence(obj, secuenceName);
		
		obj.setKey(id);
		
		return obj;
	}
	
	@Override
	protected void setInsertStatementFromSequence(PreparedStatement pstmInsert,
												  String id,
												  I obj,
												  String query) throws DaoManagerException {
		int i=1;

		set(pstmInsert,i++, id, query);

		updateStament(pstmInsert,obj,query,i);
	}
	
	@Override
	protected void setUpdateStatement(PreparedStatement pstmInsert,
									  I obj,
									  String query) throws DaoManagerException {
		int i=1;

		i=updateStament(pstmInsert,obj,query,i);		
		
		set(pstmInsert,i++,obj.getKey(),query);
	}
	
	
	protected void setInsertStatement(PreparedStatement pstmInsert,I obj,String query) throws DaoManagerException{
		setInsertStatementFromSequence(pstmInsert, obj.getKey(), obj, query);
	}
}
