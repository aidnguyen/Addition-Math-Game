/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kidmathgame;

/**
 *
 * @author Ai
 */

public class User implements Comparable<User> {
	String name;
	int score;
	
        public User(int score){
            this.name = "";
            this.score = score;
        }
	public User(String name, int score){
		this.name = name;
		this.score = score;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public int getScore(){
		return score;
	}
	public String getName() {
		return name;
	}
	public int compareTo(User o){
		if (o instanceof User) {
			User otherUser = (User) o; 
			if (otherUser.getScore() > this.getScore())
				return 1;
			else if (otherUser.getScore() < this.getScore())
				return -1;
			else
				return 0;
		}
	return 0;
	}
}

