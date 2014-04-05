package edu.gatech.hci.foodnavigator.model;

import android.graphics.Bitmap;

/* A model object for food */
public class Food {

	private long id = -1;
	private String localFoodName = "";
	private String localPronun = "";
	private String localDescription = "";
	private String country_name = "";
	private String userFoodName = "";
	private String userDescription = "";
	private Bitmap picture = null;
	private Boolean favorite = false;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLocalFoodName() {
		return localFoodName;
	}
	public void setLocalFoodName(String localFoodName) {
		this.localFoodName = localFoodName;
	}
	public String getLocalPronun() {
		return localPronun;
	}
	public void setLocalPronun(String localPronun) {
		this.localPronun = localPronun;
	}
	public String getLocalDescription() {
		return localDescription;
	}
	public void setLocalDescription(String localDescription) {
		this.localDescription = localDescription;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getUserFoodName() {
		return userFoodName;
	}
	public void setUserFoodName(String userFoodName) {
		this.userFoodName = userFoodName;
	}
	public String getUserDescription() {
		return userDescription;
	}
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}
	public Bitmap getPicture() {
		return picture;
	}
	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}
	public Boolean getFavorite() {
		return favorite;
	}
	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}


}
