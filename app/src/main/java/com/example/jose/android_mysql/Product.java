package com.example.jose.android_mysql;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jose on 16/2/2016.
 */
public class Product {

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
