package eu.ginere.indexer.indexer;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IndexerElement implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = "$Version$".hashCode();
	
	private final String token;
	private final String type;
	private final String id;

	public IndexerElement(String tokens, 
				  String type, 
				  String id) {
		this.token=tokens;
		this.type=type;
		this.id=id;
	}

	public String getToken(){
		return token;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.SIMPLE_STYLE);		
	}

}


