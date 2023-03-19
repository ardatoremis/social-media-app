import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements CharSequence, Serializable {

	User owner;
	Date unformattedDate;
	String formattedDateString;
	String message;
	
	public Message(User owner, Date date, String message) {
		this.owner = owner;
		this.unformattedDate = date;
		this.formattedDateString = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
		this.message = formattedDateString + " " + message;
		
	}
	
	
	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char charAt(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}