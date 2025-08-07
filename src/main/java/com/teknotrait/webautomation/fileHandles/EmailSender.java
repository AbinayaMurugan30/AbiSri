package com.teknotrait.webautomation.fileHandles;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailSender {

	public static void sendReportByEmail() {
		System.out.println("Sending email");
		String to = PropertyFileHandling.get("email.to");
		String from = PropertyFileHandling.get("email.from");
		String host = PropertyFileHandling.get("smtp.host");
		String port = PropertyFileHandling.get("smtp.port");
		String password = PropertyFileHandling.get("email.password");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Automation Test Report and Log");

			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(
					"Hi,\n\nPlease find attached the automation test report.\n\nRegards,\nAbinaya Jeyamurugan");

			// Report Attachment
			MimeBodyPart attachReportPart = new MimeBodyPart();
//			String filePath = PropertyFileHandling.get("reportPath");
//			attachReportPart.attachFile(new File(filePath));

			// Fetch the report folder path
			String reportFolderPath = PropertyFileHandling.getAbsolutePath("reportPath");

			// Locate the most recent report file in that folder
			File reportDir = new File(reportFolderPath);
			File[] files = reportDir.listFiles((dir, name) -> name.endsWith(".html") || name.endsWith(".pdf"));

			if (files == null || files.length == 0) {
				throw new IOException("No report file found in: " + reportFolderPath);
			}

			// Sort by last modified to get the latest file
			File latestReport = files[0];
			for (File f : files) {
				if (f.lastModified() > latestReport.lastModified()) {
					latestReport = f;
				}
			}

			System.out.println("Attaching report: " + latestReport.getAbsolutePath());
			attachReportPart.attachFile(latestReport);

//			//Logs Attachment
//			MimeBodyPart attachlogspart = new MimeBodyPart();
//		//	String logsfilePath = ConfigReader.get("logsPath");// for logs
//			attachlogspart.attachFile(new File(Hooks.logFilePath));

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(attachReportPart);
			// multipart.addBodyPart(attachlogspart);

			message.setContent(multipart);

			Transport.send(message);
			System.out.println("Email sent successfully.");
		} catch (Exception e) {
			System.out.println("Failed to send email: " + e.getMessage());
		}

	}
}
