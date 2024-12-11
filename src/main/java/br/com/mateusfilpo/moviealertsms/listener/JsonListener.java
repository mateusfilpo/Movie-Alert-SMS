package br.com.mateusfilpo.moviealertsms.listener;

import br.com.mateusfilpo.moviealertsms.model.Movie;
import br.com.mateusfilpo.moviealertsms.service.NotificationSnsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class JsonListener {

    private final NotificationSnsService service;

    public JsonListener(NotificationSnsService service) {
        this.service = service;
    }

    @KafkaListener(topics = "new-movie-notification", groupId = "sms-notification-group", containerFactory = "jsonContainerFactory")
    public void consumeNewMovieMessage (@Payload Movie kafkaDTO) {
        service.sendSMS(kafkaDTO);
    }
}
