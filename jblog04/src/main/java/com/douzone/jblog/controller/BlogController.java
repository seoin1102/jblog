package com.douzone.jblog.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.security.Auth;
import com.douzone.jblog.security.AuthUser;
import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;
import com.douzone.jblog.vo.UserVo;
import com.douzone.jblog.service.FileUploadService;
import com.douzone.jblog.service.PostService;
import com.douzone.jblog.service.UserService;

@Controller
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {
	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private BlogService blogService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;
	
	@RequestMapping({"", "/{pathNo1}", "/{pathNo1}/{pathNo2}"})
	public String index(
		@PathVariable("id") String id,
		@PathVariable("pathNo1") Optional<Long> pathNo1,
		@PathVariable("pathNo2") Optional<Long> pathNo2,
		Model model,
		PostVo postVo) {
		
		Long categoryNo = 0L;
		Long postNo = 0L;
		List<CategoryVo> list = categoryService.getCategory(id);
		List<PostVo> list2 = postService.getPostList(id);
		BlogVo blogVo = blogService.getContents(id);
		
		List<String>userList = userService.getAllUser();
	    if(!userList.contains(id)) {
	         return "redirect:/";
	      }
		
		if(list2.isEmpty()) {
			postVo.setTitle("");
			postVo.setContents("");
			list2.add(postVo);
		}
		
		if(pathNo2.isPresent()) {
			categoryNo = pathNo1.get();
			postNo = pathNo2.get();
			postVo = postService.getPost(postNo, id);
			list2 = postService.getCategoryPost(categoryNo, id);
		} else if(pathNo1.isPresent()) {
			categoryNo = pathNo1.get();
			list2 = postService.getCategoryPost(categoryNo, id);
			postVo = list2.get(0);
		}else {
			postVo = list2.get(0);
		}
	
		model.addAttribute("list", list);
		model.addAttribute("postVoList", list2);
		model.addAttribute("postVo", postVo);
		model.addAttribute("blogVo", blogVo);
		
		return "blog/main";
	}
	
	@Auth
	@RequestMapping("/admin/basic")
	public String adminBasic(
			@PathVariable("id") String id, 
			@AuthUser UserVo authUser,
			Model model) {
		if(!authUser.getId().equals(id)) {
			return "redirect:/";
		}
		BlogVo blogVo = blogService.getContents(id);
		model.addAttribute("blogVo", blogVo);
		return "blog/admin/basic";
	}
	
	@Auth
	@RequestMapping(value = "/admin/basic", method = RequestMethod.POST)
	public String adminBasic(@AuthUser UserVo authUser, @RequestParam(value="title", required=true, defaultValue="") String title, @RequestParam(value="logo-file",required = false ) MultipartFile multipartFile, BlogVo blogVo) {
		String url = fileUploadService.restoreImage(multipartFile);
		blogVo.setId(authUser.getId());
		blogVo.setTitle(title);
		blogVo.setLogo(url);
		blogService.updateBlog(blogVo);
		blogVo.setTitle(blogVo.getTitle());
		blogVo.setLogo(blogVo.getLogo());
		
		return "blog/admin/basic";
		}
	
	@Auth
	@RequestMapping("/admin/category")
	public String adminCategory(
			@PathVariable("id") String id, 
			@AuthUser UserVo authUser,
			Model model) {
		if(!authUser.getId().equals(id)) {
			return "redirect:/";
		}
		List<CategoryVo> list = categoryService.getCategory(id);
		BlogVo blogVo = blogService.getContents(id);
		model.addAttribute("list", list);
		model.addAttribute("blogVo", blogVo);
		return "blog/admin/category";
	}
	
	@Auth
	@RequestMapping(value="/admin/category", method = RequestMethod.POST)
	public String adminCategory(
			@PathVariable("id") String id, 
			@AuthUser UserVo authUser,
			@RequestParam(value="name", required=true, defaultValue="") String name, 
			@RequestParam(value="desc", required=true, defaultValue="") String desc, 
			Model model) {

		if(!authUser.getId().equals(id)) {
			return "redirect:/";
		}
		categoryService.addCategory(id, name, desc);
		List<CategoryVo> list = categoryService.getCategory(id);
		BlogVo blogVo = blogService.getContents(id);
		model.addAttribute("list", list);
		model.addAttribute("blogVo", blogVo);
		return "blog/admin/category";
	}
	
	@Auth
	@RequestMapping("/admin/category/delete/{no}")
	public String deleteCategory(@AuthUser UserVo authUser, @PathVariable("no") Long categoryNo, Model model, CategoryVo categoryVo) {
		categoryVo.setNo(categoryNo);
		categoryVo.setBlogId(authUser.getId());
		categoryService.deleteCategory(categoryVo);

		List<CategoryVo> list = categoryService.getCategory(authUser.getId());
		BlogVo blogVo = blogService.getContents(authUser.getId());
		model.addAttribute("list", list);
		model.addAttribute("blogVo", blogVo);
		return "blog/admin/category";
	}
	
	@Auth
	@RequestMapping("/admin/write")
	public String write(
			@PathVariable("id") String id, 
			@AuthUser UserVo authUser,
			Model model) {
		if(!authUser.getId().equals(id)) {
			return "redirect:/";
		}
		List<CategoryVo> list = categoryService.getCategory(id);
		BlogVo blogVo = blogService.getContents(id);
		model.addAttribute("list", list);
		model.addAttribute("blogVo", blogVo);
		return "blog/admin/write";
	}
	
	@Auth
	@RequestMapping(value="/admin/write", method = RequestMethod.POST)
	public String write(
			@PathVariable("id") String id, 
			@RequestParam(value="category", required=true, defaultValue="") String category, 
			@AuthUser UserVo authUser,
			Model model,
			@ModelAttribute @Valid PostVo postVo,
			BindingResult result) {
		BlogVo blogVo = blogService.getContents(id);
		model.addAttribute("blogVo", blogVo);
		if(!authUser.getId().equals(id)) {
			return "redirect:/";
		}
		if(result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			List<CategoryVo> list = categoryService.getCategory(id);
			model.addAttribute("list", list);
			return "blog/admin/write";
		}
		Long no = categoryService.getCategoryNo(id, category);
		postVo.setNo(no);
		postVo.setTitle(postVo.getTitle());
		postVo.setContents(postVo.getContents());

		postService.addPost(postVo);
		postVo.setNo(postVo.getNo());
		postVo.setTitle(postVo.getTitle());
		postVo.setContents(postVo.getContents());
		List<CategoryVo> list = categoryService.getCategory(id);
		model.addAttribute("list", list);
	
		return "blog/admin/write";
	}
}