package com.douzone.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.repository.PostRepository;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

	public boolean addPost(PostVo postVo) {
		return postRepository.addPost(postVo);
		
	}

	public List<PostVo> getCategoryPost(Long categoryNo, String blogId) {
		return postRepository.findCategoryPost(categoryNo, blogId);
	}
	
	public List<PostVo> getPostList(String blogId) {
		return postRepository.findAllPost(blogId);
	}

	public PostVo getPost(Long postNo, String blogId) {
		
		return postRepository.findPost(postNo, blogId);
	}
	
}
