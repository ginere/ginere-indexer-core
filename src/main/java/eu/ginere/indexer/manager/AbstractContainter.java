package eu.ginere.indexer.manager;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import eu.ginere.base.util.test.TestInterface;
import eu.ginere.base.util.test.TestResult;
import eu.ginere.indexer.descriptor.IndexerObjectDescriptor;
import eu.ginere.indexer.updater.ObectUpdaterInterface;

public class AbstractContainter implements TestInterface{

    static final Logger log = Logger.getLogger(AbstractContainter.class);

	private final GlobalManager manager;

	public static final Hashtable<String, IndexerObjectDescriptor<?>> descriptorCache=new Hashtable<String, IndexerObjectDescriptor<?>>();

	public static final Hashtable<String, ObectUpdaterInterface<?>> updateCache=new Hashtable<String, ObectUpdaterInterface<?>>();

	protected AbstractContainter(GlobalManager manager){
		this.manager=manager;		
	}

	public void addUpdater(ObectUpdaterInterface<?> updater){
        updateCache.put(updater.getType(),updater);
    }

	public void addDescriptor(IndexerObjectDescriptor<?> descriptor){
        descriptorCache.put(descriptor.getType(),descriptor);
    }
	
	@Override
	public TestResult test() {
        TestResult ret=new TestResult(GlobalManager.class);

        ret.add(manager.test());
        
        return ret;
	}
    
    /*
	public void update(String type) {
		// TODO Auto-generated method stub
		
	}

	public void update(String type, String id) {
		// TODO Auto-generated method stub
		
	}
    */
}
