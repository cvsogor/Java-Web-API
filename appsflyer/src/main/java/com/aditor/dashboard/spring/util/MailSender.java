package com.aditor.dashboard.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.StringTerm;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class MailSender
{
    Properties props = new Properties();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MailSender()
    {
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    public void SendMail(final String from, final String password, String to, String subject, String text, String attachFilePath) //throws MessagingException
    {
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(from, password);
                    }
                });
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            if(attachFilePath == null)
            {
                message.setText(text);
            }
            else
            {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(text);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachFilePath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(source.getName());
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart );
            }
            Transport.send(message);
        }
        catch (MessagingException e)
        {
            logger.error("Send Mail Error: ", e);
        }
    }
}
