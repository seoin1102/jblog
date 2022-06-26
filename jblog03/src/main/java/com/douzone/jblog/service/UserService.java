package com.douzone.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.repository.UserRepository;
import com.douzone.jblog.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BlogRepository blogRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	public void join(UserVo vo) {
		userRepository.insert(vo);
		blogRepository.insert(vo.getId(), vo.getName());
		categoryRepository.insert(vo.getId());
	}

	public UserVo getUser(String id, String password) {
		UserVo vo = new UserVo();
		vo.setId(id);
		vo.setPassword(password);
		
		return getUser(vo);
	}
	
	public UserVo getUser(UserVo vo) {
		return userRepository.findByIdAndPassword(vo);
	}

	public UserVo getUser(Long no) {
		return userRepository.findByNo(no);
	}
	
	public List<String> getAllUser() {
		return userRepository.findUser();
	}

	public void updateUser(UserVo vo) {
		userRepository.update(vo);
	}
}