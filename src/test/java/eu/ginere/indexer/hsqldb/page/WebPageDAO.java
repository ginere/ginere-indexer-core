package eu.ginere.indexer.hsqldb.page;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.hsqldb.dao.AbstractKeyDTODao;

public class WebPageDAO extends AbstractKeyDTODao<WebPage>{
	
	static final Logger log = Logger.getLogger(WebPageDAO.class);

	private static final String TABLE_NAME = WebPage.TABLE_NAME;
	private static final String ID = WebPage.ID;
	private static final String COLUMNS_ARRAY_MINUS_KEY_COLUMN[] = WebPage.COLUMNS_ARRAY_MINUS_KEY_COLUMN;
    private static final String COLUMNS_NAMES=ID+", "+StringUtils.join(COLUMNS_ARRAY_MINUS_KEY_COLUMN, ',');


	static final private String CREATE_QUERY_ARRAY[][]=new String[][]{
		{ "CREATE TABLE "+TABLE_NAME+" ("
          + "		"+ID+" varchar(3000) NOT NULL,"
          + "		TITLE varchar(3000),"
          + "		CONTENT CLOB,"
          + "		LAST_UPDATE TIMESTAMP,"
          + "		PRIMARY KEY ("+ID+")"
          + ")"
        }
    };

	
	public static final WebPageDAO DAO=new WebPageDAO();
	
	private WebPageDAO(){
		super(WebPageDAO.TABLE_NAME,
			 CREATE_QUERY_ARRAY,
              WebPageDAO.ID,
              WebPageDAO.COLUMNS_ARRAY_MINUS_KEY_COLUMN);
	}


	@Override
	protected WebPage createFromResultSet(String id, 
                                           ResultSet rset) throws SQLException, DaoManagerException {
		return new WebPage(id, rset);
	}

    @Override
	protected int updateStament(PreparedStatement pstmInsert, 
                                WebPage obj,
                                String query, 
                                int i) throws DaoManagerException {
		
		set(pstmInsert,i++,obj.title,query);
		set(pstmInsert,i++,obj.content,query);
		set(pstmInsert,i++,obj.lastUpdate,query);

		return i;
	}

    public WebPage insert(String url) throws IOException, DaoManagerException{
    	org.jsoup.Connection conn =Jsoup.connect(url);
    	Document doc=conn.get();
    	
    	WebPage page=new WebPage(url,doc.title(),doc.text());
    	
    	page =insert(page);
    	
    	log.info("Inserted:"+url);
    	return page;
    }
    
	public WebPage insert(WebPage obj) throws DaoManagerException {
		super.insertInner(obj);
		
		return obj;
	}

	static interface WebPageIteratorInterface extends Iterator<WebPage>{
		
	}

	
	public static final String SELECT_LAST_UPDATE="select "+COLUMNS_NAMES+" from "+TABLE_NAME+" where ( DT_UPDATE is not null AND DT_UPDATE > ?) OR ( DT_UPDATE is null AND DT_CREATE > ?)";
	
	public void iterateOnLastUpdate(Date date,WebPageIteratorInterface iterator) throws DaoManagerException {
		Connection connection = getConnection();
		String query=SELECT_LAST_UPDATE;
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			
			try {				
				setDate(pstm, 1, date, query);
				setDate(pstm, 2, date, query);
				
				super.iterate(iterator, pstm, query);
				
			} finally {
				close(pstm);
			}
		} finally {
			closeConnection(connection);
		}
			

	}
	

