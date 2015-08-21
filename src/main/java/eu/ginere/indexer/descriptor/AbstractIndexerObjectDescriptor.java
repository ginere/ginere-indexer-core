package eu.ginere.indexer.descriptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.indexer.util.IndexerStringUtils;


public abstract class AbstractIndexerObjectDescriptor<T> implements IndexerObjectDescriptor<T> {

	static final Logger log = Logger.getLogger(AbstractIndexerObjectDescriptor.class);

	public  static final int MIN_TOKEN_LENGTH_TO_INDEX = 3;
	
	public final String type;
	public final Class<T> clazz;
	public final String fieldsNameArray[];
	public final Hashtable<String, Field>cache=new Hashtable<String, Field>();

	protected AbstractIndexerObjectDescriptor(Class<T> clazz,String fields[]) throws RuntimeException {
//		try {
//			this.type=type;
			this.type=clazz.getName();
			this.clazz=clazz;
			this.fieldsNameArray=fields;
			
			for (String fieldName:fieldsNameArray){
				try {
					Field field=clazz.getDeclaredField(fieldName);
					field.setAccessible(true); // You might want to set modifier to public first.
					
					this.cache.put(fieldName,field);
				}catch(NoSuchFieldException e){
					log.error("For the fields:'"+fieldName+"'",e);
				}
			}
//		}catch (Exception e) {
//			throw new RuntimeException("class:"+clazz+" fields:"+StringUtils.join(fields),e);
//		}
		
		ObjectDescriptorManager.subscribe(this);
	}

	public String getType(){
		return type;
	}

	public List<IndexerElement> getIndexerElements(T object) throws DaoManagerException{
		List<IndexerElement> ret=new ArrayList<IndexerElement>();
		String id=getObjectId(object);
		HashSet<String> tokensSet=new HashSet<String>();
		
		for (String fieldName:fieldsNameArray){
			try {
				Field field=cache.get(fieldName);
				if (field != null) {
					Object value=field.get(object);
					
					String stringValue=IndexerStringUtils.getStringValue(value);
					IndexerStringUtils.getTokens(tokensSet,stringValue,MIN_TOKEN_LENGTH_TO_INDEX);
				} else {
					log.warn("No field:'"+fieldName+"' in object type:'"+type+"' ["+clazz.getName()+"]");
				}
			}catch(IllegalAccessException e){
				//throw new DaoManagerException("While indexing field name:'"+fieldName+"' type:'"+type+"' for object id:'"+id+"'",e);
				log.error("While indexing field name:'"+fieldName+"' type:'"+type+"' for object id:'"+id+"'",e);
			}
		}

		for (String token:tokensSet){
			IndexerElement ir=new IndexerElement(token, type, id);
			
			ret.add(ir);
		}

		return ret;
	}
	
	/**
	 * returns the id of hte object
	 * @param object
	 * @return
	 */
	public abstract String getObjectId(T object);
	
	/**
	 * returns the object for this id or null if no object defined
	 * @param key
	 * @return
	 */
	protected abstract Object get(String key,String style);
	
	public Object get(IndexerResult result,String style){
		String id=result.getId();
		
		return get(id,style);		
	}

}
