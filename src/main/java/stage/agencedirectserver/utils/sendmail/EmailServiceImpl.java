package stage.agencedirectserver.utils.sendmail;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component @Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public String sendSimpleMail(EmailDetails details) {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(details.getRecipient());
            helper.setText(details.MessageConstructor());
            helper.setSubject("Espace ouverture de compte");
            emailSender.send(message);
            return "Mail sent successfully";
        }catch (MessagingException e){
            e.printStackTrace();
            return "Error while sending mail";
        }

    }

    @Override
    public String sendMailWithAttachment(EmailDetails details){
        return null;
    }
}
