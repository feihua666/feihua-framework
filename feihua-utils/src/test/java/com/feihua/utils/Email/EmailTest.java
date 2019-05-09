package com.feihua.utils.Email;

import com.feihua.utils.EmailUtils.EmailUtils;
import com.feihua.utils.EmailUtils.Mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;

/**
 * Created by yangwei
 * Created at 2019/1/7 17:09
 */
public class EmailTest {
    public static void main(String[] args) throws IOException, MessagingException {
        Session session  = EmailUtils.createSession("smtp.sina.com","feihua666@sina.com","xiaobudian123",true,false,"25");
        Mail mail = new Mail();
        //mail.addToAddress("654593600@qq.com");
        mail.addToAddress("feihua666@sina.com");
        mail.setSubject("subject");
        mail.setFrom("feihua666@sina.com");
        mail.setContent("test");
        EmailUtils.send(session,mail);

    }
}
