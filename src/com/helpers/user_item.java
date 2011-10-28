package com.helpers;


public class user_item {
	private String id;
	private String notes;
	private String photo1="";
	private String photo2="";
	private String photo3="";
	private String rating="";
	private String address="";
	private String date;
	private String lat;
	private String longi;
	private String timestamp;
	
	
	public user_item(String Id, String notes, String photo1, String photo2, String photo3,String rating, String address,String date,String lat,String longi,String timestamp) {
		this.id = Id;
		this.notes = notes;
		this.photo1 = photo1;
		this.photo2 = photo2;
		this.photo3 = photo3;
		this.rating = rating;
		this.address = address;
		this.date = date;
		this.lat = lat;
		this.longi = longi;
		this.timestamp = timestamp;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getNotes() {
		return notes;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getRating() {
		return rating;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	
	public void setPhoto(String photo, int num) {
		
		if(num == 1)
			this.photo1 = photo;
		if(num == 2)
			this.photo2 = photo;
		if(num == 3)
			this.photo3 = photo;
		
	}
	public  String getPhoto(int num) {
		if(num == 1)
			return this.photo1;
		if(num == 2)
			return this.photo2;
		if(num == 3)
			return this.photo3;
		
		return "";
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLat() {
		return lat;
	}
	
	public void setLong(String longi) {
		this.longi = longi;
	}
	public String getLong() {
		return longi;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTimestamp() {
		return timestamp;
	}
	
}
