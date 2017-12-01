package com.csci340.mazjoeproject;

import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.lang.String;



public class Main {
	/******************************************
	 *            INITIALIZATIONS             *
	 ******************************************/
	//initialize semaphores
	public static Semaphore sem = new Semaphore(1);
	static Scanner input = new Scanner(System.in);
	
	//variable to track turn number in rotation
	public static int whoseTurn;
	
	
	public static void main(String[] args) {
		double rand;
		/************************************************
		 *           GAME DIFFICULTY SETTINGS           *
		 ************************************************/
		String reader;	//line reader
		int	startHeads = 3; //starting heads
		int ultCharge = 10;	//ultimate charger
		int ultImpact = 10;	//ultimate impact
		int maxHydra = 40;	//maximum number of hydra heads
		
		System.out.println("Customize Difficulty? (YES/NO)");
		reader = (input.nextLine()).toUpperCase();
		//difficulty levels: normal or difficult (or insane)
		//if no, set difficulty to normal
		if(reader.equals("NO")) {
			System.out.println("Difficulty set to: NORMAL");
		} else if (reader.equals("YES")) {
		//if yes, ask normal (n) or hard (h)
			System.out.println("NORMAL or HARD?");
			reader = (input.nextLine()).toUpperCase();
		//difficulty levels: normal or difficult (or insane)
			if(reader.equals("NORMAL")) {
				//NORMAL
				System.out.println("Difficulty set to: NORMAL");
				startHeads = 3;
				//ultCharge = 3;
				ultImpact = 10;
				maxHydra = 40;
			} else if (reader.equals("HARD")) {
				//HARD
				System.out.println("Difficulty set to: HARD");
				startHeads = 5;
				//ultCharge = 5;
				ultImpact = 7;
				maxHydra = 100;
			}
		}
		
		//then change all hard-coded numbers to variables
		whoseTurn=0;
	
		//get name as input
		System.out.println("Enter name!");
		
		//initialize adventurer status and output 
		Adventurer.health=30;
		Adventurer.name = input.nextLine();
		System.out.println("Behold! The bravest warrior of their clan! Their name be mighty, and their name be " + Adventurer.name + "!");
		
		//initialize headCount
		HydraHead.headCount = 0;
		
		//initialize hydra array
		HydraHead[] hydra= new HydraHead[maxHydra];
		
		//fill array with hydra instances
		for(int i = 0; i < hydra.length; ++i){
			hydra[i] = new HydraHead(i);
		}
		
		//inside a semaphore lock, start hydra process, then wait
		for (int i = 1; i <= startHeads; ++i){
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			hydra[HydraHead.headCount].start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				sem.release();
		}
		
		/*******************************************************
		*                   MAIN GAME LOOP                     *
		*******************************************************/
		//while the player and hydra are both still alive:
		while(Adventurer.health>0 && HydraHead.livingHeads>0){
			for(int m = 0; m < HydraHead.headCount; m++) {
				System.out.println(hydra[m].healthBuilder());
			}
			System.out.println("  (|/|/|/|/|/|)================>>");
			System.out.println(" ||  ||  ||  ||");
			System.out.println(" ww  ww  ww  ww");
			//wait a bit
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//output heads still alive at beginning of turn
			System.out.printf("%d heads still alive!\n", HydraHead.livingHeads);
			
			//iterate through initiative for all hydra heads and player 
			for(whoseTurn = 0; whoseTurn < (HydraHead.headCount+1); ++whoseTurn){
				//wait a bit
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//print the turn number
				//System.out.printf("Turn %d\n", whoseTurn);
				try {
					sem.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//death message
				if(Adventurer.health<=0){
					System.out.println("You have died!");
					//exit the loop bc you died
					break;
					
				}
				
				//after iterating through all hydra head attacks, the player attacks
				if(whoseTurn == HydraHead.headCount){
					//Adventurer's turn
					//wait a bit
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//build output message for player attack options
					String availableheads="You may attack: ";
					int aliveCount=0;
					for(int i = 0; i < HydraHead.headCount; ++i){
						if (hydra[i].alive){
							availableheads+=i;
							availableheads+=" ";
							aliveCount++;
						}
						
					}
					availableheads += "with your sword";
					//if the hydra is killed, skip down to the endgame logic
					if (aliveCount==0)break;
					
					//if the adventurer has charged their ultimate weapon, add the option to use it to the display string
					if (Adventurer.ultimateCharge>=ultCharge){
						availableheads+=", or enter 100 to activate ultimate attack!";
					} else {
						availableheads+= "!";
					}
					
					//print the display string 
					System.out.println(Adventurer.ultBuilder());
					System.out.println(availableheads);
					
					//begin player response logic handling
					System.out.println("Which head to attack?");
					for(int i = 0; i < HydraHead.headCount; ++i){
						
					}
					//player will enter the number of the head they'd like to attack next
					int attack = input.nextInt();
					rand = (Math.random())*100;
					if(rand < 10) {
						Adventurer.ultimateCharge += 3;
						}
					//determine if ultimate is used and execute ultimate attack
					if (attack==100 && Adventurer.ultimateCharge>=ultCharge ){
						//reset ultimate weapon charge
						Adventurer.ultimateCharge=0;
						System.out.println("Using ultimate!");
						int killed=0;
						//for ALL the heads, if it's still alive, kill the first ten heads
						for(int i = 0; i < HydraHead.headCount; ++i){
							//exit loop it the ultimate attack kills the hydra
							if(killed==10 || HydraHead.livingHeads<=0) break;
							if (hydra[i].alive) {
								hydra[i].alive=false;
								System.out.printf("Head %d killed!\n", i);
								++killed;
								HydraHead.livingHeads--;
							}
						}
					}
					//regular attack
					else{
						//can't attack a head not in the range of heads present
						while (attack>=HydraHead.headCount){
							System.out.println("That's not a valid head! Try again.");
							attack = input.nextInt();
						}
						//can't attack a dead head
						if (!hydra[attack].alive){
							System.out.println("That one's already dead! Try again!");
							attack = input.nextInt();
						}
						//attacked hydra takes damage
						hydra[attack].takeDamage();
						//output remaining health for head
						System.out.printf("Head %d has %d health!\n", attack, hydra[attack].health);
						//when a hydra head is killed, output that it's died, then spawn two more heads
						if(hydra[attack].health<=0){
							whoseTurn++;
							HydraHead.livingHeads--;
							System.out.printf("Head %d killed!\n", attack);
							hydra[attack].stop();
							hydra[attack].alive=false;
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//HydraHead.headCount++;
							hydra[HydraHead.headCount].start();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//HydraHead.headCount++;					
							hydra[HydraHead.headCount].start();
							Adventurer.ultimateCharge+=2;
							if(rand < 1) {
								Adventurer.ultimateCharge += 5;
								}
						}
					
					}
				
				
				
			}
			
				sem.release();
		}
			
		}
		//endgame logic 
		if(Adventurer.health>0){
		System.out.println("You have defeated the Hydra!");
		}

	}
	

	
	public static void attackAdvent(Adventurer advent){
		advent.health--;
	}

}
