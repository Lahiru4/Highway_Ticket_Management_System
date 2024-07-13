package lk.ijse.payment_service.service;


import lk.ijse.payment_service.dto.PaymentDTO;

public interface PaymentService {

    String makePayment(PaymentDTO paymentDTO);
    void removePayment(String id);
    PaymentDTO getPayment(String id);
    String updatePayment(PaymentDTO paymentDTO);


}
