package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.douzone.jblog.vo.BlogVo;

@Repository
public class BlogRepository {
	@Autowired
	private SqlSession sqlSession;
	
	public boolean insert(String id, String name) {
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name+"님의 블로그");
		return sqlSession.insert("blog.insert", map) == 1;
		
	}

	public BlogVo findById(String id) {
		BlogVo vo = sqlSession.selectOne("blog.findById", id);
		return vo;
	}

	public boolean update(BlogVo vo) {
		return sqlSession.update("blog.update", vo) == 1;
		
	}
}
