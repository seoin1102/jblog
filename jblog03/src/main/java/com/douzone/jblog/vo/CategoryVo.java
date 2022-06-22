package com.douzone.jblog.vo;

public class CategoryVo {
	private Long no;
	private String name;
	private String description;
	private String blogId;
	private int countPost;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBlogId() {
		return blogId;
	}
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	public int getCountPost() {
		return countPost;
	}
	public void setCountPost(int countPost) {
		this.countPost = countPost;
	}
	@Override
	public String toString() {
		return "CategoryVo [no=" + no + ", name=" + name + ", description=" + description + ", blogId=" + blogId
				+ ", countPost=" + countPost + "]";
	}
	
	
	
}