package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;
@Repository
public class CategoryRepository {
	@Autowired
	private SqlSession sqlSession;
	
	public boolean insert(String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		return sqlSession.insert("category.insert", map) == 1;
	}
	
	public boolean insert(String id, String name, String desc) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("name", name);
		map.put("desc", desc); 
		return sqlSession.insert("category.insert", map) == 1;
	}

	public List<CategoryVo> findById(String id) {
		return sqlSession.selectList("category.findById",id);

	}

	public boolean delete(CategoryVo categoryVo) {

		return sqlSession.delete("category.delete",categoryVo) == 1;

	}

	public Long findByName(String id, String name) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("name", name);
		return sqlSession.selectOne("category.findByName", map);
	}


}
