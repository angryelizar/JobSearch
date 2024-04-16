package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendMessageDto {
    private Long messageAuthor;
    private Long messageRecipient;
    private Long respondApplicant;
    private String messageText;
}
