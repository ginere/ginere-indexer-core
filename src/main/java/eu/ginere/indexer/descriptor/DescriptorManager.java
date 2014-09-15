package eu.ginere.indexer.descriptor;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import eu.ginere.indexer.indexer.IndexerResult;

public class DescriptorManager {

	static final Logger log = Logger.getLogger(DescriptorManager.class);

	
	public static final Hashtable<String, IndexerDescriptor<?>> cache=new Hashtable<String, IndexerDescriptor<?>>();

	
//	public static final PerfilIndexerDescriptor DESCRIPTOR=PerfilIndexerDescriptor.DESCRIPTOR;

	
	public static void subscribe(IndexerDescriptor<?> descriptor){
		String key=descriptor.getType();
		
		cache.put(key, descriptor);
	}

	
	public static Object get(IndexerResult result,String style) {
		String type=result.getType();
		IndexerDescriptor<?>descriptor=cache.get(type);
		
		if (descriptor == null){
			log.error("No descriptor for type:'"+type+"'");
			return null;
		} else {
			return descriptor.get(result,style);
		}
	}

}
