package org.mickael.librarybatch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {

    @Bean
    public SimpleMailMessage emailRecoveryTemplate(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("%s");
        simpleMailMessage.setFrom("mc.ocform@gmail.com");
        simpleMailMessage.setSubject("Période d'emprunt dépassée - Bibliothèque d'OCland");
        simpleMailMessage.setText("Bonjour, %s %s" +
                                          "\n\nLa date de retour pour votre emprunt du livre \"%s\" était le : %s" +
                                          "\nVous avez la possibilité, si vous ne l'avez pas encore fait, de prolonger la durée de votre emprunt de 4 semaines." +
                                          "\nSinon, pensez à ramener le livre à votre bibliothèque au plus vite pour éviter une pénalité" +
                                          "\n\n\nBibliothèque d'OCland" +
                                          "\n\n\n\n\nCeci est un envoi automatique, merci de ne pas y répondre.");
        return simpleMailMessage;
    }
}
