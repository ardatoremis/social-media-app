import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter; 
public class Main {

    static File profiles = new File("profiles");
    static File timelines = new File("timelines");
    static ArrayList<User> userList = new ArrayList<User>();
    static String input;
	
	
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
	    if (!profiles.exists()) profiles.mkdir();
	    if (!timelines.exists()) timelines.mkdir();	    
	    
		if(args.length == 2) {
			
			String getFolder = args[1];
	
		try {	
			File saving = new File(getFolder);
			ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(saving + "/" + "savelog.bin"));
			
			profiles = (File) objIn.readObject();
			timelines = (File) objIn.readObject();
			userList = (ArrayList<User>) objIn.readObject();
			objIn.close();
			
			int declare1 = 0;
			int declare2 = 0;
			String[] deletingProfiles = profiles.list();
			for(String d1:deletingProfiles) {
			for(int i=0; i < userList.size(); i++) {			
				if( d1.equals(userList.get(i).username) ) {
				declare1++;
					}
				}		
			if( declare1 == 0 ) {
				File insideProfile = new File(profiles,d1);
				insideProfile.delete();
				declare1 = 0;
			}
			}
//----------------------------------------------------------------------------------------------------			
			String[] deletingTimelines = timelines.list();
			for(String d2:deletingTimelines) {
			for(int i=0; i < userList.size(); i++) {			
				if( d2.equals(userList.get(i).username) ) {
				declare2++;
					}
				}		
			if( declare2 == 0 ) {
				File insideTimeline = new File(timelines,d2);
				insideTimeline.delete();
				declare2 = 0;
			}
			}
			
			while(true) {
				System.out.println("Please type your command");
				input = keyboard.nextLine();
				system(input);
			}
			
			
		}
		
		catch(Exception e) {
			
			
		}
		
			
			
		}

//------------------------------------------------------		

		String[] deletingProfiles = profiles.list();
		for(String d1:deletingProfiles) {
			File insideProfile = new File(profiles,d1);
			insideProfile.delete();
		}
		String[] deletingTimelines = timelines.list();
		for(String d2:deletingTimelines) {
			File insideTimeline = new File(timelines,d2);
			insideTimeline.delete();
		}
		
		if(args.length == 1) {
			
			String getFile = args[0];
				
				try {
					File file = new File(getFile);
			        BufferedReader br = new BufferedReader(new FileReader(file));
			        String line = "", oldtext = "";
			        while((line = br.readLine()) != null) {
			            system(line);
			        }
			        br.close();
			      

			    } catch (IOException e) {
			        
			    }
				
				finally {
					System.exit(0);
				}
		
		}
		
	
		System.out.println("Please type your command");
		while(true) {
			input = keyboard.nextLine();
			system(input);
		}
		
		
		
}		
		

		public static void system(String input) {	
			userList.trimToSize();	
			int rowCounter = 0;
			
			
//------------------------------------------------------------------------------------------------------			
			
			if( input.startsWith("Create") )	{

				String getUsername = "";
				String getBio = "";
				
				for(int i=7; i > 0; i++) {
					if(input.charAt(i) == ' ')
						i = -1;
					else getUsername = getUsername + input.charAt(i);	
				}
				
				for(int i=6+getUsername.length()+2; i < input.length(); i++) {
					getBio = getBio + input.charAt(i);
				}
				
				int userAlreadyExistChecker = 0;
				
				for(int i=0; i < userList.size(); i++) {
					if( userList.get(i).username.equals(getUsername) )
						userAlreadyExistChecker++;
				}
				
				if(userList.size() == 0 || userAlreadyExistChecker == 0) {
					userList.add(new User(getUsername, getBio, profiles, timelines));
																																																			
					userAlreadyExistChecker = 0;
				}
				else System.out.println("Already existing user name");
				return;
			}
				
//------------------------------------------------------------------------------------------------------			

			if( input.startsWith("Follow") ) {
				String getFirstUsername = "";
				String getSecondUsername = "";
				
				for(int i=7; i > 0; i++) {
					if(input.charAt(i) == ' ')
						i = -1;
					else getFirstUsername = getFirstUsername + input.charAt(i);	
				}
				
				for(int i=6+getFirstUsername.length()+2; i < input.length(); i++) {
					getSecondUsername = getSecondUsername + input.charAt(i);
				}
				
				int userExistChecker = 0;
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername))
						userExistChecker++;
					if(userList.get(i).username.equals(getSecondUsername))
						userExistChecker++;
				}
				
				if(userExistChecker != 2) {
					System.out.println("At least one of the users does not exist");
					userExistChecker = 0;
					return;
				}
				userExistChecker = 0;	
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername)) {
						for(int k=0; k < userList.size(); k++) {
							if(userList.get(k).username.equals(getSecondUsername) && userList.get(k).followerList.contains(userList.get(i))) {
								System.out.println("Already following");
								break;
							}
							if(userList.get(k).username.equals(getSecondUsername) && !userList.get(k).followerList.contains(userList.get(i))) {
								userList.get(k).followerList.add(userList.get(i));
								userList.get(k).followerList.trimToSize();	
								userList.get(i).followList.add(userList.get(k)); 
								userList.get(i).followList.trimToSize();		
								System.out.println(getFirstUsername + " is following " + getSecondUsername);
								userList.get(i).timelineUpdater();
								break;
							}
						}
						break;
					}
				}
				return;
			}
			
			
