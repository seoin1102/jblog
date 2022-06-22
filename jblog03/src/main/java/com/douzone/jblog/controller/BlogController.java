package com.douzone.jblog.controller;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	@RequestMapping({"", "/{pathNo1}", "/{pathNo1}/{pathNo2}"})
	public String index(
		@PathVariable("id") String id,
		@PathVariable("pathNo1") Optional<Long> pathNo1,
		@PathVariable("pathNo2") Optional<Long> pathNo2,
		Model model,
		PostVo postVo) {
		
		Long categoryNo = 0L;
		Long postNo = 1L;
		
		List<CategoryVo> list = categoryService.getCategory(id);
		BlogVo blogVo = blogService.getContents(id);

		model.addAttribute("list", list);
		
		List<String>userList = userService.getAllUser();
		if(!userList.contains(id)) {
			return "redirect:/user/join";
		}
		
		if(pathNo2.isPresent()) {
			categoryNo = pathNo1.get();
			postNo = pathNo2.get();
			List<PostVo> list2 = postService.getCategoryPost(categoryNo, id);
			postVo = postService.getPost(postNo, id);
		
			if(postVo == null) {
				return "redirect:/"+id;
			}
			model.addAttribute("postVoList", list2);
			model.addAttribute("blogVo", blogVo);

			model.addAttribute("postVo", postVo);
			return "blog/main";
			
		} else if(pathNo1.isPresent()) {
			categoryNo = pathNo1.get();
			List<PostVo> list2 = postService.getCategoryPost(categoryNo, id);
			if(list2.isEmpty()) {
				postVo.setTitle("");
				postVo.setContents("");
			}else {
				postVo = list2.get(0);
			}
			
			model.addAttribute("postVoList", list2);
			System.out.println("postVoList"+list2);
			model.addAttribute("blogVo", blogVo);

			model.addAttribute("postVo", postVo);
			return "blog/main";
		}
		List<PostVo> list2 = postService.getPostList(id);
		

		if(list2.isEmpty()) {
			postVo.setTitle("");
			postVo.setContents("");
		}else {
			postVo = list2.get(0);
		}
		model.addAttribute("postVo", postVo);
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("postVoList", list2);
		
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
		model.addAttribute("list", list);
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
		model.addAttribute("list", list);
		return "blog/admin/category";
	}
	
	@Auth
	@RequestMapping("/admin/category/delete/{no}")
	public String deleteCategory(@AuthUser UserVo authUser, @PathVariable("no") Long categoryNo, Model model, CategoryVo categoryVo) {
		categoryVo.setNo(categoryNo);
		categoryVo.setBlogId(authUser.getId());
		categoryService.deleteCategory(categoryVo);

		List<CategoryVo> list = categoryService.getCategory(authUser.getId());
		model.addAttribute("list", list);
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
		model.addAttribute("list", list);
		return "blog/admin/write";
	}
	
	@Auth
	@RequestMapping(value="/admin/write", method = RequestMethod.POST)
	public String write(
			@PathVariable("id") String id, 
			@AuthUser UserVo authUser,
			@RequestParam(value="title", required=true, defaultValue="") String title,
			@RequestParam(value="content", required=true, defaultValue="") String content,
			@RequestParam(value="category", required=true, defaultValue="") String categoryName,
			Model model,
			PostVo postVo) {
		if(!authUser.getId().equals(id)) {
			return "redirect:/";
		}
		Long no = categoryService.getCategoryNo(id, categoryName);
		System.out.println(no);
		postVo.setNo(no);
		postVo.setTitle(title);
		postVo.setContents(content);
		postService.addPost(postVo);
		List<CategoryVo> list = categoryService.getCategory(id);
		model.addAttribute("list", list);
		return "blog/admin/write";
	}
}