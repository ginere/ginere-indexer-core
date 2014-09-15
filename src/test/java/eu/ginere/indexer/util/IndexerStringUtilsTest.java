package eu.ginere.indexer.util;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.ginere.indexer.util.IndexerStringUtils;

public class IndexerStringUtilsTest {

	static final Logger log = Logger.getLogger(IndexerStringUtilsTest.class);
	@Test
	public void testInsert() throws Exception {
		try {
			
			String a=null;
			String b=null;
			int ret;
			
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
