package lk.ijse.payment_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentDTO {

    private String id;

    @NotNull(message = "Payment amount cannot be null")
    @Positive(message = "Payment amount must be positive")
    private Double amount;

    @NotNull(message = "User ID cannot be null")
    @Size(min = 1, message = "User ID cannot be empty")
    private String userId;

    @NotNull(message = "Ticket ID cannot be null")
    @Size(min = 1, message = "Ticket ID cannot be empty")
    private String ticket_id;

}
