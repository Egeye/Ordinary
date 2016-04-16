package com.octavio.ordinary.model;

public class Diary {
	private String title;
	private String content;
	private int id;
	private String time;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Diary(String title, String content, int id, String time) {
		super();
		this.title = title;
		this.content = content;
		this.id = id;
		this.time = time;
	}
	public Diary() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Diary [title=" + title + ", content=" + content + ", id=" + id + ", time=" + time + "]";
	}
	

}
