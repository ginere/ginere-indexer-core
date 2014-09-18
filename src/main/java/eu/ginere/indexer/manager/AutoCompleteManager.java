package eu.ginere.indexer.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.test.TestResult;
import eu.ginere.indexer.dao.IndexerDAOInterface;
import eu.ginere.indexer.dao.TokenDAOInterface;
import eu.ginere.indexer.util.IndexerStringUtils;

/**
 * Use this to get the autocompletation list 
 * 
 * @author ginere
 *
 */
public class AutoCompleteManager extends AbstractIndexerManager{
	
	static final Logger log = Logger.getLogger(AutoCompleteManager.class);
	    	
    AutoCompleteManager(IndexerDAOInterface indexerDAO,TokenDAOInterface tokenDAO){
        super(indexerDAO,tokenDAO);
    }

	
    /**
	 * @return The test result.
	 */
	public TestResult test(){
        return super.testProtected();
    }

    
	/**
	 * Propone autocompletacion en la busqueda. DEvuelve en primer lugar la cadenas de busqeuda que dan mas resultados
	 * 
	 * @param tokens Los tokens que identifican la busqueda
	 * @param number Numero de cadenas propuestass
	 * @return
	 */
	public List<String> searchAutoComplete(String tokens,String type,int numberOfElements){
		// We get the last token that we have to autocomplete.
		
		String lastToken=IndexerStringUtils.getLastIncompleteToken(tokens);

		// We have to choose if number will be autocompleted.
		
		boolean isNumeric=StringUtils.isNumeric(lastToken);
		
		if (lastToken !=null && ((isNumeric && lastToken.length()>=3) || !isNumeric)){
			try {
				
				List <String> list=tokenDAO.getAutoComplete(lastToken,type,numberOfElements);
				List <String> ret=new ArrayList<String>(list.size());
				
				// We return a list of autocomplete string ex: "from One upon a t" we will return 
				// "from One upon a time","from One upon a temp","from One upon a tall", etc ...
				String start=tokens.substring(0,tokens.lastIndexOf(lastToken));
				for (String auto:list){
					ret.add(start+auto);
				}
	
				return ret;
			}catch (DaoManagerException e) {
				log.error("Tokens:"+tokens,e);
				List <String> ret=new ArrayList<String>(1);
				ret.add(tokens);
				return ret;
			}
		} else {
			List <String> ret=new ArrayList<String>(1);
			ret.add(tokens);
			return ret;
		}		
	}
}
