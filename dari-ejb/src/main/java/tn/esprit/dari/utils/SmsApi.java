package tn.esprit.dari.utils;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;

public class SmsApi {
	
	
	/* lors de l'appel de cette methode on doit respecter 
	 * la clause que message body entrée dans cette methode 
	 * soit une chaine de caractere qui ne doit pas depasser 11 caracteres */
	
	public static void sendSmsbyvonage(String from, String to, String messageText) {

		/*
		 * "You can't trust code that you did not totally create yourself ! Ken Thompson"
		 */

		System.out.println(from);
		System.out.println(to);
		NexmoClient client = new NexmoClient.Builder()
				  .apiKey("a0be2a21")
				  .apiSecret("wLo3CZfFEOLmoa2r")
				  .build();

				//String messageText = "Hello from Vonage SMS API";
				TextMessage message = new TextMessage(from, to, messageText);


				SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

				for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
				    System.out.println(responseMessage);
				}
	/* from here we can test our API consumption */ 
	
	}
}
