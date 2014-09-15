package eu.ginere.indexer.indexer;
//package avem.indexer.indexer;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import avem.common.util.dao.DaoManagerException;
//import avem.jdbc.dao.AbstractSQLDAO;
//
//public class GlobalIndexerDAO extends AbstractSQLDAO{
//
//	static final Logger log = Logger.getLogger(GlobalIndexerDAO.class);
//	
//	static final private String TABLE_NAME= "GLOBAL_INDEXER";
//	static final private String[] COLUMNS=new String[] {
//		"TOKENS", 
//		"TYPE",
//		"ID"
//	};
//
//	static final private String CREATE_QUERY_ARRAY[][]=new String[][]{
//		{ // V1
//			"CREATE TABLE "+TABLE_NAME+" ("
//			+ "		TOKENS varchar(200) NOT NULL,"
//			+ "		TYPE varchar(64) NOT NULL,"
//			+ "		ID varchar(32) NOT NULL,"
//			+ "		UPDATED timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"			
//			+ "		FULLTEXT (TOKENS),"			
//			+ " 	UNIQUE (TOKENS,TYPE,ID)"
//			+ ") ENGINE = MYISAM ;",  // MYISAM
//		},
//	};
//
//	public static final GlobalIndexerDAO DAO=new GlobalIndexerDAO();
//
//	private GlobalIndexerDAO(){
//		super(TABLE_NAME,CREATE_QUERY_ARRAY);		
//	}
//
//	private static final String INSERT = "insert into "+TABLE_NAME+
//		" (TOKENS,TYPE,ID) values (?,?,?)";
//
//	public void insert(IndexerElement indx) throws DaoManagerException{
//		insert(indx.getToken(),
//			   indx.getType(),
//			   indx.getId());
//	}
//
//	public void insert(String tokens,String type,String id) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=INSERT;
//	
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//
//			setString(pstm, 1, tokens, query);
//			setString(pstm, 2, type, query);
//			setString(pstm, 3, id, query);
//	
//			int number=executeUpdate(pstm, query);
//	
//		} catch (DaoManagerException e) {
//			String error = "tokens:'"+tokens
//				+ "' type:'"+type
//				+ "' id:'"+id+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}		
//	}
//
//	private static final String DELETE = "delete from "+TABLE_NAME+
//	" where TYPE=? and ID=?";
//
//	public void delete(String type, String id) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=DELETE;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//			
//			setString(pstm, 1, type, query);
//			setString(pstm, 2, id, query);
//	
//			int number=executeUpdate(pstm, query);
//	
//			if (log.isDebugEnabled()){
//				log.debug("Rows modified:"+number);
//			}
//		} catch (DaoManagerException e) {
//			String error = " type:'"+type
//				+ "' id:'"+id+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}		
//	}
//
//	private static final String SEARCH = "select tokens,type,id from "+TABLE_NAME+ //MYISAM
//		" where MATCH(TOKENS) AGAINST (? IN BOOLEAN MODE) "; 
//	private static final String COUNT = "select count(*) from "+TABLE_NAME+  // MYISAM
//		" where MATCH(TOKENS) AGAINST (? IN BOOLEAN MODE) ";
//	public List<IndexerResult> search(String searchString) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=SEARCH;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//			
//			setString(pstm, 1, searchString, query);
//	
//			ResultSet rset=executeQuery(pstm, query);
//	
//			List<IndexerResult> list= new ArrayList<IndexerResult>(rset.getFetchSize());
//			
//			while (rset.next()){
//				String token=rset.getString(1);
//				String type=rset.getString(2);
////				String field=rset.getString(3);
//				String id=rset.getString(3);
//
//				IndexerResult result=new IndexerResult(token,type,id);
//				list.add(result);
//			}
//			return list;
//		} catch (SQLException e) {
//			String error = " searchString:'"+searchString+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);			
//		} catch (DaoManagerException e) {
//			String error = " searchString:'"+searchString+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}		
//	}
//	
//	public int count(String searchString) throws DaoManagerException{
//		Connection connection = getConnection();
//		String query=COUNT;
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//			
//			setString(pstm, 1, searchString, query);
//	
//			return getIntFromQuery(pstm, query, 0);
//			
//		} catch (DaoManagerException e) {
//			String error = " searchString:'"+searchString+"'";
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}		
//	}
//}