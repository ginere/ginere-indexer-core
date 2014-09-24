package eu.ginere.indexer.hsqldb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.hsqldb.dao.AbstractDAO;
import eu.ginere.indexer.descriptor.IndexerElement;
import eu.ginere.indexer.descriptor.IndexerResult;

public class IndexerDAO extends AbstractDAO{

	static final Logger log = Logger.getLogger(IndexerDAO.class);
	
	static final private String TABLE_NAME= "INDEXER";
	static final private String[] COLUMNS=new String[] {
		"TOKEN", 
		"TYPE",
//		"FIELD",
		"ID"
	};

	static final private String CREATE_QUERY_ARRAY[][]=new String[][]{
		{ // V1
			"CREATE TABLE "+TABLE_NAME+" ("
			+ "		TOKEN varchar(200) NOT NULL,"
			+ "		TYPE varchar(128) NOT NULL,"
//			+ "		FIELD varchar(32) NOT NULL,"
			+ "		ID varchar(32) NOT NULL"
//			+ "		ID int UNSIGNED NOT NULL,"
//			+ "		UPDATED timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
//			+ "		FULLTEXT (TOKEN)"
//			+ ") ENGINE = MYISAM ;",  // MYISAM
			
//			+ " 	UNIQUE (TOKEN,TYPE,FIELD,ID),"
//			+ " 	UNIQUE (TOKEN,TYPE,ID),"
//			+ " 	INDEX (TOKEN)"
//			+ "		UPDATED timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
//			CREATE INDEX INDEXER_ID_TYPE ON INDEXER (ID,TYPE);
			+ ");",
//			"create index TYPE_TOKEN_INDEX ON INDEXER (TYPE,TOKEN);",
			"create index TYPE_TOKEN_INDEX ON INDEXER (TYPE,TOKEN);",
			//			"create unique index INDEXER_TOKEN_TYPE_ID ON INDEXER (TOKEN,TYPE,ID);"
		},
	};
	
	static final private String CREATE_INDEX_ARRAY[][]=new String[][]{
//		{ "TYPE_TOKEN_INDEX","ALTER TABLE "+TABLE_NAME+" ADD INDEX `TYPE_TOKEN_INDEX` (TYPE, TOKEN) ;"},
//		SEARCH_ONE_START_TYPE
		{ "TYPE_TOKEN_INDEX","ALTER TABLE "+TABLE_NAME+" ADD INDEX `TYPE_TOKEN_INDEX_ID` (TYPE, TOKEN, ID) ;"},
		
		};
	
//	SELECT ID FROM INDEXER WHERE ID in (
//			  SELECT ID FROM INDEXER where TOKEN='madrid' AND TYPE='PERFIL'
//			) and TOKEN='maria' AND TYPE='PERFIL'
//			order by ID desc LIMIT 1,20;
//
//			create index INDEXER_TOKEN_TYPE ON INDEXER (TOKEN,TYPE);
//			create unique index INDEXER_TOKEN_TYPE_ID ON INDEXER (TOKEN,TYPE,ID);
//
//			drop index INDEXER_TOKEN_TYPE on INDEXER;
//			drop index INDEXER_TOKEN_TYPE_ID on INDEXER;

	public static final IndexerDAO DAO=new IndexerDAO();

	private IndexerDAO(){
		super(TABLE_NAME,CREATE_QUERY_ARRAY);
	}

	private static final String INSERT = "insert into "+TABLE_NAME+
		" (TOKEN,TYPE,ID) values (?,?,?)";

	public void insert(IndexerElement indx) throws DaoManagerException{
		insert(indx.getToken(),
			   indx.getType(),
//			   indx.getField(),
			   indx.getId());
	}

