package lk.ijse.ticket_service.controller;

import lk.ijse.ticket_service.dto.ResponseDTO;
import lk.ijse.ticket_service.dto.TicketDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lk.ijse.ticket_service.service.TicketService;
import lk.ijse.ticket_service.service.VehicleService;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final VehicleService vehicleService;
    private final ResponseDTO responseDTO;

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createTicket(@Validated @RequestBody TicketDTO ticketDTO) {

        logger.info("Saving Ticket details");
        try {

            boolean isVehicleExist = vehicleService.isVehicleExists(ticketDTO.getVehicleId());

            if (!isVehicleExist) {
                responseDTO.setCode("404");
                responseDTO.setMessage("Vehicle not found");
                responseDTO.setContent(ticketDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                String opt = ticketService.newTicket(ticketDTO);
                if (opt.equals("Ticket id already exists")) {
                    responseDTO.setCode("207");
                    responseDTO.setMessage("Ticket already exists");
                    responseDTO.setContent(ticketDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
                } else {
                    responseDTO.setCode("200");
                    responseDTO.setMessage("Ticket saved successfully");
                    responseDTO.setContent(ticketDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                }
            }
        } catch (Exception exception) {
            logger.error("Error saving Ticket: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateTicket(@Validated @RequestBody TicketDTO ticketDTO) {
        logger.info("Updating Ticket details");

        // Ensure responseDTO is properly initialized
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            boolean isVehicleExist = vehicleService.isVehicleExists(ticketDTO.getVehicleId());

            if (!isVehicleExist) {
                responseDTO.setCode("404");
                responseDTO.setMessage("Vehicle not found");
                responseDTO.setContent(ticketDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                String resp = ticketService.updateTicket(ticketDTO);
                if (resp.equals("Ticket updated successfully")) {
                    responseDTO.setCode("200");
                    responseDTO.setMessage("Ticket updated successfully");
                    responseDTO.setContent(ticketDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                } else {
                    // Handle unexpected response
                    responseDTO.setCode("400");
                    responseDTO.setMessage(resp);
                    responseDTO.setContent(ticketDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception exception) {
            logger.error("Error updating ticket: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getSelectedTicket(@PathVariable String id) {
        logger.info("Fetching ticket with ID: {}", id);
        try {
            TicketDTO ticketDTO = ticketService.getTicket(id);
            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching ticket by ID: {}", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/updateStatus/{id}")
    public ResponseEntity<ResponseDTO> updateStatus(@PathVariable String id) {
        logger.info("Changing Ticket status");

        try {
            TicketDTO ticket = ticketService.getTicket(id);
            ticket.setStatus("Paid");

            boolean isVehicleExist = vehicleService.isVehicleExists(ticket.getVehicleId());

            if (!isVehicleExist) {
                responseDTO.setCode("404");
                responseDTO.setMessage("Vehicle not found");
                responseDTO.setContent(ticket);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                ResponseEntity<ResponseDTO> responseDTOResponseEntity = updateTicket(ticket);
                if (responseDTOResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    responseDTO.setCode("200");
                    responseDTO.setMessage("Ticket status updated successfully");
                    responseDTO.setContent(ticket);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                }
            }
        } catch (Exception exception) {
            logger.error("Error updating ticket: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        responseDTO.setCode("500");
        responseDTO.setMessage("Failed to update ticket status");
        responseDTO.setContent("Unexpected error occurred");
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}