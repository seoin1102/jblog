package com.douzone.jblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.vo.CategoryVo;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	public List<CategoryVo> getCategory(String id) {
		return categoryRepository.findById(id);
	}
	public boolean addCategory(String id, String name, String desc) {
		return categoryRepository.insert(id, name, desc);
	}
	public boolean deleteCategory(CategoryVo categoryVo) {
		return categoryRepository.delete(categoryVo);
		
	}
	
	public Long getCategoryNo(String id, String categoryName) {
		return categoryRepository.findByName(id, categoryName);
		
	}



}
