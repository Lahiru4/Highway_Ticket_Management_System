package lk.ijse.ticket_service.dao;

import lk.ijse.ticket_service.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepo extends JpaRepository<Ticket,String> {
}