	public void insert(String token,String type,String id) throws DaoManagerException{
		Connection connection = getConnection();
		String query=INSERT;
	
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				setString(pstm, 1, token, query);
				setString(pstm, 2, type, query);
				//			setString(pstm, 3, field, query);
				setString(pstm, 3, id, query);
				
				int number=executeUpdate(pstm, query);
				
				//			if (log.isDebugEnabled()){
				//				log.debug("Rows modified:"+number);
				//			}
			} finally {
				close(pstm);
			}
		} catch (DaoManagerException e) {
			String error = "token:'"+token
				+ "' type:'"+type
//				+ "' field:'"+field
				+ "' id:'"+id+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}

	private static final String DELETE = "delete from "+TABLE_NAME+
	" where TYPE=? and ID=?";

	public void delete(String type, String id) throws DaoManagerException{
		Connection connection = getConnection();
		String query=DELETE;
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				setString(pstm, 1, type, query);
				setString(pstm, 2, id, query);
				
				int number=executeUpdate(pstm, query);
				
				if (log.isDebugEnabled()){
					log.debug("Rows modified:"+number);
				}
			} finally {
				close(pstm);
			}
		} catch (DaoManagerException e) {
			String error = " type:'"+type
				+ "' id:'"+id+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}

//	private static final String SEARCH = "select token,type,id from "+TABLE_NAME+ //MYISAM
//	" where MATCH(TOKEN) AGAINST (? IN BOOLEAN MODE) "; 
//	private static final String COUNT = "select count(*) from "+TABLE_NAME+  // MYISAM
//	" where MATCH(TOKEN) AGAINST (? IN BOOLEAN MODE) ";

	private static final String SEARCH = "select type,id from "+TABLE_NAME+
	" where token like ? ";
	private static final String COUNT = "select count(*) from "+TABLE_NAME+
	" where token like ?  ";

	public List<IndexerResult> search(String searchString) throws DaoManagerException{
		Connection connection = getConnection();
		String query=SEARCH;
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				setString(pstm, 1, searchString, query);
				
				ResultSet rset=executeQuery(pstm, query);
				
				List<IndexerResult> list= new ArrayList<IndexerResult>(rset.getFetchSize());
				
				while (rset.next()){
					String token=rset.getString(1);
					String type=rset.getString(2);
					//				String field=rset.getString(3);
					String id=rset.getString(3);
					
					IndexerResult result=new IndexerResult(type,id);
					list.add(result);
				}
				return list;
			} finally {
				close(pstm);
			}
		} catch (SQLException e) {
			String error = " searchString:'"+searchString+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);			
		} catch (DaoManagerException e) {
			String error = " searchString:'"+searchString+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}
	
	public int count(String searchString) throws DaoManagerException{
		Connection connection = getConnection();
		String query=COUNT;
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				setString(pstm, 1, searchString, query);
				
				return getIntFromQuery(pstm, query, 0);
			} finally {
				close(pstm);
			}			
		} catch (DaoManagerException e) {
			String error = " searchString:'"+searchString+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}

	
	private static final String AUTO_COMPLETE = "SELECT token,count(*) as number FROM "+TABLE_NAME+" where token like ? group by token order by number desc LIMIT ?";

	public List<String> getAutoComplete(String lastToken, int numberAutoComplete) throws DaoManagerException{
		Connection connection = getConnection();
		String query=AUTO_COMPLETE;
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				//			String value=lastToken.replace(""%", '');
				
				setString(pstm, 1, lastToken+'%', query);
				setInt(pstm, 2, numberAutoComplete, query);
				
				return getStringList(pstm, query);
			} finally {
				close(pstm);
			}
		} catch (DaoManagerException e) {
			String error = " lastToken:'"+lastToken+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}
	}
	
	private static final String GET_TOKEN_LIST = "SELECT distinct(token) from  "+TABLE_NAME+" where type=?";

	public List<String> getTokenList(String type) throws DaoManagerException{
		Connection connection = getConnection();
		String query=GET_TOKEN_LIST;
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			
			setString(pstm, 1, type, query);
			
			return getStringList(pstm, query);			
		} catch (DaoManagerException e) {
			String error = " type:'"+type+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}
	}

	private static final String GET_TOKEN_COUNT = "SELECT count(TOKEN) from  "+TABLE_NAME+" where token=? AND type=?";
	public int getTokenCount(String token, String type) throws DaoManagerException{
		Connection connection = getConnection();
		String query=GET_TOKEN_COUNT;
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				setString(pstm, 1, token, query);
				setString(pstm, 2, type, query);
				
				return getIntFromQuery(pstm, query, 0);		
			} finally {
				close(pstm);
			}
		} catch (DaoManagerException e) {
			String error = "token:'"+token+"' type:'"+type+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}
	}

	private static final String SEARCH_VAR_START = "SELECT ID,TYPE,count(*) as number FROM "+TABLE_NAME+" where ";
	// token='ana' or token='caballero'
	private static final String SEARCH_VAR_END = " group by ID,TYPE order by number desc LIMIT ?,?";

	public List<IndexerResult> search(HashSet <String>tokens,int first,int number) throws DaoManagerException{
		Connection connection = getConnection();
		StringBuilder buffer=new StringBuilder();

		buffer.append(SEARCH_VAR_START);
		for (int i=0;i<tokens.size();i++){
			if (i != 0) {
				buffer.append(" or ");			
			}
			buffer.append(" token=? ");
		}
		buffer.append(SEARCH_VAR_END);
		String query=buffer.toString();
		
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				int i=1;
				for (String token:tokens){
					setString(pstm, i++, token, query);
				}
				setInt(pstm, i++, first, query);
				setInt(pstm, i++, number, query);
				
				ResultSet rset=executeQuery(pstm, query);
				
				List<IndexerResult> list= new ArrayList<IndexerResult>(rset.getFetchSize());
				
				while (rset.next()){
					//String token=rset.getString(1);
					//				String field=rset.getString(3);
					String id=rset.getString(1);
					String type=rset.getString(2);
					
					IndexerResult result=new IndexerResult(type,id);
					list.add(result);
				}
				return list;
			} finally {
				close(pstm);
			}
		} catch (SQLException e) {
			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' first:"+first+" number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);			
		} catch (DaoManagerException e) {
			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' first:"+first+" number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}




	private static final String SEARCH_VAR_START_TYPE = "SELECT ID,count(*) as number FROM "+TABLE_NAME+" where TYPE=? AND (";
	// token='ana' or token='caballero'
	private static final String SEARCH_VAR_END_TYPE = " ) group by ID order by number desc,ID asc LIMIT ?,?";

	public List<IndexerResult> searchType(HashSet <String>tokens,String type,int first,int number) throws DaoManagerException{
		Connection connection = getConnection();
		StringBuilder buffer=new StringBuilder();

		buffer.append(SEARCH_VAR_START_TYPE);
		for (int i=0;i<tokens.size();i++){
			if (i != 0) {
				buffer.append(" or ");			
			}
			buffer.append(" token=? ");
		}
		buffer.append(SEARCH_VAR_END_TYPE);
		String query=buffer.toString();
		
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				int i=1;
				setString(pstm, i++, type, query);
				for (String token:tokens){
					setString(pstm, i++, token, query);
				}
				setInt(pstm, i++, first, query);
				setInt(pstm, i++, number, query);
				
				ResultSet rset=executeQuery(pstm, query);
				
				List<IndexerResult> list= new ArrayList<IndexerResult>(rset.getFetchSize());
				
				while (rset.next()){
					//String token=rset.getString(1);
					//				String field=rset.getString(3);
					String id=rset.getString(1);
					//				String type=rset.getString(2);
					
					IndexerResult result=new IndexerResult(type,id);
					list.add(result);
				}
				return list;
			} finally {
				close(pstm);
			}
		} catch (SQLException e) {
			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' first:"+first+" number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);			
		} catch (DaoManagerException e) {
			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' first:"+first+" number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}

//	SELECT ID FROM INDEXER where ID in (
//			  SELECT ID FROM INDEXER WHERE ID in (
//			    SELECT ID as number FROM INDEXER where TYPE='PERFIL' AND token='garcia')
//			  and token='maria' AND TYPE='PERFIL')
//			 and TOKEN='madrid' AND TYPE='PERFIL'
//			order by ID desc
//			LIMIT 1,20;

	public List<IndexerResult> searchTypeBis(Collection<String>tokens,String type,int first,int number) throws DaoManagerException{
		Connection connection = getConnection();
		StringBuilder buffer=new StringBuilder();

		for (int i=0;i<tokens.size();i++){
			if (i == 0) {
				buffer.append("SELECT ID FROM INDEXER where TOKEN=? AND TYPE=?");			
			} else {
				buffer=buffer.insert(0,"SELECT ID FROM INDEXER WHERE ID in (");
				
				buffer.append(") and TOKEN=? AND TYPE=?");
			}
		}
		buffer.append(" order by ID desc LIMIT ?,? ");
		String query=buffer.toString();
		
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
						
			int i=1;
			for (String token:tokens){
				setString(pstm, i++, token, query);
				setString(pstm, i++, type, query);
			}
			setInt(pstm, i++, first, query);
			setInt(pstm, i++, number, query);
	
			ResultSet rset=executeQuery(pstm, query);
	
			List<IndexerResult> list= new ArrayList<IndexerResult>(rset.getFetchSize());
			
			while (rset.next()){
				//String token=rset.getString(1);
//				String field=rset.getString(3);
				String id=rset.getString(1);
//				String type=rset.getString(2);

				IndexerResult result=new IndexerResult(type,id);
				list.add(result);
			}
			return list;
		} catch (SQLException e) {
			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' first:"+first+" number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);			
		} catch (DaoManagerException e) {
			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' first:"+first+" number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}
	

	// select count(*) from (SELECT ID FROM INDEXER where token='ana' or token='caballero' group by ID,TYPE ) as sq;
	private static final String COUNT_VAR_START = "select count(*) from (SELECT ID FROM "+TABLE_NAME+" where type=? AND ";
	// token='ana' or token='caballero'
	private static final String COUNT_VAR_END = " group by ID ) as sq";

	public int count(HashSet <String>tokens,String type) throws DaoManagerException{
		Connection connection = getConnection();
		StringBuilder buffer=new StringBuilder();

		buffer.append(COUNT_VAR_START);
		for (int i=0;i<tokens.size();i++){
			if (i != 0) {
				buffer.append(" or ");			
			}
			buffer.append(" token=? ");
		}
		buffer.append(COUNT_VAR_END);
		String query=buffer.toString();
		
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
						
			int i=1;
			setString(pstm, i++, type, query);
			for (String token:tokens){
				setString(pstm, i++, token, query);
			}
	
			return getIntFromQuery(pstm, query, 0);
		} catch (DaoManagerException e) {
			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' type:"+type;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}

	private static final String DELETE_FOR_TYPE = "delete  from "+TABLE_NAME+" where type=?";
	
	public void deleteForType(String type) throws DaoManagerException {
		Connection connection = getConnection();
		String query=DELETE_FOR_TYPE;
		
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				setString(pstm, 1, type, query);
				
				
				executeUpdate(pstm, query);
			} finally {
				close(pstm);
			}
		} catch (DaoManagerException e) {
			String error = " type:'"+type;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}				
	}

	
////	SELECT POBLACION.POBLACION,INDEXER.ID,INDEXER.TYPE,count(INDEXER.ID) as number FROM INDEXER,POBLACION 
////	where 
////	  (token='las' or token='rozas' or token='madrid') and type='POBLACION' 
////	  and POBLACION.ID=INDEXER.ID
////	  and POBLACION.PROVINCIA_ID='28'
////	group by INDEXER.ID order by number desc LIMIT 1
//	
//	private static final String SEARCH_POBLACION_VAR_START = "SELECT INDEXER.ID,count(INDEXER.ID) as number FROM INDEXER,POBLACION"+ 
//	"	where ( ";
//	//	  token='las' or token='rozas' or token='madrid'
//	private static final String SEARCH_POBLACION_VAR_END = ") and type='POBLACION' and POBLACION.ID=INDEXER.ID and POBLACION.PROVINCIA_ID=? and number > 1"+
//	"	group by INDEXER.ID order by number desc LIMIT 1";
//	
//	public String searchPoblacion(HashSet <String>tokens, String provinciaId) throws DaoManagerException {
//		Connection connection = getConnection();
//		StringBuilder buffer=new StringBuilder();
//
//		buffer.append(SEARCH_POBLACION_VAR_START);
//		for (int i=0;i<tokens.size();i++){
//			if (i != 0) {
//				buffer.append(" or ");			
//			}
//			buffer.append(" token=? ");
//		}
//		buffer.append(SEARCH_POBLACION_VAR_END);
//		String query=buffer.toString();
//		
//		
//		try {
//			PreparedStatement pstm = getPrepareStatement(connection,query);
//						
//			int i=1;
//			for (String token:tokens){
//				setString(pstm, i++, token, query);
//			}
//			setString(pstm, i++, provinciaId, query);
//
//			ResultSet rset=executeQuery(pstm, query);
//	
//			if (rset.next()){
//				String ret=rset.getString(1);
//				
//				return ret;
//			} else {
//				return null;
//			}
//		} catch (SQLException e) {
//			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' provinciaId:"+provinciaId;
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);			
//		} catch (DaoManagerException e) {
//			String error = " searchString:'"+StringUtils.join(tokens, ",")+"' provinciaId:"+provinciaId;
//			
//			log.error(error, e);
//			throw new DaoManagerException(error, e);
//		} finally {
//			closeConnection(connection);
//		}
//		
//	}
	

	private static final String SEARCH_ONE_START_TYPE = "SELECT ID FROM "+TABLE_NAME+" where TYPE=? AND token=? order by ID desc LIMIT ?,?";

	public List<IndexerResult> searchType(String token,String type,int first,int number) throws DaoManagerException{
		Connection connection = getConnection();
		String query=SEARCH_ONE_START_TYPE;
		
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
						
			int i=1;
			setString(pstm, i++, type, query);
			setString(pstm, i++, token, query);
			setInt(pstm, i++, first, query);
			setInt(pstm, i++, number, query);
	
			ResultSet rset=executeQuery(pstm, query);
	
			List<IndexerResult> list= new ArrayList<IndexerResult>(rset.getFetchSize());
			
			while (rset.next()){
				String id=rset.getString(1);
				IndexerResult result=new IndexerResult(type,id);
				list.add(result);
			}
			return list;
		} catch (SQLException e) {
			String error = " searchString:'"+token+"' type:"+type+"' first:"+first+"number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);			
		} catch (DaoManagerException e) {
			String error = " searchString:'"+token+"' type:"+type+"' first:"+first+"number:"+number;
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}		
	}

	private static final String ITERATE_OVER_TOKEN_LIST = "SELECT token,count(*) from  "+TABLE_NAME+" where type=? group by token";

	public interface IteratorOventTokenList{
		void iterate(String type,String token,int count);
	}
	
	public void iterateOverTokenList(String type,IteratorOventTokenList iterator) throws DaoManagerException{
		long time=System.currentTimeMillis();
		Connection connection = getConnection();
		String query=ITERATE_OVER_TOKEN_LIST;
		
		try {
			PreparedStatement pstm = getPrepareStatement(connection,query);
			try {
				setString(pstm, 1, type, query);
				
				ResultSet rset=executeQuery(pstm, query);
				log.error("Time:"+(System.currentTimeMillis()-time));
				while (rset.next()){
					String token=rset.getString(1);
					int number=rset.getInt(2);

					iterator.iterate(type,token,number);
				}
			
				return ;			
			}finally{
				close(pstm);
			}
		} catch (SQLException e) {
			String error = " type:'"+type+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);			
		} catch (DaoManagerException e) {
			String error = " type:'"+type+"'";
			
			log.error(error, e);
			throw new DaoManagerException(error, e);
		} finally {
			closeConnection(connection);
		}
	}

	public void createIndexes() {
		super.createIndexes(CREATE_INDEX_ARRAY);		
	}
	public void dropIndexes() {
		super.dropIndexes(CREATE_INDEX_ARRAY);		
	}

}
