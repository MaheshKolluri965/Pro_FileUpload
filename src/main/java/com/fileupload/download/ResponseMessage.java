package com.fileupload.download;

public class ResponseMessage {
	private String message;

	  public ResponseMessage(String message) {
	    this.message = message;
	  }

	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }

}

/*
 *4. Define Response Information Classes
Let’s create two classes in message package. The controller will use these classes for sending message via HTTP responses.

ResponseFile: contains name, url, type, size
ResponseMessage for notification/information message
 * 
 *  */
