package eu.ginere.indexer.indexer;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

	public static final List<Object> EMPTY_OBJECT_LIST=new ArrayList<Object>(0);;
	public final List<Object> objectList;

	public final int page;
	public final int pageSize;
	public final int total;

	public long laps;

	
	public static final SearchResult EMPTY_RESULT=new SearchResult();
	
	private SearchResult() {

		this.objectList=EMPTY_OBJECT_LIST;

		this.page=0;
		this.pageSize=0;
		this.total=0;
		
		this.laps=0;
	}

	public SearchResult(List<Object> objectList, 
						int page, 
						int pageSize,
						int total) {

		this.objectList=objectList;

		this.page=page;
		this.pageSize=pageSize;
		this.total=total;
//		
//		this.laps=laps;
	}

	public List<Object> getObjectList() {
		return objectList;
	}

	public int getPage() {
		return page;
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
