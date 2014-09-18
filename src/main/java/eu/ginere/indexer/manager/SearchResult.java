package eu.ginere.indexer.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * This is represents the result of a search
 * 
 * @author ginere
 *
 */
public class SearchResult {

	public static final List<Object> EMPTY_OBJECT_LIST=new ArrayList<Object>(0);;
	public static final SearchResult EMPTY_RESULT=new SearchResult();
	
	public final List<Object> objectList;

	public final int pageNumber;
	public final int pageSize;
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
}
