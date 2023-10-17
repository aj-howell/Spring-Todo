package com.todo.Email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.todo.task.subtask.SubTask;


public class GMailer {
    
    private String user_email;


    public GMailer(String user_email)
    {
      this.user_email=user_email;
    }

     private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory, String email)
      throws IOException {
    // Load client secrets.s
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(GMailer.class.getResourceAsStream("/client_secret_531354637257-oa33f9p75hnm3c052t1f3jla0u1em5jd.apps.googleusercontent.com.json")));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
        .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
        .setAccessType("offline")
        .build();

     //used to receive the authorization from user   
     LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

      if(!compareLastEmailRecipient(email)) // if the email changed then make the user re-authenticate
      {
         flow.getCredentialDataStore().delete("user"); // Change "user" to your user identifier
         System.out.println("deleted");
      }

      //authorizes the user 
      Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
       //returns an authorized Credential object.
       return credential;
  }

    public void sendEmail(SubTask subtask,  String userEmail) throws Exception
    {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT, jsonFactory,userEmail))
        .setApplicationName("Muslim-Todo")
        .build();


           // Create the email content
        String messageSubject =  subtask.getTaskName();
        String bodyText = subtask.getDescription();

        // Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(userEmail));

        //setting the email after each send
        setUser_email(userEmail);

        //adding email components
        email.addRecipient(javax.mail.Message.RecipientType.TO,
            new InternetAddress(userEmail));
        email.setSubject(messageSubject);
        email.setText(bodyText); 

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        //sending the email
        try {
          service.users().messages().send("me", message).execute();
          System.out.println("message id: " + message.getId());
          System.out.println(message.toPrettyString());
        } catch (GoogleJsonResponseException e) {
          GoogleJsonError error = e.getDetails();
          if (error.getCode() == 403) {
            System.err.println("Unable to create draft: " + e.getDetails());
          } else {
            throw e;
          }
        }

    }

    public void setUser_email(String user_email) {
      this.user_email = user_email;
    }

    public boolean compareLastEmailRecipient(String enteredEmail) {
      // Check if the lastRecipient is equal to the enteredEmail
      return user_email != null && user_email.equals(enteredEmail);
    }
}
