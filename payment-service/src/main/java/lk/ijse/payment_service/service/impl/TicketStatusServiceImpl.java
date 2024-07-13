package lk.ijse.payment_service.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.payment_service.dto.ResponseDTO;
import lk.ijse.payment_service.dto.StatusDTO;
import lk.ijse.payment_service.service.TicketStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional
@RequiredArgsConstructor
public class TicketStatusServiceImpl implements TicketStatusService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(TicketStatusServiceImpl.class);

    @Override
    public boolean updateTicketStatus(StatusDTO statusDTO) {
        try {
            String url = "http://ticket-service/api/v1/ticket/updateStatus/" + statusDTO.getId();
            ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(url, ResponseDTO.class);
            logger.info("Ticket Update Response: {}", responseEntity.getBody());
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            logger.error("Error updating ticket status", e);
        }
        return false;
    }


}