//------------------------------------------------------------------------------------------------------					
			
			if( input.startsWith("Unfollow") ) {
				String getFirstUsername = "";
				String getSecondUsername = "";
				
				for(int i=9; i > 0; i++) {
					if(input.charAt(i) == ' ')
						i = -1;
					else getFirstUsername = getFirstUsername + input.charAt(i);	
				}
				
				for(int i=8+getFirstUsername.length()+2; i < input.length(); i++) {
					getSecondUsername = getSecondUsername + input.charAt(i);
				}
				
				int userExistChecker = 0;
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername))
						userExistChecker++;
					if(userList.get(i).username.equals(getSecondUsername))
						userExistChecker++;
				}
				
				if(userExistChecker != 2) {
					System.out.println("At least one of the users does not exist");
					userExistChecker = 0;
					return;
				}
				userExistChecker = 0;
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername)) {
						for(int k=0; k < userList.size(); k++) {		
							if(userList.get(k).username.equals(getSecondUsername) && !userList.get(k).followerList.contains(userList.get(i))) {
								System.out.println("Already not following");
								break;
							}
							if(userList.get(k).username.equals(getSecondUsername) && userList.get(k).followerList.contains(userList.get(i))) {
								userList.get(k).followerList.remove(userList.get(i));
								userList.get(k).followerList.trimToSize();
								userList.get(i).followList.remove(userList.get(k)); 
								userList.get(i).followList.trimToSize();
								System.out.println(getFirstUsername + " is unfollowing " + getSecondUsername);
								userList.get(i).timelineUpdater();
								break;
							}	
						}
						break;
					}
				}
				return;	
			}
			
//------------------------------------------------------------------------------------------------------			
			
			if( input.startsWith("Share") ) {
				String getFirstUsername = "";
				String message = "";
				
				for(int i=6; i > 0; i++) {
					if(input.charAt(i) == ' ')
						i = -1;
					else getFirstUsername = getFirstUsername + input.charAt(i);	
				}
				
				for(int i=5+getFirstUsername.length()+2; i < input.length(); i++) {
					message = message + input.charAt(i);
				}
				
				int userExistChecker = 0;
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername))
						userExistChecker++;
				}
				
				if(userExistChecker != 1) {
					System.out.println("User does not exist");
					userExistChecker = 0;
					return;
				}
				userExistChecker = 0;
		
			if(getFirstUsername.equals("master")) {	
				System.out.println(getFirstUsername + " shared the following message for EVERYONE: " + message);
				for(int i=0; i < userList.size(); i++) {	
					 Date date = new Date();	 
					 Message temp = new Message(userList.get(i), date, message);
					 userList.get(i).tweetWriter(temp);	 
					 userList.get(i).tweets.trimToSize();
					 userList.get(i).tweetsSorter();
				 	 userList.get(i).profileUpdater();
				}
				
				for(int i=0; i < userList.size(); i++) {			
						for(int k=0; k < userList.get(i).followerList.size(); k++ ) {
							userList.get(i).followerList.get(k).timelineUpdater();
						}
					}
				return;
			}	
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername)) {
					System.out.println(getFirstUsername + " shared the following message: " + message);
					 Date date = new Date();	 
					 Message temp = new Message(userList.get(i), date, message);
					 userList.get(i).tweetWriter(temp);	 
					 userList.get(i).tweets.trimToSize();
					 userList.get(i).tweetsSorter();
				 	 userList.get(i).profileUpdater();
				 	 break;
					}
				
				}
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername)) {				
						for(int k=0; k < userList.get(i).followerList.size(); k++ ) {
							userList.get(i).followerList.get(k).timelineUpdater();
						}
						break;
						}
					}
				
				return;
				}
				
