/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kidmathgame;
import java.util.Random;
/**
 *
 * @author Ai
 */
import javax.swing.*;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import static kidmathgame.GameFrame.*;

public class ProcessingData {
    
    static int score = 0; // count the number of right answers
    public static Timer timer; // count time when playing
    private static Vector<User> userList = new Vector();
    private static Map<Integer, JTextField> nameList = new HashMap<>();
    private static Map<Integer, JTextField> scoreList = new HashMap<>();
    
    //Method to read file , we don't need it when run as jar file
    private static void readFile(String filename, Vector<User> list) {
        String path = "/kidmathgame/" + filename;
        InputStream input = ProcessingData.class.getResourceAsStream(path);
        try{
            BufferedReader in = new BufferedReader( //Create input file object
            new InputStreamReader(input));
            String line = in.readLine(); //Read in a line from file
            int i = 0;//Not in EOF
            while (line != null) {
                if (filename.equals("highscore.txt"))
                    userList.add(new User(Integer.parseInt(line))); //Display the line
                else if (filename.equals("name.txt"))
                   userList.get(i).setName(line);
            line = in.readLine(); //Continue to read in
            i++;
            }
            in.close(); //Close input file
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null,e);
            } 
    
    }
    
    //this method cannot work with -JAR file
    public static void writeFile(String filename){
        String path = "/kidmathgame/" + filename;
        InputStream input = ProcessingData.class.getResourceAsStream(path);
        try {
            File myFile = new File(ProcessingData.class.getResource(path).toURI());
            //Create output as append with buffer
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(myFile)));
            for(int i = 1; i <= userList.size(); i++){
               if (filename.equals("highscore.txt"))
                   out.println(scoreList.get(i).getText());
               else if (filename.equals("name.txt"))
                   out.println(nameList.get(i).getText());
            }
            out.close();
	}
        catch (IOException e) {
            System.out.println(e);
	} 
        catch (URISyntaxException ex) {
            Logger.getLogger(ProcessingData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Create user high score list
    public static void createScoreList(){
        nameList.put(1, name1);
        nameList.put(2, name2);
        nameList.put(3, name3);
        nameList.put(4, name4);
        nameList.put(5, name5);
        scoreList.put(1, score1);
        scoreList.put(2, score2);
        scoreList.put(3, score3);
        scoreList.put(4, score4);
        scoreList.put(5, score5);
        
        //readFile("highscore.txt", userList);
        if(userList.isEmpty())
           userList.add(new User("",0));
        //else
        //    readFile("name.txt", userList);
    }
     
    //Method to change/display [new] questions 
    public static void changeQuestion(){
        //Generate 2 random number from 0 to 49
        Random randomGenerator = new Random();
        int randomInt1 = randomGenerator.nextInt(50);
        int randomInt2 = randomGenerator.nextInt(50);
        String num1Value = Integer.toString(randomInt1);
        String num2Value = Integer.toString(randomInt2);
        num1.setText(num1Value);
        num2.setText(num2Value);
        answerField.setText(null);
    }
    //Method to play sound when answer right or wrong
    public static void playSound(String fileName){
        String path = "/kidmathgame/audio/" + fileName;
        URL url = ProcessingData.class.getResource(path);
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,e.getLocalizedMessage());
        }  
    }
    
    //Method to count time and add new high score if user get it
    public static void countTime(){
        timer = new Timer(1000, new ActionListener() {
            int counter = timeBar.getMaximum();
            public void actionPerformed(ActionEvent e) {
                counter--;
                timeBar.setValue(counter);
                timeBar.setString(Integer.toString(counter) + "s");
                if (counter < 1) {
                    answerField.setEditable(false);
                    enterButton.setVisible(false);
                    skipButton.setVisible(false);
                    timeBar.setVisible(false);
                    timeLabel.setVisible(false);
                    ((Timer)e.getSource()).stop();
                    User newUser = new User(userName.getText(),Integer.parseInt(scoreField.getText()));
                    verifyHighScore(newUser);
                }
            }
        });
        timer.start();
    }
    
    //Method to add new user to high score list if score of new user is
    //greater than all elements in userList
    private static void verifyHighScore(User newUser){
        boolean isGreater =false; //for stop adding when already find an old score less than new score
        Collections.sort(userList); 
        
        if(userList.size() < 5){
            for(int i =0; i < userList.size(); i++){
                if (newUser.getScore() > 0) 
                        isGreater = true;
            }
                if(isGreater){
                    userList.add(newUser);
                    congratMessage.setVisible(true);
                }
        }
        else{
            for(int i =0; i < userList.size(); i++){
                if (newUser.getScore() > userList.get(i).getScore()) 
                    isGreater = true;
            }
            if(isGreater){
                userList.remove(userList.get(0));
                userList.add(newUser);
                congratMessage.setVisible(true);
            }
        }

         // sort userList in ascending order first
	Collections.sort(userList);
    }
    
    public static void displayHighScore(){
        int i = 1;
        for (User user : userList){
            nameList.get(i).setText(user.getName());
            scoreList.get(i).setText(Integer.toString(user.getScore()));
            i++;
        }
    }
}
