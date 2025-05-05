package wad.sentinel.api.utils;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import wad.sentinel.api.exceptions.EmailException;

/**
 * Interface to JavaMail to send eMails.
 */
public class Email {

	// Content type
	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_HTML = "text/html";

	// Server configuration
	private String smtpServer;
	private String smtpPort;
	private String smtpUser;
	private String smtpPassword;

	// Addresses
	private InternetAddress addressFrom;
	private InternetAddress addressTo;
	private Vector<InternetAddress> addressCC;
	private Vector<InternetAddress> addressCCO;

	// Content
	private String subject = "";
	private String content = "";
	private String contentType = TEXT_PLAIN;
	private Vector<FileDataSource> attachments;
	private Date sendDate;

	/**
	 * Constructor with server communication parameters
	 * 
	 * @param server
	 * @param port
	 * @param user
	 * @param password
	 */
	public Email(String server, String port, String user, String password) {
		// Set received connection parameters
		this.smtpServer = server;
		this.smtpPort = port;
		this.smtpUser = user;
		this.smtpPassword = password;

		// Initialize vector properties
		this.attachments = new Vector<FileDataSource>();
		this.addressCC = new Vector<InternetAddress>();
		this.addressCCO = new Vector<InternetAddress>();
	}

	/**
	 * Compose a new email
	 *
	 * @param to      Mail 'to' address
	 * @param from    Mail 'from' address
	 * @param subject Mail subject
	 * @param content Mail content
	 * @param html    Indicates wether content is in HTML format (TEXT otherwise)
	 */
	public void compose(String to, String from, String subject, String content, Boolean html)
			throws EmailException {
		setAddressFrom(from);
		setAddressTo(to);
		setSubject(subject);
		setContent(content);

		if ((html != null) && html)
			setContentType(TEXT_HTML);
	}

	/**
	 * Sets an origin address to FROM field
	 * 
	 * @param address Address to set
	 */
	public void setAddressFrom(String address) {
		try {
			this.addressFrom = new InternetAddress(address);
		} catch (AddressException iPE) {
			throw new EmailException("Invalid address: " + address);
		}
	}

	/**
	 * Sets a destination address to TO field
	 * 
	 * @param address Address to set
	 */
	public void setAddressTo(String address) {
		try {
			this.addressTo = new InternetAddress(address);
		} catch (AddressException iPE) {
			throw new EmailException("Invalid address: " + address);
		}
	}

	/**
	 * Adds an address to CC
	 * 
	 * @param address Address to add
	 */
	public void addAddressCC(String address) {
		try {
			this.addressCC.add(new InternetAddress(address));
		} catch (AddressException iPE) {
			throw new EmailException("Invalid address: " + address);
		}
	}

	/**
	 * Adds an address to CCO
	 * 
	 * @param address Address to add
	 */
	public void addAddressCCO(String address) {
		try {
			this.addressCCO.add(new InternetAddress(address));
		} catch (AddressException iPE) {
			throw new EmailException("Invalid address: " + address);
		}
	}

	/**
	 * Adds a file as an attachment
	 * 
	 * @param file File to attach
	 */
	public void addAttachment(File file) {
		FileDataSource fileDataSource = new FileDataSource(file);
		attachments.add(fileDataSource);
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = smtpUser;
			String password = smtpPassword;
			return new PasswordAuthentication(username, password);
		}
	}

	/**
	 * Send the mail.
	 * It has to be populated and all the necessary information registered before
	 * sending.
	 */
	public void send()
			throws EmailException {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", this.smtpServer);
		props.put("mail.smtp.port", this.smtpPort);
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.host", this.smtpServer);
		props.setProperty("mail.port", this.smtpPort);
		props.setProperty("mail.user", this.smtpUser);
		props.setProperty("mail.password", this.smtpPassword);
		Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getDefaultInstance(props, auth);

		try {
			Transport transport = mailSession.getTransport();
			MimeMessage msg = new MimeMessage(mailSession);

			msg.setSubject(this.subject);
			msg.setSentDate(this.sendDate);
			msg.setFrom(this.addressFrom);

			msg.setHeader("MIME-Version", "1.0");
			// msg.setHeader ( "Content-Type", this.tipoContenido + "; charset=\"US-ASCII\""
			// );
			msg.setHeader("Content-Type", this.contentType + "; charset=\"iso-8859-15\"");
			// msg.setHeader ( "Content-Type", this.tipoContenido + "; charset=\"utf-8\"" );

			msg.setHeader("Content-Transfer-Encoding", "7bit");
			msg.addRecipient(Message.RecipientType.TO, this.addressTo);

			if (this.addressCC != null && !this.addressCC.isEmpty()) {
				for (int i = 0; i < this.addressCC.size(); i++) {
					InternetAddress cc = this.addressCC.elementAt(i);
					msg.addRecipient(Message.RecipientType.CC, cc);
				}
			}

			if (this.addressCCO != null && !this.addressCCO.isEmpty()) {
				for (int i = 0; i < this.addressCCO.size(); i++) {
					InternetAddress bbc = this.addressCCO.elementAt(i);
					msg.addRecipient(Message.RecipientType.BCC, bbc);
				}
			}

			if (this.attachments.size() > 0) {

				MimeMultipart multipart = new MimeMultipart();

				BodyPart text = new MimeBodyPart();
				// texto.setText(this.contenido);
				text.setContent(this.content, this.contentType);
				text.setHeader("Content-Type", this.contentType + "; charset=\"iso-8859-15\"");

				multipart.addBodyPart(text);

				for (int i = 0; i < this.attachments.size(); i++) {
					BodyPart adjunto = new MimeBodyPart();

					adjunto.setDataHandler(new DataHandler(this.attachments.elementAt(i)));
					adjunto.setFileName(this.attachments.elementAt(i).getName());

					multipart.addBodyPart(adjunto);
				}

				msg.setContent(multipart);

			} else {

				msg.setContent(this.content, this.contentType);

			}

			transport.connect();
			// transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (NoSuchProviderException iPE) {
			throw new EmailException(this.getClass().getName() + "; Provider error: IP=" + this.smtpServer + " PORT="
					+ this.smtpPort + " USER= " + this.smtpUser);
		} catch (AddressException iPE) {
			throw new EmailException(this.getClass().getName() + "; Incorrect address:" + this.addressTo);
		} catch (MessagingException iPE) {
			throw new EmailException(this.getClass().getName() + "; Error sending email: " + this.addressTo
					+ " Asunto: " + this.subject);
		}
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public InternetAddress getAddressFrom() {
		return addressFrom;
	}

	public void setAddressFrom(InternetAddress addressFrom) {
		this.addressFrom = addressFrom;
	}

	public InternetAddress getAddressTo() {
		return addressTo;
	}

	public void setAddressTo(InternetAddress addressTo) {
		this.addressTo = addressTo;
	}

	public Vector<InternetAddress> getAddressCC() {
		return addressCC;
	}

	public Vector<InternetAddress> getAddressCCO() {
		return addressCCO;
	}

	public void setAddressCC(Vector<InternetAddress> addressCC) {
		this.addressCC = addressCC;
	}

	public void setAddressCCO(Vector<InternetAddress> addressCCO) {
		this.addressCCO = addressCCO;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Vector<FileDataSource> getAttachments() {
		return attachments;
	}

	public void setAttachments(Vector<FileDataSource> attachments) {
		this.attachments = attachments;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

}
