package eu.ginere.indexer.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import eu.ginere.base.util.descriptor.annotation.Description;

/**
 * This is represents the result of a search
 * 
 * @author ginere
 *
 */
public class SearchResult {

	public static final List<Object> EMPTY_OBJECT_LIST=new ArrayList<Object>(0);;
	public static final SearchResult EMPTY_RESULT=new SearchResult();
	
	@Description(description = "The objects")
	public final List<Object> objectList;

	@Description(description = "The page number")
	public final int pageNumber;
	
	@Description(description = "The page size")
	public final int pageSize;
	
	@Description(description = "The total elemnts number")
	public final int total;

	public long laps;
	
	private SearchResult() {

		this.objectList=EMPTY_OBJECT_LIST;

		this.pageNumber=0;
		this.pageSize=0;
		this.total=0;
		
		this.laps=0;
	}

	public SearchResult(List<Object> objectList, 
						int page, 
						int pageSize,
						int total) {

		this.objectList=objectList;

		this.pageNumber=page;
		this.pageSize=pageSize;
		this.total=total;
	}

	public List<Object> getObjectList() {
		return objectList;
	}

	public int getPage() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotal() {
		return total;
	}
	
	public long getLaps() {
		return laps;
	}

	public void setLaps(long laps) {
		this.laps = laps;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
