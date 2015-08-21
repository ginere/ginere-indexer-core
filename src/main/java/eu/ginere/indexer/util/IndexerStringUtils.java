package eu.ginere.indexer.util;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public abstract class IndexerStringUtils{

	static final Logger log = Logger.getLogger(IndexerStringUtils.class);
	private static final String SEPARATOR_CHARS = "'(){}[]|/-,.:;\"? \t\n\r";


	public static String getStringValue(Object value) {
		if (value == null){
			return null;
		} else if (value instanceof String){
			return (String)value;
		} else if (value instanceof Integer){
			return Integer.toString((Integer)value);
		} else if (value instanceof Collection<?>){
			Collection<?>collection=(Collection<?>)value;
			StringBuilder buffer=new StringBuilder();
			for (Object obj:collection){
				buffer.append(getStringValue(obj));
				buffer.append(',');
			}
			
			return buffer.toString();
		} else {
			return value.toString();
		}
	}

//	public static HashSet<String> getTokens(String stringValue) {
//		HashSet<String> ret=new HashSet<String> ();
//
//		getTokens(ret,stringValue,0);
//		return ret;
//	}

	

	public static HashSet<String> getTokens(String stringValue,int minTokenLength) {
		HashSet<String> ret=new HashSet<String> ();

		getTokens(ret,stringValue,minTokenLength);
		return ret;
	}

	public static void getTokens(HashSet<String> tokens,String stringValue,int minTokenLength) {
		if (stringValue == null){
			return ;
		} else if ("".equals(stringValue)){
			return ;			
		} else {
			
//			if (stringValue.contains(".")){
//				log.debug("Warning:"+stringValue);
//			}
			String array[]=StringUtils.split(stringValue,SEPARATOR_CHARS);
			
			for (String val:array){
//				if (val.length()>=minTokenLength && !StringUtils.isNumeric(val)){
				if (val.length()>=minTokenLength){
										
					// minusclas
					val=val.toLowerCase();
					// quitamos los acentos
					val=TokenUtils.cleanAcents(val);
//					
//					quitar los numeros ....array
//					
//					Quitar los caracteres especiales como / () etc ...
					
					
					tokens.add(val);
				}
			}
			
			return ;
		}
	}

//	/**
//	 * Función que elimina acentos y caracteres especiales de
//	 * una cadena de texto.
//	 * http://www.v3rgu1.com/blog/231/2010/programacion/eliminar-acentos-y-caracteres-especiales-en-java/
//	 * @param input
//	 * @return cadena de texto limpia de acentos y caracteres especiales.
//	 */
//	
//    private static final Pattern PATTERN_SIN_ACENTOS = Pattern.compile("[^\\p{ASCII}]");
//	
//	public static String cleanAcents(String input) {
//	    // Descomposición canónica
//	    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
//	    // Nos quedamos únicamente con los caracteres ASCII
////	    Pattern pattern = Pattern.compile("\\p{ASCII}");
//
//	    String ret=PATTERN_SIN_ACENTOS.matcher(normalized).replaceAll("");
//	    
//	    return ret;
//	}

	public static String tokenize(String input) {
		String ret=input.toLowerCase();
		ret=TokenUtils.cleanAcents(ret);

		return ret;
	}

	/**
	 * Devuelve le ultimo incimpleto token si lo hay
	 * @param tokens
	 * @return
	 */
	public static String getLastIncompleteToken(String tokens) {
		if (tokens == null){
			return null;
		} else if ("".equals(tokens) || tokens.endsWith(" ")){
			return null;			
		} else {
			String array[]=StringUtils.split(tokens);
			
			String lastToken=array[array.length-1];
			
			return lastToken;
		}
	}
	
	
	private static int minimum(int a, int b, int c) {
		if (a <= b && a <= c) {
			return a;
		}
		if (b <= a && b <= c) {
			return b;
		}
		return c;
	}
 
    public static int computeLevenshteinDistance(String str1, String str2) {
    	if (str1==null){
    		str1="";
    	}
    	if (str2==null){
    		str2="";
    	}
        return computeLevenshteinDistance(str1.toCharArray(),
                                          str2.toCharArray());
    }
	 

    private static int computeLevenshteinDistance(char [] str1, char [] str2) {
        int [][]distance = new int[str1.length+1][str2.length+1];
 
        for(int i=0;i<=str1.length;i++) {
                distance[i][0]=i;
        }
        for(int j=0;j<=str2.length;j++) {
                distance[0][j]=j;
        }
        
		for (int i = 1; i <= str1.length; i++) {
			for (int j = 1; j <= str2.length; j++) {
				
				distance[i][j] = minimum(distance[i - 1][j] + 1,
										 distance[i][j - 1] + 1, 
										 distance[i - 1][j - 1]	+ ((str1[i - 1] == str2[j - 1]) ? 0 : 1));
			}
		}
		
        return distance[str1.length][str2.length];
    }


}
