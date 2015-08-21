package eu.ginere.indexer.descriptor;

import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.ginere.indexer.descriptor.AbstractIndexerObjectDescriptor;
import eu.ginere.indexer.descriptor.IndexerElement;

public class AbstractIndexerDescriptorTest extends TestCase {

	static final Logger log = Logger.getLogger(AbstractIndexerDescriptorTest.class);

    public static class Person {
        public final String nationaIdCardNumber;
       public final int age;
        public final String firstname;
        public final String lastname;
 
        public Person(String nationaIdCardNumber,int age, String firstname,String lastname){
            this.nationaIdCardNumber=nationaIdCardNumber;
            this.age=age;
            this.firstname=firstname;
            this.lastname=lastname;
        }
    }

    public static String FIELDS[]={"nationaIdCardNumber","age","firstname","lastname"};
    
    public static class PersonDescriptor extends AbstractIndexerObjectDescriptor<Person>{
    	
    	private final static Hashtable<String, Person> cache=new Hashtable<String, AbstractIndexerDescriptorTest.Person>();
    	
    	private PersonDescriptor(){
    		super(Person.class,FIELDS);
    	}
    	
		@Override
		public String getObjectId(Person object) {
			cache.put( object.nationaIdCardNumber, object);
			return object.nationaIdCardNumber;
		}

		@Override
		protected Person get(String key, String style) {
			return cache.get(key);
		}

    }

    static final PersonDescriptor DESCRIPTOR=new PersonDescriptor();
    
	@Test
	static public void testConsulta() throws Exception {
		try {
            
			Person angel=new Person("angelNumber", 34, "Angel", "Sanchez");
			
			List<IndexerElement> list=DESCRIPTOR.getIndexerElements(angel);
			
			for (IndexerElement element:list){
				log.info("Element:"+element);
			}
			
	    } catch (Exception e) {
	        log.error("", e);
	        throw e;
	    }
		
	}

}
