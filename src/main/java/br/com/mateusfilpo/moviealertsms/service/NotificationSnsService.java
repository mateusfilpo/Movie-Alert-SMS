package br.com.mateusfilpo.moviealertsms.service;

import br.com.mateusfilpo.moviealertsms.model.Movie;
import br.com.mateusfilpo.moviealertsms.model.User;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class NotificationSnsService {

    private final AmazonSNS amazonSNS;

    public NotificationSnsService(AmazonSNS amazonSNS) {
        this.amazonSNS = amazonSNS;
    }

    public void sendSMS(Movie movieKafkaDTO) {
        movieKafkaDTO.getUsers().forEach(user -> {
            PublishRequest publishRequest = new PublishRequest()
                    .withMessage(createMessage(user, movieKafkaDTO))
                    .withPhoneNumber(user.getPhoneNumber());
            amazonSNS.publish(publishRequest);
        });
    }

    private String createMessage(User user, Movie movieKafkaDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append(user.getFirstName()).append(" ").append(user.getLastName()).append(",\n");
        sb.append("A new movie has just arrived in our catalog, and we think you'll love it:\n\n");
        sb.append("Title: ").append(movieKafkaDTO.getTitle()).append("\n");
        sb.append("Description: ").append(movieKafkaDTO.getDescription()).append("\n");
        sb.append("Genres: ");

        String genres = movieKafkaDTO.getGenres().stream()
                .map(genre -> genre.getName())
                .collect(Collectors.joining(", "));
        sb.append(genres).append("\n");

        sb.append("\nEnjoy watching!");

        return sb.toString();
    }
}
