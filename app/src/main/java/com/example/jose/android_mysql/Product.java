package com.example.jose.android_mysql;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jose on 16/2/2016.
 */
public class Product implements Serializable {

	@SerializedName("pid")
	public int pid;

	@SerializedName("name")
	public String name;

	@SerializedName("qty")
	public int qty;

	@SerializedName("price")
	public double price;

	@SerializedName("image_url")
	public String image_url;
}
