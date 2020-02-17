package com.steadytech.sertronik.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Bounds{

	@SerializedName("southwest")
	private Southwest southwest;

	@SerializedName("northeast")
	private Northeast northeast;

	public void setSouthwest(Southwest southwest){
		this.southwest = southwest;
	}

	public Southwest getSouthwest(){
		return southwest;
	}

	public void setNortheast(Northeast northeast){
		this.northeast = northeast;
	}

	public Northeast getNortheast(){
		return northeast;
	}

	@Override
 	public String toString(){
		return 
			"Bounds{" + 
			"southwest = '" + southwest + '\'' + 
			",northeast = '" + northeast + '\'' + 
			"}";
		}
}