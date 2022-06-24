package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;
@Repository
public class PostRepository {
	@Autowired
	private SqlSession sqlSession;
	public boolean addPost(PostVo postVo) { 
		return sqlSession.insert("post.insert", postVo) == 1;
	}
	public List<PostVo> findCategoryPost(Long categoryNo, String blogId) {
		Map<String, Object> map = new HashMap<>();
		map.put("categoryNo", categoryNo);
		map.put("blogId", blogId);
		return sqlSession.selectList("post.selectCategoryPost", map);
	}
	public List<PostVo> findAllPost(String blogId) {
		return sqlSession.selectList("post.selectAll", blogId);
	}
	public PostVo findPost(Long postNo, String blogId) {
		Map<String, Object> map = new HashMap<>();
		map.put("no", postNo);
		map.put("blogId", blogId);
		return sqlSession.selectOne("post.select", map);
	}

}
