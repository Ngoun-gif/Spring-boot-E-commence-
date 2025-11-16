package Ecommerce.Application.project.modules.notification.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TelegramService {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.chat-id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String message) {
        try {
            // Replace newline with URL-encoded newline ONLY
            String text = message.replace("\n", "%0A");

            String url = "https://api.telegram.org/bot" + botToken +
                    "/sendMessage?chat_id=" + chatId +
                    "&parse_mode=Markdown" +
                    "&text=" + text;

            restTemplate.getForEntity(url, String.class);

        } catch (Exception e) {
            System.err.println("Telegram send error: " + e.getMessage());
        }
    }
}
