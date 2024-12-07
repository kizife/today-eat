package com.ssafy.cafe.model.dto;

import com.google.firebase.messaging.Message;

public class FcmMessage {
	private boolean validate_only;
	private Message message;
	
	/*constructors & setters, getters*/
	 public FcmMessage(boolean validateOnly, Message message) {
	        this.validate_only = validateOnly;
	        this.message = message;
	    }
	
	public static class Message {
		private Notification notification;
		private String token;
		
		/*constructors & setters, getters*/
		  public Message(Notification notification, String token) {
	            this.notification = notification;
	            this.token = token;
	        }
	}
	
	public static class Notification{
		private String title;
		private String body;
		private String image;
		
		/*constructors & setters, getters*/
		public Notification(String title, String body, String image) {
            this.title = title;
            this.body = body;
            this.image = image;
        }
	}

}