//------------------------------------------------------------------------------------------------------				
				
			if( input.startsWith("Edit") ) {
				String getFirstUsername = "";
				String getOldMessage = "";
				String getNewMessage = "";
				
				for(int i=5; i > 0; i++) {
					if(input.charAt(i) == ' ')
						i = -1;
					else getFirstUsername = getFirstUsername + input.charAt(i);	
				}
				
				for(int i=4+getFirstUsername.length()+2; i > 0; i++) {
					if(input.charAt(i) == '|')
						i = -1;
					else getOldMessage = getOldMessage + input.charAt(i);
				}
				getOldMessage = getOldMessage.substring(0,getOldMessage.length()-2);
				
				for(int i=4+getFirstUsername.length()+getOldMessage.length()+2; i > 0; i++) {
					if(input.charAt(i) == ' ')
						i = -1;
					else getOldMessage = getOldMessage + input.charAt(i);
				}
				
				
				int indexFinder = 0;
				
				for(int i=0; i < input.length(); i++) {
					if(input.charAt(i) == '|')
						break;
					else indexFinder++;
				}
				
				for(int i=indexFinder+2; i < input.length(); i++) {
					getNewMessage = getNewMessage + input.charAt(i);
				}
				
				int userExistChecker = 0;
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername))
						userExistChecker++;
				}
				
				if(userExistChecker != 1) {
					System.out.println("User does not exist");
					userExistChecker = 0;
					return;
				}
				userExistChecker = 0;
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername)) {
						userList.get(i).tweetsSorter();	
						for(int k=0; k < userList.get(i).tweets.size(); k++) {
							if(userList.get(i).tweets.get(k).message.substring(17).equals(getOldMessage)) {
								userList.get(i).tweets.get(k).message = userList.get(i).tweets.get(k).message.substring(0,16) + " " + getNewMessage;
								break;
							}	
						}
						userList.get(i).profileUpdater();
					break;	
					}
				}
				
				for(int i=0; i < userList.size(); i++) {
					if(userList.get(i).username.equals(getFirstUsername)) {				
						for(int k=0; k < userList.get(i).followerList.size(); k++ ) {
							userList.get(i).followerList.get(k).timelineUpdater();
						}
						break;
						}
					}
				
				
				
				
				
				
				return;
			}
			
//------------------------------------------------------------------------------------------------------			
			
			if( input.startsWith("Save") ) {
				
				String getFolder = "";
				
				for(int i=5; i < input.length(); i++) {
					getFolder = getFolder + input.charAt(i);
				}
		try {		
				File saving = new File(getFolder);
				if (!saving.exists()) saving.mkdir();
				
				
				ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(saving + "/" + "savelog.bin"));
				objOut.writeObject(profiles);
				objOut.writeObject(timelines);
				objOut.writeObject(userList);
				objOut.flush();
				objOut.close();

		}
		
		catch(Exception e) {
			
		}
		
				
				
			}
			
//------------------------------------------------------------------------------------------------------
					
			if( input.startsWith("Exit") ) {
				System.exit(0);
			}
			
//------------------------------------------------------------------------------------------------------
			
		} 
			


}