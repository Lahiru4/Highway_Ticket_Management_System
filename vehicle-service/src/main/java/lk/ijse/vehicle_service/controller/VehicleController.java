package lk.ijse.vehicle_service.controller;

import lk.ijse.vehicle_service.dto.ResponseDTO;
import lk.ijse.vehicle_service.dto.VehicleDTO;
import lk.ijse.vehicle_service.service.UserService;
import lk.ijse.vehicle_service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {


    private final VehicleService vehicleService;
    private final UserService userService;
    private final ResponseDTO responseDTO;

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);



    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> newVehicle(@Validated @RequestBody VehicleDTO vehicleDTO) {

        logger.info("Saving Vehicle details");

        try {
            boolean isUserExist = userService.isUserExists(vehicleDTO.getUserId());
            if (!isUserExist){
                responseDTO.setCode("404");
                responseDTO.setMessage("User not found");
                responseDTO.setContent(vehicleDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }else {
                String opt = vehicleService.newVehicle(vehicleDTO);
                if (opt.equals("Vehicle already exists")) {
                    responseDTO.setCode("207");
                    responseDTO.setMessage("Vehicle already exists");
                    responseDTO.setContent(vehicleDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
                } else {
                    responseDTO.setCode("200");
                    responseDTO.setMessage("Vehicle saved successfully");
                    responseDTO.setContent(vehicleDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                }
            }
        } catch (Exception exception) {
            logger.error("Error saving vehicle : ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateVehicle(@Validated @RequestBody VehicleDTO vehicleDTO) {
        logger.info("Updating Vehicle details");

        ResponseEntity<ResponseDTO> responseEntity = new ResponseEntity<>(responseDTO,HttpStatus.OK);

        try {
            boolean isUserExist = userService.isUserExists(vehicleDTO.getUserId());

            if (!isUserExist){
                responseDTO.setCode("404");
                responseDTO.setMessage("User not found");
                responseDTO.setContent(vehicleDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }else {
                String resp = vehicleService.updateVehicle(vehicleDTO);
                if (resp.equals("Vehicle updated successfully")) {
                    responseDTO.setCode("200");
                    responseDTO.setMessage("Vehicle updated successfully");
                    responseDTO.setContent(vehicleDTO);
                    responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.OK);
                }
            }
        } catch (Exception exception) {
            logger.error("Error updating vehicle: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            responseEntity= new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/selectedVehicle/{id}")
    public ResponseEntity<?> getSelectedVehicle(@PathVariable String id) {
        logger.info("Fetching Vehicle with ID: {}", id);
        try {
            VehicleDTO vehicleDTO = vehicleService.getVehicle(id);
            return new ResponseEntity<>(vehicleDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching Vehicle by ID: {}", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/vehicleExists/{vehicleId}")
    public ResponseEntity<?> isVehicleExists(@PathVariable String vehicleId) {
        logger.info("Fetching Vehicle with ID: {}", vehicleId);
        try {
            boolean isExist = vehicleService.isVehicleExist(vehicleId);
            return new ResponseEntity<>(isExist, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching Vehicle by ID: {}", vehicleId, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
