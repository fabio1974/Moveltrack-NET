package net.moveltrack.firebase;

public class FirebaseMessage {
	private String to;
	private String priority;
	private FirebaseNotification notification;
	
	public FirebaseMessage() {
	}

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

	public FirebaseNotification getNotification() {
		return notification;
	}
	public void setNotification(FirebaseNotification notification) {
		this.notification = notification;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	
	
}