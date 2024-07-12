package lk.ijse.ticket_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ticket")
public class Ticket {

    @Id
    private String id;
    private String description;
    private String date;
    private String time;
    private String status;
    private String vehicleId;

}
