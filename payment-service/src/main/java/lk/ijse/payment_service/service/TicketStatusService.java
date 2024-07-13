package lk.ijse.payment_service.service;


import lk.ijse.payment_service.dto.StatusDTO;

public interface TicketStatusService {

    boolean updateTicketStatus(StatusDTO statusDTO);
}
