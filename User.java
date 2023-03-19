import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.Serializable;
public class User implements Serializable {

	String username;
	String bio;
	File profileFile;
	File timelineFile;
	ArrayList<User> followerList = new ArrayList<User>();
	ArrayList<User> followList = new ArrayList<User>();
	ArrayList<Message> tweets = new ArrayList<Message>();
	
	public User(String username, String bio, File profiles, File timelines) {
		this.username = username;
		this.bio = bio;
		System.out.println(this.username + " has been created!");
		this.profileFile = new File(profiles + "/" + this.username + ".txt");
		this.timelineFile = new File(timelines + "/" + this.username + ".txt");
		
		followList.trimToSize();
		followerList.trimToSize();
		tweets.trimToSize();
		
		PrintWriter out = null;
		
		try {
		    out = new PrintWriter(new FileOutputStream(profileFile));
			out.println("User Name: " + this.username);
			out.println("Bio: " + this.bio);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
		}
		
		
		try {
		    out = new PrintWriter(new FileOutputStream(timelineFile));
			out.println(" ");
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
		}
		
		
		
		
		
	}
	
//-------------------------------------------------------------------------------------------
	
	public void follow(User user1, User user2) {
		user2.followerList.add(user1);
		user2.followerList.trimToSize();
		user1.followList.add(user2);
		user1.followerList.trimToSize();
	}
	
//-------------------------------------------------------------------------------------------	
	
	public void timelineUpdater() {
				 

	  ArrayList<Message> allMessageCollector = new ArrayList<Message>();
	    

	  for( int i = 0; i < this.followList.size(); i++ ) {
		  followList.get(i).tweetsSorter();
		  	for( int k = 0; k < followList.get(i).tweets.size(); k++ ) {
		  		allMessageCollector.add(followList.get(i).tweets.get(k));
		  		allMessageCollector.trimToSize();
		  	}
		  }
	  

	 for (int i = 0; i < allMessageCollector.size(); i++) {
      int min = i;
      for (int j = i + 1; j < allMessageCollector.size(); j++) {
         if (allMessageCollector.get(j).unformattedDate.after(allMessageCollector.get(min).unformattedDate)) {
            min = j;
         } 
      }
      Message temp = allMessageCollector.get(i);
      allMessageCollector.set(i, allMessageCollector.get(min));
      allMessageCollector.set(min, temp);
   }
	  
  
		try {
			
			String text = "";
			
	        for( int i = 0; i < allMessageCollector.size(); i++ ) {     
	        text += allMessageCollector.get(i).message.substring(0,17) + allMessageCollector.get(i).owner.username + " " + allMessageCollector.get(i).message.substring(17) + "\n";
	        }

	        FileWriter writer = new FileWriter(timelineFile);
	        writer.write(text);
	        writer.close();
	    } catch (IOException e) {
	        
	    }
	
		
	}
	
//-------------------------------------------------------------------------------------------	
	
	public void timelineRemover(User user2) {
		
		   try {
			      String text = "";
			      
			      for(int i=0; i < user2.tweets.size(); i++) {
				    	
				    BufferedReader br = new BufferedReader(new FileReader(timelineFile));
			        String line = "", oldtext = "";
			        while((line = br.readLine()) != null) {
			            oldtext += line + "\n";
			        }
			        br.close();
				    	
			         text  = oldtext.replaceAll(user2.tweets.get(i).message, "");
			        
				        FileWriter writer = new FileWriter(timelineFile);
				        writer.write(text);
				        writer.close();
				    }
			        
			    } catch (IOException e) {
			        
			    }
	        
	}
	
//-------------------------------------------------------------------------------------------	
	
	public void tweetWriter(Message message) {
		this.tweets.add(message);
		this.tweets.trimToSize();
	}
	
//-------------------------------------------------------------------------------------------	
	
	public void profileUpdater() {
		 
		try {    
			        String text2 = "";
		for(int i=0; i < this.tweets.size(); i++) {
		     text2 += this.tweets.get(i).message + "\n";      
			      }         
		       
			   String newStr = "";   
			   
			      newStr = "User Name: " + this.username + "\n" + "Bio: " + this.bio + "\n" + text2;
			      
		        FileWriter writer = new FileWriter(profileFile);
		        writer.write(newStr);
		        writer.close();
		      
		}
		      catch (IOException e) {
		        
		    }
		 	
	}
	
//-------------------------------------------------------------------------------------------	
	
	public void tweetsSorter() {
		
		for (int i = 0; i < tweets.size(); i++) {
		      int min = i;
		      for (int j = i + 1; j < tweets.size(); j++) {
		         if (tweets.get(j).unformattedDate.after(tweets.get(min).unformattedDate)) {		
		            min = j;
		         } 
		      }
		      Message temp = tweets.get(i);
		      tweets.set(i, tweets.get(min));
		      tweets.set(min, temp);
		   }
		
	}
	
//-------------------------------------------------------------------------------------------
	
	public void changedProfileUpdater(String oldMessage, String newMessage) {
		 
		try {
			    	
			    BufferedReader br = new BufferedReader(new FileReader(profileFile));
		        String line = "", oldtext = "";
		        while((line = br.readLine()) != null) {
		            oldtext += line + "\n";
		        }
		        br.close();
			    	
		        String text = oldtext.replaceFirst(oldMessage, newMessage);
		        
		        FileWriter writer = new FileWriter(profileFile);
		        writer.write(text);
		        writer.close();
		    } catch (IOException e) {
		        
		    }
		 	
	}
	
//-------------------------------------------------------------------------------------------	
	
	public void editor(String oldMessage, String newMessage) {
		
		for(int i=0; i < this.tweets.size(); i++) {
			if(this.tweets.get(i).message.equals(oldMessage)) {
				Message subMessage = new Message(this.tweets.get(i).owner, this.tweets.get(i).unformattedDate, newMessage);
				tweets.set(i, subMessage);
				this.tweets.trimToSize();
				System.out.println(tweets.get(i).message);
				break;
			}
		}
		
		
	}
	
	
}