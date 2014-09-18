//package eu.ginere.indexer.indexer;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//
//import avem.common.util.dao.DaoManagerException;
//import avem.jdbc.dao.AbstractSQLDAO;
//
//public class TokenDAO extends AbstractSQLDAO{
//
//	static final Logger log = Logger.getLogger(TokenDAO.class);
//	
//	static final private String TABLE_NAME= "TOKENS_COUNT";
//	static final private String[] COLUMNS=new String[] {
//		"TOKEN", 
//		"TYPE",
//		"NUMBER"
//	};
//
//	static final private String CREATE_QUERY_ARRAY[][]=new String[][]{
//		{ // V1
//			"CREATE TABLE "+TABLE_NAME+" ("
//			+ "		TOKEN varchar(200) NOT NULL,"
//			+ "		TYPE varchar(128) NOT NULL,"
//			+ "		NUMBER int UNSIGNED NOT NULL"
////			+ " 	UNIQUE (TOKEN,TYPE)"
////			+ " 	INDEX (TOKEN)"
//			+ ");",
//		},{
//			// V2
//			"CREATE INDEX TOKEN_INDEX ON "+TABLE_NAME+" (TOKEN,TYPE);"
//		}
//	};
//
//	public static final TokenDAO DAO=new TokenDAO();
//
//	private TokenDAO(){
//		super(TABLE_NAME,CREATE_QUERY_ARRAY);		
//	}
//
//	private static final String INSERT = "insert into "+TABLE_NAME+
//		" (TOKEN,TYPE,NUMBER) values (?,?,?)";
//
//	public void insert(TokenElement indx) throws DaoManagerException{
//		insert(indx.getToken(),
//			   indx.getType(),
//			   indx.getNumber());
//	}
//
//	public void insert(String token,String type,int number) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=INSERT;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//
//			setString(pstm, 1, token, query);
//			setString(pstm, 2, type, query);
//			setInt(pstm, 3, number, query);
//	
//			executeUpdate(pstm, query);
//	
//		} catch (DaoManagerException e) {
//			String error = "token:'"+token
//				+ "' type:'"+type
//				+ "' number:'"+number+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}		
//	}
//
//	private static final String DELETE = "delete from "+TABLE_NAME+
//	" where TOKEN=? and TYPE=?";
//
//	public void delete(String token, String type) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=DELETE;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//			
//			setString(pstm, 1, token, query);
//			setString(pstm, 2, type, query);
//	
//			int number=executeUpdate(pstm, query);
//	
//			if (log.isDebugEnabled()){
//				log.debug("Rows modified:"+number);
//			}
//		} catch (DaoManagerException e) {
//			String error = " token:'"+type
//				+ "' type:'"+type+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}		
//	}
//
//
//	private static final String AUTO_COMPLETE = "SELECT token,number FROM "+TABLE_NAME+" where type=? and token like ? order by number desc LIMIT ?";
//
//	public List<String> getAutoComplete(String lastToken, String type,int numberAutoComplete) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=AUTO_COMPLETE;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
////			String value=lastToken.replace(""%", '');
//			
//			setString(pstm, 1, type, query);
//			setString(pstm, 2, lastToken+'%', query);
//			setInt(pstm, 3, numberAutoComplete, query);
//			
//			return getStringList(pstm, query);
//			
//		} catch (DaoManagerException e) {
//			String error = " lastToken:'"+lastToken+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}
//	}
//	
////	private static final String SORT_TOKEN = "select * from tokens_count where token='wert' or TOKEN='madrid' order by number asc;";
//	private static final String SORT_TOKEN_START = "select token from "+TABLE_NAME+" where type = ? and ( TOKEN=? ";
//	private static final String SORT_TOKEN_MIDDLE = " or TOKEN=? ";
//	
//	private static final String SORT_TOKEN_STOP = " ) order by number desc;";
//
//	public Collection<String> sort(Collection<String> tokens,String type) throws DaoManagerException{
//		if (tokens == null || tokens.size()<=1){
//			return tokens;
//		}
//		StringBuilder buffer=new StringBuilder();
//		
//		for (int i=0;i<tokens.size();i++){
//			if (i == 0) {
//				buffer.append(SORT_TOKEN_START);				
//			} else {
//				buffer.append(SORT_TOKEN_MIDDLE);
//			}
//		}
//		
//		buffer.append(SORT_TOKEN_STOP);
//		
//		Connection connection = getConnection();
//		String query=buffer.toString();
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
////			String value=lastToken.replace(""%", '');
//			
//			int i=1;
//			setString(pstm, i++, type, query);
//			for (String token:tokens){
//				setString(pstm, i++, token, query);
//			}
//			
//			return getStringList(pstm, query);
//			
//		} catch (DaoManagerException e) {
//			String error = " tokens:'"+tokens+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}
//	}
//
//	public void updateOrInsert(String token, String type,int number) throws DaoManagerException {
//		if (exists(token,type)){
//			updateTokenValue(token,type,number);
//		} else {
//			insert(token, type, number);
//		}
//		
//	}
//
//	private static final String UPDATE_INCREMENT_TOKEN = "update TOKENS_COUNT SET number=? where TOKEN=? and TYPE=?";
//
//	public int updateTokenValue(String token, String type,int number) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=UPDATE_INCREMENT_TOKEN;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//			
//			setInt(pstm, 1, number, query);
//			setString(pstm, 2, token, query);
//			setString(pstm, 3, type, query);
//			
//			return executeUpdate(pstm, query);
//			
//		} catch (DaoManagerException e) {
//			String error = " lastToken:'"+token+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}
//	}
//	
//
//	
//	private static final String EXISTS_FOR_TOKEN_TYPE = "select NUMBER from " + TABLE_NAME
//	+ " where TOKEN=? AND TYPE=? LIMIT 1";
//	
//	private boolean exists(String token, String type) throws DaoManagerException {
//		String query=EXISTS_FOR_TOKEN_TYPE;
//		Connection connection = getConnection();
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//	
//			setString(pstm, 1, token, query);
//			setString(pstm, 2, type, query);
//	
//			ResultSet rset = executeQuery(pstm,query);
//			
//			return rset.next();
//		} catch (SQLException e) {
//			String error = "token:'"+token+"' type:"+type;
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//	
//		} catch (DaoManagerException e) {
//			String error = "token:'"+token+"' type:"+type;
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}
//	}
//
//	private static final String DELETE_FOR_TYPE = "delete from "+TABLE_NAME+" where type=?";
//	
//	public void deleteForType(String type) throws DaoManagerException {
//		Connection connection = getConnection();
//		String query=DELETE_FOR_TYPE;
//		
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//						
//			setString(pstm, 1, type, query);
//			
//	
//			executeUpdate(pstm, query);
//		} catch (DaoManagerException e) {
//			String error = " type:'"+type;
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}		
//		
//	}
//
//	private static final String GET_COUNT = "SELECT number FROM "+TABLE_NAME+" where type=? and token=?;";
//
//	public int getCount(String token, String type) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=GET_COUNT;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//			
//			setString(pstm, 1, type, query);
//			setString(pstm, 2, token, query);
//			
//			return getIntFromQuery(pstm, query, 0);
//			
//		} catch (DaoManagerException e) {
//			String error = " token:'"+token+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}
//	}
//	
//
//	private static final String GET_COUNT_VAR = "SELECT min(number) FROM "+TABLE_NAME+" where type=? and ";
//
//	public int getCount(HashSet<String> tokens, String type) throws DaoManagerException {
//		Connection connection = getConnection();
//		StringBuilder buffer=new StringBuilder();
//
//		buffer.append(GET_COUNT_VAR);
//		for (int i=0;i<tokens.size();i++){
//			if (i != 0) {
//				buffer.append(" or ");			
//			}
//			buffer.append(" token=? ");
//		}
//		String query=buffer.toString();
//				
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//			
//			int i=1;
//			setString(pstm, i++, type, query);
//			for (String token:tokens){
//				setString(pstm, i++, token, query);
//			}
//			
//			return getIntFromQuery(pstm, query, 0);
//			
//		} catch (DaoManagerException e) {
//			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' type:"+type;
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}
//	}
//
//	public synchronized void truncateTable() throws DaoManagerException {
//		super.truncate();	
//	}
//
//}
