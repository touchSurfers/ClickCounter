package com.helpers;


public class user_item {
	private String id;
	private String address="";
	private String date;
	private String lat;
	private String longi;
	private String timestamp;
	
	
	public user_item(String Id,String address,String date,String lat,String longi,String timestamp) {
		this.id = Id;
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
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
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
