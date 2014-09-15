package eu.ginere.indexer.indexer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.manager.AbstractManager;
import eu.ginere.indexer.util.IndexerStringUtils;

public class AutoCompleteManager extends AbstractManager {
	
	static final Logger log = Logger.getLogger(AutoCompleteManager.class);

	/**
	 * Numero de elementos maximos que devuelve el auto complete
	 */
	static public final int NUMBER_AUTO_COMPLETE=5;
	
	/**
	 * Propone autocompletacion en la busqueda. DEvuelve en primer lugar la cadenas de busqeuda que dan mas resultados
	 * 
	 * @param tokens Los tokens que identifican la busqueda
	 * @param number Numero de cadenas propuestass
	 * @return
	 */
	public static List<String> searchAutoComplete(String tokens,String type){
		// obtenemos el ultimo token que es el que hay que auto completar
		String lastToken=IndexerStringUtils.getLastIncompleteToken(tokens);

		// With DNI numbers ther are 2800000 tokens that start with a number
		// We have to vaoid that
		boolean isNumeric=StringUtils.isNumeric(lastToken);
		if (lastToken !=null && ((isNumeric && lastToken.length()>=3) || !isNumeric)){
			try {
				
//				Usar el TokenDAO
				
				// Obtenemos las completaciones
//				List <String> list=IndexerDAO.DAO.getAutoComplete(lastToken,NUMBER_AUTO_COMPLETE);
				List <String> list=TokenDAO.DAO.getAutoComplete(lastToken,type,NUMBER_AUTO_COMPLETE);
				List <String> ret=new ArrayList<String>(list.size());
				
				// devolvemos una lista de valores con el comienzo de cada valor igia al los tokens
				// y al final el valor propuesto
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
