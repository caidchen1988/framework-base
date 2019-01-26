package com.zwt.framework.utils.util.mail.mail.apache;


import com.zwt.framework.utils.util.mail.exception.SendMailException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用Apache-commons-email 发送邮件
 */
public class SendMail {

    //SMTP主机设置
    private static final String MAIL_HOST_NAME = "smtp.qq.com";
    //发送邮件的邮箱用户名
    private static final String MAIL_SYSTEM_USER = "test@qq.com";
    //发送邮件的邮箱密码
    private static final String MAIL_SYSTEM_PASSWORD = "SMTP授权码";

    /**
     * 发送邮件
     * @param subject  邮件主题
     * @param receivers  接受者邮件地址
     * @param copys    要抄送的邮件地址
     * @param msg       内容
     * @param attachName 附件地址
     * @return
     * @throws Exception
     */
    public static boolean sendMail(String subject, List<String> receivers,List<String> copys, String msg, String attachName){
        if (CollectionUtils.isEmpty(receivers) || (subject == null) || (msg == null)) {
            throw new SendMailException("Email receivers or subject or content must be not null");
        }
        boolean isSuccess = false;
        try {
            //SimpleEmail email = new SimpleEmail();//创建简单邮件,不可添加附件、HTML文本等
            //MultiPartEmail  email = new MultiPartEmail();//创建能加附件的邮件,可多个、网络附件亦可
            //HtmlEmail email = new HtmlEmail();//创建能加附件内容为HTML文本的邮件、HTML直接内联图片但必须用setHtmlMsg()传邮件内容
            MultiPartEmail email = new MultiPartEmail();
            //设置smtp 主机
            email.setHostName(MAIL_HOST_NAME);
            email.setSmtpPort(465);
            email.setSSL(true);
            //邮箱登录认证（保证邮件服务器POP3/SMTP服务开启）
            email.setAuthentication(MAIL_SYSTEM_USER,MAIL_SYSTEM_PASSWORD);
            //收信人列表
            for(String receiver:receivers){
                email.addTo(receiver, receiver);
            }
            //抄送人列表
            if(!CollectionUtils.isEmpty(copys)){
                for(String copy:copys){
                    email.addCc(copy,copy);
                }
            }
            //发信人
            email.setFrom(MAIL_SYSTEM_USER, MAIL_SYSTEM_USER);
            //邮件主题
            email.setSubject(MimeUtility.encodeText(subject));
            //邮件内容
            email.setMsg(msg);
            //邮件附件
            if ((attachName != null) && (attachName.trim().length() > 0)) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(attachName);
                attachment.setDisposition("attachment");
                email.attach(attachment);
            }
            //发送邮件
            email.send();
            isSuccess = true;
        } catch (EmailException e) {
            throw new SendMailException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new SendMailException(e.getMessage());
        }
        return isSuccess;
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("test1@qq.com");
        sendMail("test",list,null,"测试一下",null);
    }
}