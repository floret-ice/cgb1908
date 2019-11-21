package com.cy.pj.common.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class PageObject<T> implements Serializable{ //泛型类

	private static final long serialVersionUID = -8771602734404049678L;
	
	/**总行数(通过查询获得)*/
	private Integer rowCount;
	/**当前页记录*/
	private List<T> records;
	/**总页数(通过计算获得)*/
	private Integer pageCount;	
	/**当前页的页码值*/
	private Integer pageCurrent;	
	/**页面大小(每页最多显示多少条记录)*/
	private Integer pageSize;
	public PageObject(Integer rowCount, List<T> records, Integer pageCurrent, Integer pageSize) {
		super();
		this.rowCount = rowCount;
		this.records = records;		
		this.pageCurrent = pageCurrent;
		this.pageSize = pageSize;
//		this.pageCount=rowCount/pageSize;
//		if(rowCount%pageSize!=0) {
//			this.pageCount++;
//		}
		//计算总页数
		this.pageCount = (rowCount-1)/pageSize+1;
	}
	
	
	
	
	
}	
