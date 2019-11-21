package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EditorDao {
	@Insert("insert into t_strategy (content) values(#{content})")
	public int insertEdit(String content);
	
	@Select("select * from t_strategy where id=#{id}")
	public List<String> findEditorById(Integer id);
}
