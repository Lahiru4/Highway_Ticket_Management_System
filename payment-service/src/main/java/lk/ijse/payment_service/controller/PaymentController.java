package lk.ijse.payment_service.controller;

import jakarta.validation.Valid;
import lk.ijse.payment_service.dto.PaymentDTO;
import lk.ijse.payment_service.dto.ResponseDTO;
import lk.ijse.payment_service.service.PaymentService;
import lk.ijse.payment_service.service.TicketStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final TicketStatusService ticketStatusService;
    private final ResponseDTO responseDTO;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

//    private final RestTemplate restTemplate;restTemplate

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> makePayment(@Validated @RequestBody PaymentDTO paymentDTO , BindingResult bindingResult) {

        logger.info("Saving Payment details");
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
        try {
                String opt = paymentService.makePayment(paymentDTO);
                if (opt.equals("Payment id already exists")) {
                    responseDTO.setCode("207");
                    responseDTO.setMessage("Payment already exists");
                    responseDTO.setContent(paymentDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
                } else {

                    if (opt.equals("Payment saved successfully")) {
                        responseDTO.setCode("200");
                        responseDTO.setMessage("Payment saved successfully");
                        responseDTO.setContent(paymentDTO);
                        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                    }else {
                        responseDTO.setCode("405");
                        responseDTO.setMessage("Status not Updated");
                        responseDTO.setContent(paymentDTO);
                        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
                    }

                }

        } catch (Exception exception) {
            logger.error("Error saving Payment: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updatePayment(@Validated @RequestBody PaymentDTO paymentDTO, BindingResult bindingResult) {
        logger.info("Updating Payment details");

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<ResponseDTO> responseEntity = new ResponseEntity<>(responseDTO,HttpStatus.OK);

        try {

                String resp = paymentService.updatePayment(paymentDTO);
                if (resp.equals("Payment updated successfully")) {
                    responseDTO.setCode("200");
                    responseDTO.setMessage("Payment updated successfully");
                    responseDTO.setContent(paymentDTO);
                    responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.OK);
                }

        } catch (Exception exception) {
            logger.error("Error updating payment: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            responseEntity= new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSelectedPayment(@PathVariable String id) {
        logger.info("Fetching payment with ID: {}", id);
        try {
            PaymentDTO paymentDTO = paymentService.getPayment(id);
            return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching payment by ID: {}", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
