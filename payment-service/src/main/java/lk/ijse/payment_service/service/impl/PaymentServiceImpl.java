package lk.ijse.payment_service.service.impl;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lk.ijse.payment_service.dao.PaymentRepo;
import lk.ijse.payment_service.dto.PaymentDTO;
import lk.ijse.payment_service.dto.StatusDTO;
import lk.ijse.payment_service.entity.Payment;
import lk.ijse.payment_service.exception.QuantityExceededException;
import lk.ijse.payment_service.service.PaymentService;
import lk.ijse.payment_service.service.TicketStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepo paymentRepo;
    private final TicketStatusService ticketStatusService;

    @Override
    public String makePayment(PaymentDTO paymentDTO) {
        logger.info("Attempting to make Payment: {}", paymentDTO.getId());
        boolean opt = paymentRepo.existsById(paymentDTO.getId());
        if (opt) {
            logger.warn("Payment id already exists: {}", paymentDTO.getId());
            throw new QuantityExceededException("Payment already exists");
        } else {

            StatusDTO statusDTO  = new StatusDTO(paymentDTO.getTicket_id(),"Paid");
            boolean isUpdated = ticketStatusService.updateTicketStatus(statusDTO);

            if (isUpdated) {
                paymentRepo.save(new Payment(paymentDTO.getId(),paymentDTO.getAmount(),paymentDTO.getUserId(),paymentDTO.getTicket_id()));
                logger.info("Payment make successfully: {}", paymentDTO.getId());
                return "Payment saved successfully";
            }else {
                logger.error("Payment status not updated on the ticket: {}", paymentDTO.getTicket_id());
                return "Ticket status not updated";
            }

        }
    }

    @Override
    public void removePayment(String id) {

    }

    @Override
    public PaymentDTO getPayment(String id) {
        logger.info("Fetching payment: {}", id);
        Payment payment = paymentRepo.getReferenceById(id);

        return new PaymentDTO(payment.getId(), payment.getAmount(), payment.getUserId(), payment.getTicket_id());
    }

    @Override
    public String updatePayment(PaymentDTO paymentDTO) {
        logger.info("Attempting to update Payment: {}", paymentDTO.getId());
        Payment existingPaymentOpt = paymentRepo.getReferenceById(paymentDTO.getId());

        Payment updatePayment =new Payment(paymentDTO.getId(),paymentDTO.getAmount(),paymentDTO.getUserId(),paymentDTO.getTicket_id());
        updatePayment.setId(existingPaymentOpt.getId()); // Ensure the ID remains the same

        paymentRepo.save(updatePayment);
        logger.info("Payment updated successfully: {}", paymentDTO.getId());
        return "Payment updated successfully";
    }
}
