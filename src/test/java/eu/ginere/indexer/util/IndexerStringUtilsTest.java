package eu.ginere.indexer.util;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.junit.Test;

public class IndexerStringUtilsTest {

	static final Logger log = Logger.getLogger(IndexerStringUtilsTest.class);
	@Test
	public void testInsert() throws Exception {
		try {
			
			String a=null;
			String b=null;
			int ret;
			
			HashSet<String> tokens=new HashSet<String>();
			
			IndexerStringUtils.getTokens(tokens,"Guerra.De.Las",3);
			
			log.debug("Tokends '"+tokens);
			
			
			ret=IndexerStringUtils.computeLevenshteinDistance(a, b);
			
			log.debug("Libestein '"+a+"' '"+b+"'="+ret);
			
			
			a="Sant Vicenç dels Horts";
			b="San Vicente dels Horts";
			ret=IndexerStringUtils.computeLevenshteinDistance(a, b);
			
			log.debug("Libestein '"+a+"' '"+b+"'="+ret);
			
		}catch (Exception e) {
			log.error("", e);
			throw e;
		}	
	}
}
