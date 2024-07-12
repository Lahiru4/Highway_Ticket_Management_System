package lk.ijse.ticket_service.service.impl;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lk.ijse.ticket_service.dao.TicketRepo;
import lk.ijse.ticket_service.dto.TicketDTO;
import lk.ijse.ticket_service.entity.Ticket;
import lk.ijse.ticket_service.exception.QuantityExceededException;
import lk.ijse.ticket_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepo ticketRepo;

    @Override
    public String newTicket(TicketDTO ticketDTO) {
        logger.info("Attempting to save Ticket: {}", ticketDTO.getId());
        boolean opt = ticketRepo.existsById(ticketDTO.getId());
        if (opt) {
            logger.warn("Ticket id already exists: {}", ticketDTO.getId());
            throw new QuantityExceededException("Ticket already exists");
        } else {
           ticketRepo.save(new Ticket(ticketDTO.getId(),ticketDTO.getDescription(),ticketDTO.getDate(), ticketDTO.getTime(),ticketDTO.getStatus(),ticketDTO.getVehicleId()));
            logger.info("Ticket saved successfully: {}", ticketDTO.getId());
            return "Ticket saved successfully";
        }
    }

    @Override
    public void deleteTicket(String id) {

    }

    @Override
    public TicketDTO getTicket(String id) {
        logger.info("Fetching ticket: {}", id);
        Ticket ticket = ticketRepo.getReferenceById(id);
        return new TicketDTO(ticket.getId(),ticket.getDescription(),ticket.getDate(),ticket.getTime(),ticket.getStatus(),ticket.getVehicleId());
    }

    @Override
    public String updateTicket(TicketDTO ticketDTO) {

        logger.info("Attempting to update Ticket: {}", ticketDTO.getId());
        Ticket existingTicketOpt = ticketRepo.getReferenceById(ticketDTO.getId());

        // Update the customer entity with new values
        Ticket updateTicket =new Ticket(ticketDTO.getId(),ticketDTO.getDescription(), ticketDTO.getDate(), ticketDTO.getTime(),ticketDTO.getStatus(),ticketDTO.getVehicleId());
        updateTicket.setId(existingTicketOpt.getId()); // Ensure the ID remains the same

        ticketRepo.save(updateTicket);
        logger.info("Ticket updated successfully: {}", ticketDTO.getId());
        return "Ticket updated successfully";
    }
}
