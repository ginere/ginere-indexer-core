package eu.ginere.indexer.indexer;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IndexerResult implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = "$Version$".hashCode();
	
//	private final String token;
	private final String type;
//	private final String field;
	private final String id;

	IndexerResult(String type, 
//				  String field, 
				  String id) {
//		this.token=name;
		this.type=type;
//		this.field=field;
		this.id=id;
	}

//	public String getToken() {
//		return token;
//	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

//	public String getField() {
//		return field;
//	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.SIMPLE_STYLE);		
	}

}


