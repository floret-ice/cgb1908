package com.cy.pj.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.sys.dao.EditorDao;
import com.cy.pj.sys.service.EditorService;

@Service 
public class EditorServiceImpl implements EditorService {

	@Autowired
	private EditorDao editorDao;

	@Override
	public int saveEditor(String content) {
	
		if(content==null||content=="") throw new IllegalArgumentException("请输入文本内容");
		 
		int row = editorDao.insertEdit(content);
		return row;
	}

}