//	public static final String GET_PROPERTY="select PARAMETER_VALUE,PARAMETER_DESCRIPTION from "+TABLE_NAME+" where PARAMETER_CODE=? and rownum <=1 order by PARAMETER_CODE ";
//	
//	public String getWebPage(String propertyName) throws DaoManagerException {
//		try {
//			Connection connection = getConnection();
//			String query=GET_PROPERTY;
//			
//			try {
//				PreparedStatement pstm = getPrepareStatement(connection,query);
//				
//				setString(pstm, 1, propertyName, query);
//
//				
//				return getString(pstm, query);
//				
//			} finally {
//				closeConnection(connection);
//			}
//		}catch (DaoManagerException e) {
//			throw new DaoManagerException("propertyName:"+propertyName+"'",e);
//		}
//	}
//	
//	public boolean exists(String propertyName) throws DaoManagerException {
//		try {
//			Connection connection = getConnection();
//			String query=GET_PROPERTY;
//			
//			try {
//				PreparedStatement pstm = getPrepareStatement(connection,query);
//				
//				setString(pstm, 1, propertyName, query);
//				
//				ResultSet rset = executeQuery(pstm,query);
//				
//				return rset.next();
//			} catch (SQLException e) {
//				String error = " propertyName:'"+propertyName+"'";
//				
//				throw new DaoManagerException(error, e);			
//			} catch (DaoManagerException e) {
//				String error = " propertyName:'"+propertyName+"'";
//				
//				throw new DaoManagerException(error, e);
//			} finally {
//				closeConnection(connection);
//			}
//		}catch (DaoManagerException e) {
//			throw new DaoManagerException("propertyName:"+propertyName+"'",e);
//		}
//	}
//
//	public static final String INSERT_PROPERTY="insert into "+TABLE_NAME+" (PARAMETER_CODE,PARAMETER_VALUE,PARAMETER_DESCRIPTION) VALUES (?,?,?)";
//
//	public void insertWebPage(String propertyName,
//							   String defaultValue,
//							   String description) throws DaoManagerException {
//		try {
//			Connection connection = getConnection();
//			String query=INSERT_PROPERTY;
//			
//			try {
//				PreparedStatement pstm = getPrepareStatement(connection,query);
//				
//				setString(pstm, 1, propertyName, query);
//				setString(pstm, 2, defaultValue, query);
//				setString(pstm, 3, description, query);
//				
//				long rowNum=executeUpdate(pstm, query);
//				
//				if (log.isDebugEnabled()){
//					log.debug("Lineas actualizadas:"+rowNum);			
//				}	
//			} finally {
//				closeConnection(connection);
//			}
//		}catch (DaoManagerException e) {
//			throw new DaoManagerException("propertyName:"+propertyName+
//										  "', defaultValue:"+defaultValue+
//										  "' description:"+description+
//										  "'",e);
//		}
//	}
//
//	public static final String DELETE_PROPERTY="delete from "+TABLE_NAME+" where PARAMETER_CODE=?";
//
//	public void delete(String propertyName) throws DaoManagerException {
//		try {
//			Connection connection = getConnection();
//			String query=DELETE_PROPERTY;
//			
//			try {
//				PreparedStatement pstm = getPrepareStatement(connection,query);
//				
//				setString(pstm, 1, propertyName, query);
//	
//				long rowNum=executeUpdate(pstm, query);
//				
//				if (log.isDebugEnabled()){
//					log.debug("Lineas actualizadas:"+rowNum);			
//				}	
//			} finally {
//				closeConnection(connection);
//			}
//		}catch (DaoManagerException e) {
//			throw new DaoManagerException(" propertyName:'"+propertyName+"'",e);
//		}
//	}
//
//	public static final String UPDATE_VALUE="update "+TABLE_NAME+" set PARAMETER_VALUE=? where PARAMETER_CODE=?";
//
//	public void setValue(String propertyName, String value) throws DaoManagerException {
//		try {
//			Connection connection = getConnection();
//			String query=UPDATE_VALUE;
//			
//			try {
//				PreparedStatement pstm = getPrepareStatement(connection,query);
//				
//				setString(pstm, 1, value, query);
//				setString(pstm, 2, propertyName, query);
//	
//				long rowNum=executeUpdate(pstm, query);
//				
//				if (log.isDebugEnabled()){
//					log.debug("Lineas actualizadas:"+rowNum);			
//				}	
//			} finally {
//				closeConnection(connection);
//			}
//		}catch (DaoManagerException e) {
//			throw new DaoManagerException( " propertyName:'"+propertyName+
//									 "', value:'"+value+"'",e);
//		}
//	}
//
//
//	public static final String UPDATE_DESCRIPCION="update "+TABLE_NAME+" set PARAMETER_DESCRIPTION=? where PARAMETER_CODE=?";
//
//	public void updateDescripcion(String propertyName, String descripcion) throws DaoManagerException {
//		try {
//			Connection connection = getConnection();
//			String query=UPDATE_DESCRIPCION;
//			
//			try {
//				PreparedStatement pstm = getPrepareStatement(connection,query);
//				
//				setString(pstm, 1, descripcion, query);
//				setString(pstm, 2, propertyName, query);
//	
//				long rowNum=executeUpdate(pstm, query);
//				
//				if (log.isDebugEnabled()){
//					log.debug("Lineas actualizadas:"+rowNum);			
//				}	
//			} finally {
//				closeConnection(connection);
//			}
//		}catch (DaoManagerException e) {
//			throw new DaoManagerException(" propertyName:'"+propertyName+
//									"', descripcion:"+descripcion+"'",e);
//		}
//	}




}
