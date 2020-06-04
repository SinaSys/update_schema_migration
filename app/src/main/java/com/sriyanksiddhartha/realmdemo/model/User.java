package com.sriyanksiddhartha.realmdemo.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

	@PrimaryKey
	private String id;
	private String name;
	private int age;
	private String hobby;
	private SocialAccount socialAccount;
	private Company company;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public SocialAccount getSocialAccount() {
		return socialAccount;
	}

	public void setSocialAccount(SocialAccount socialAccount) {
		this.socialAccount = socialAccount;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
