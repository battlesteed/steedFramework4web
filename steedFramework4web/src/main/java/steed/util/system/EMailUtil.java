package steed.util.system;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import steed.util.base.PropertyUtil;

public class EMailUtil {
	
	public static void sendEmail(String content,String title,String sendTo) throws AddressException, MessagingException{
		Properties properties = PropertyUtil.getProperties(PropertyUtil.configProperties);
		Session session = Session.getInstance(properties, new MyAuthenticator());
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(properties.getProperty("mail.accout")));
		message.setSubject(title);
		message.setRecipients(RecipientType.TO,InternetAddress.parse(sendTo));
		MimeBodyPart contentBodyPart = new MimeBodyPart();
		contentBodyPart.setContent(content,"text/html;charset=utf-8");
		MimeMultipart mimeMuti = new MimeMultipart("mixed");
		mimeMuti.addBodyPart(contentBodyPart);
		/*File attachment = new File("E:\\workspase\\readPacket\\src\\config4server.json");
		 // 添加附件的内容
        if (attachment != null) {
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            
            // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
            // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
            
            //MimeUtility.encodeWord可以避免文件名乱码
            try {
				attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
            mimeMuti.addBodyPart(attachmentBodyPart);
        }*/
		
		
		message.setContent(mimeMuti);
		message.saveChanges();
		//message.setContent("Michael", "text/html;charset=gbk");
		Transport.send(message);
	}
}
