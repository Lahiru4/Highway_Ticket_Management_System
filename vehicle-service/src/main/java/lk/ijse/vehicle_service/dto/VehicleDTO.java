package lk.ijse.vehicle_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VehicleDTO {
    private String id;

    @NotNull(message = "Vehicle type cannot be null")
    @Size(min = 1, message = "Vehicle type cannot be empty")
    private String type;

    @NotNull(message = "User ID cannot be null")
    @Size(min = 1, message = "User ID cannot be empty")
    private String userId;
}
