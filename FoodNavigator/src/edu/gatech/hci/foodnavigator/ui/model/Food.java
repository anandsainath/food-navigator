package edu.gatech.hci.foodnavigator.ui.model;

import android.graphics.Bitmap;

/* A model object for food */
public class Food {

	private long id = -1;
	private String enName = "";
	private String enPronunciation = "";
	private String country_name = "";
	private String localName = "";
	private String localDescription = "";
	private Bitmap picture = null;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getEnPronunciation() {
		return enPronunciation;
	}

	public void setEnPronunciation(String enPronunciation) {
		this.enPronunciation = enPronunciation;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getLocalDescription() {
		return localDescription;
	}

	public void setLocalDescription(String localDescription) {
		this.localDescription = localDescription;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

}
