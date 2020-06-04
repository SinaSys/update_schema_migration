package com.sriyanksiddhartha.realmdemo.model;


import io.realm.RealmObject;

public class Company extends RealmObject {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
