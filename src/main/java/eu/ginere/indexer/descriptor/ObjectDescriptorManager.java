package eu.ginere.indexer.descriptor;

import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 * This use this class to subcrive Objects Descriptor and to retreyve the object descriptor associated to one object.
 * 
 * @author ginere
 *
 */
public class ObjectDescriptorManager {

	static final Logger log = Logger.getLogger(ObjectDescriptorManager.class);

	
	public static final Hashtable<String, IndexerObjectDescriptor<?>> cache=new Hashtable<String, IndexerObjectDescriptor<?>>();

	
//	public static final PerfilIndexerDescriptor DESCRIPTOR=PerfilIndexerDescriptor.DESCRIPTOR;

	
	public static void subscribe(IndexerObjectDescriptor<?> descriptor){
		String key=descriptor.getType();
		
		cache.put(key, descriptor);
	}

	
	public static Object get(IndexerResult result,String style) {
		String type=result.getType();
		IndexerObjectDescriptor<?>descriptor=cache.get(type);
		
		if (descriptor == null){
			log.error("No descriptor for type:'"+type+"'");
			return null;
		} else {
			return descriptor.get(result,style);
		}
	}

}
