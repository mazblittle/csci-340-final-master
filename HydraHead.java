package com.csci340.mazjoeproject;

import java.util.concurrent.Semaphore;
import java.lang.String;

public class HydraHead extends Thread{
	
	public static int headCount;
	public static int livingHeads;
	public int id;
	
	public int health;
	
	public boolean alive;
	
	public HydraHead(int id){
	this.id=id;	
	}
	
	public void run(){
		this.alive=true;
		livingHeads++;
		//hydra heads print
		System.out.printf("A new Hydra head Emerges! The hydra has %d heads!\n", livingHeads);
		headCount++;
		
		this.health = 10;
		
		while (this.health>0 && alive){
			
			if (Main.whoseTurn==this.id){
				
				try {
					Main.sem.acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Main.sem.release();
				attack();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		livingHeads--;
		
		
		
		}
	
	
	public void attack(){
		System.out.println("Head " + this.id + " snaps its fearsome jaws at the adventurer!");
		Adventurer.health--;
		String currentHealth = Adventurer.healthBuilder();
		System.out.println(currentHealth);
		
	}
	
	public void takeDamage(){
		this.health-=5;
	}
	
	//health string builder
		public String healthBuilder(){
			String healthString; 
			healthString = "(^ov" + (this.id%10) + "vo^) hydra head #" + this.id +  " | "; 
			for (int i = 0; i < this.health; i++){
				healthString += "=";
			}
			if(this.health < 10){
				for(int j = 0; j < (10-this.health); j++){
					healthString += ".";
				}
			}
			
			healthString += " ]";
			
			return healthString;
		
		}
	
	

}
