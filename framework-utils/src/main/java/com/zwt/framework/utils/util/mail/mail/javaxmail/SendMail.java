package com.zwt.framework.utils.util.mail.mail.javaxmail;

import com.zwt.framework.utils.util.mail.exception.SendMailException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author zwt
 * @detail  使用JavaxMail 发送邮件
 * @date 2019/1/23
 * @since 1.0
 */
public class SendMail{

    //SMTP主机设置
    private static final String MAIL_HOST_NAME = "smtp.qq.com";
    //发送邮件的邮箱用户名
    private static final String MAIL_SYSTEM_USER = "test@qq.com";
    //发送邮件的邮箱密码
    private static final String MAIL_SYSTEM_PASSWORD = "SMTP授权码";

    /**
     * 发送邮件
     * @param subject
     * @param receivers
     * @param copys
     * @param msg
     * @param attachName
     * @return
     */
    public static boolean sendMail(String subject, List<String> receivers, List<String> copys, String msg, String attachName){
        if (CollectionUtils.isEmpty(receivers) || (subject == null) || (msg == null)) {
            throw new SendMailException("Email receivers or subject or content must be not null");
        }
        boolean isSuccess = false;
        try {
            // 获取系统属性
            Properties properties = System.getProperties();
            // 设置邮件服务器
            properties.setProperty("mail.smtp.host", MAIL_HOST_NAME);
            //传输协议SMTP
            properties.setProperty("mail.transport.protocol", "smtp");
            //开启SMTP认证
            properties.setProperty("mail.smtp.auth", "true");
            //SMTP端口号
            properties.setProperty("mail.smtp.port","465");
            //开启ssl
            properties.setProperty("mail.smtp.ssl.enable","true");
            //用户名密码认证
            properties.setProperty("mail.user",MAIL_SYSTEM_USER );
            properties.setProperty("mail.password", MAIL_SYSTEM_PASSWORD);
            // 获取默认的 Session 对象。
            Session session = Session.getDefaultInstance(properties);
            // 创建默认的 MimeMessage 对象。
            MimeMessage message = new MimeMessage(session);
            // 发信人
            message.setFrom(new InternetAddress(MAIL_SYSTEM_USER));
            // 收信人
            for(String receiver:receivers){
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(receiver));
            }
            //抄送
            if(!CollectionUtils.isEmpty(copys)){
                for(String copy:copys){
                    message.addRecipient(Message.RecipientType.CC,
                            new InternetAddress(copy));
                }
            }
            //邮件主题
            message.setSubject(subject);

            //有附件创建附件发送
            if(!StringUtils.isEmpty(attachName)){
                // 创建消息部分
                BodyPart messageBodyPart = new MimeBodyPart();
                // 消息
                messageBodyPart.setText(msg);
                // 创建多重消息
                Multipart multipart = new MimeMultipart();
                // 设置文本消息部分
                multipart.addBodyPart(messageBodyPart);
                // 附件部分
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName("附件");
                multipart.addBodyPart(messageBodyPart);
                // 发送完整消息
                message.setContent(multipart);   //setContent方法可以插入html标签生成Html邮件
            }else{
                message.setText(msg);
            }

            // 发送消息
            Transport.send(message);

            isSuccess = true;
        }catch (MessagingException e){
            e.printStackTrace();
            throw  new SendMailException(e.getMessage());
        }

        return isSuccess;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("test1@qq.com");
        sendMail("test",list,null,"测试一下",null);
    }

}
