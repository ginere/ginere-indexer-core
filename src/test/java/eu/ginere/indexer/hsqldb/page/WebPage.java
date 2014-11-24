package eu.ginere.indexer.hsqldb.page;

import java.sql.ResultSet;
import java.util.Date;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.descriptor.annotation.Description;
import eu.ginere.hsqldb.dao.KeyDTO;

public class WebPage extends KeyDTO{
    
    public static final String TABLE_NAME = "WEB_PAGE";
	public static final String ID = "URL";

    public static final String COLUMNS_ARRAY_MINUS_KEY_COLUMN[] = {
        "TITLE",
        "CONTENT",
        "LAST_UPDATE"
    };

    @Description
	public final String title;

    @Description
	public final String content;

    @Description
	public final Date lastUpdate;

    protected WebPage(String url,String title,String content){ 
        super(url);
        this.title=title;
        this.content=content;
        this.lastUpdate=new Date();
    }
    
    protected WebPage(String id,ResultSet rset) throws DaoManagerException{ 
		super(id);

        this.title = getString(rset,"TITLE",TABLE_NAME);
        this.content = getString(rset,"CONTENT",TABLE_NAME);
        this.lastUpdate = getDate(rset,"LAST_UPDATE",TABLE_NAME);
    }

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

}
