package ru.practicum.shareit.feedback.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.model.Feedback;

@Component
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();

        feedback.setId(feedbackDto.getId());
        feedback.setItemId(feedbackDto.getItemId());
        feedback.setUserId(feedbackDto.getUserId());
        feedback.setText(feedbackDto.getText());
        feedback.setCreatedDate(feedbackDto.getCreatedDate());

        return feedback;
    }

    public FeedbackDto toFeedbackDto(Feedback feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .itemId(feedback.getItemId())
                .userId(feedback.getUserId())
                .text(feedback.getText())
                .createdDate(feedback.getCreatedDate())
                .build();
    }
}
