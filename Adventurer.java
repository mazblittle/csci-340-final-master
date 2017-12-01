package com.csci340.mazjoeproject;

public class Adventurer {
	
	//initialize variables
	public static String name;
	public static int health;
	public static int ultimateCharge=0;
	
	/* class builder */
	
	//default
	public Adventurer(){
		Adventurer.name = "Bread";
	}
	//with specification
	public Adventurer(String name){
		Adventurer.name=name;
	}
	
	//health string builder
	public static String healthBuilder(){
		String healthString; 
		healthString = name + "'s health: [ ";
		for (int i = 0; i < Adventurer.health; i++){
			healthString += "<3 ";
		}
		if(Adventurer.health < 30){
			for(int j = 0; j < (30-Adventurer.health); j++){
				healthString += "-x ";
			}
		}
		
		healthString += "]";
		
		return healthString;
	
	}
	
	public static String ultBuilder(){
		String ultString; 
		ultString = "Ultimate attack progress | ";
		for (int i = 0; i < Adventurer.ultimateCharge; i++){
			ultString += "[o]";
		}
		if(Adventurer.ultimateCharge < 10){
			for(int j = 0; j < (10-Adventurer.ultimateCharge); j++){
				ultString += "[ ]";
			}
		}
		
		
		return ultString;
	
	}

}
