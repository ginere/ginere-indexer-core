package eu.ginere.indexer.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class TokenUtils {

	static final Logger log = Logger.getLogger(TokenUtils.class);
	
	public static final List<String> EMPTY_LIST=new ArrayList<String>(0);

	public static final Pattern TOKEN_PATER=Pattern.compile("([a-zA-Z][a-zA-Z0-9']+)");

	public static List <String>  getTokens(String value){
		if (value == null || "".equals(value)){
			return EMPTY_LIST;
		} else {
			String stringToParse=cleanAcents(value);
			Matcher matcher = TOKEN_PATER.matcher(stringToParse);
			List <String> ret=new ArrayList<String>();
			
			while (matcher.find()) {
				String token=matcher.group().toLowerCase();

				ret.add(token);
			}

			return ret;
		}
	}


	// \p{ASCII}	All ASCII:[\x00-\x7F]
    private static final Pattern PATTERN_SIN_ACENTOS = Pattern.compile("[^\\p{ASCII}]");
	
	/**
	 * Función que elimina acentos y caracteres especiales de
	 * una cadena de texto.
	 * http://www.v3rgu1.com/blog/231/2010/programacion/eliminar-acentos-y-caracteres-especiales-en-java/
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */	
	public static String cleanAcents(String input) {
	    // Descomposición canónica transforma é en e'
	    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
//		log.debug("Normalized:"+normalized);

		// busca todos los caracteres que no son ascii y los elimina
	    String ret=PATTERN_SIN_ACENTOS.matcher(normalized).replaceAll("");

//		log.debug("Result:"+ret);
	    
	    return ret;
	}


//	 public static void main(String[] args){
//	        Console console = System.console();
//	        if (console == null) {
//	            System.err.println("No console.");
//	            System.exit(1);
//	        }
//	        while (true) {
//	        	// String line=console.readLine("%nEnter your regex: ");
//	        	String line="(\\w+)";
//	        	
//	        	System.out.println("reg exp:"+line);
//	            Pattern pattern =Pattern.compile(line);
//
//	            while (true){
//		            line=console.readLine("Enter input string to search: ");
////		            line=line.toUpperCase();
//		            
//		        	System.out.println("Line to match:"+line);
//		            Matcher matcher =  pattern.matcher(line);
//		
//		            boolean found = false;
//		            while (matcher.find()) {
//		                console.format("I found the text" +
//		                    " \"%s\" starting at " +
//		                    "index %d and ending at index %d.%n",
//		                    matcher.group(),
//		                    matcher.start(),
//		                    matcher.end());
////		                
////		                	for (int i=0;i<matcher.groupCount();i++){
////		    	                console.format("%d group:%s",
////		    	                		i,
////		    		                    matcher.group(i));
////		                		
////		                	}
////		            	for (int i=1;i<=matcher.groupCount();i++){
////		                	System.out.println(" ["+i+"]:"+matcher.group(i));
////		            	}
//
//		                found = true;
//		            }
//		            if(!found){
//		                console.format("No match found.%n");
//		            }
//	            }
//	        }
}
