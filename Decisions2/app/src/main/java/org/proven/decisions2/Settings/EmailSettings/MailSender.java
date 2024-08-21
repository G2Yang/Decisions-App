package org.proven.decisions2.Settings.EmailSettings;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender extends AsyncTask<Void, Void, Void> {
    // mRecipient is the recipient of the email
    private String mRecipient;
    //mSubject is the subject of the email
    private String mSubject;
    //mMessage is the content of the message
    private String mMessage;

    public MailSender() {
    }

    public MailSender(String recipient, String subject, String message) {
        mRecipient = recipient;
        mSubject = subject;
        mMessage = message;
    }

    public String getmRecipient() {
        return mRecipient;
    }

    public void setmRecipient(String mRecipient) {
        this.mRecipient = mRecipient;
    }

    public String getmSubject() {
        return mSubject;
    }

    public void setmSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            //Create the email session
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // Enter your Gmail credentials
                    return new PasswordAuthentication("desicionsydf@gmail.com", "hfgdgvmbtthigvfu");
                }
            });

            // Create the email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("desicionsydf@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mRecipient));
            message.setSubject(mSubject);
            message.setContent(mMessage, "text/html; charset=utf-8");

            //Send the email
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}