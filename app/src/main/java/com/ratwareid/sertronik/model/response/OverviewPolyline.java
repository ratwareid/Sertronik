package com.ratwareid.sertronik.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OverviewPolyline{

	@SerializedName("points")
	private String points;

	public void setPoints(String points){
		this.points = points;
	}

	public String getPoints(){
		return points;
	}

	@Override
 	public String toString(){
		return 
			"OverviewPolyline{" + 
			"points = '" + points + '\'' + 
			"}";
		}
}