package lk.ijse.user_service.controller;

import lk.ijse.user_service.dto.ResponseDTO;
import lk.ijse.user_service.dto.UserDTO;
import lk.ijse.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseDTO responseDTO;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> newUser(@Validated @RequestBody UserDTO userDTO) {

        logger.info("Saving Ticket details");
        try {
            String opt = userService.newUser(userDTO);
            if (opt.equals("User already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("User already exists");
                responseDTO.setContent(userDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("User saved successfully");
                responseDTO.setContent(userDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error saving user : ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateUser(@Validated @RequestBody UserDTO userDTO) {
        logger.info("Updating User details");

        ResponseEntity<ResponseDTO> responseEntity = new ResponseEntity<>(responseDTO,HttpStatus.OK);

        try {
            String resp = userService.updateUser(userDTO);
            if (resp.equals("User updated successfully")) {
                responseDTO.setCode("200");
                responseDTO.setMessage("User updated successfully");
                responseDTO.setContent(userDTO);
                responseEntity =  new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error updating user: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            responseEntity= new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/selectedUser/{id}")
    public ResponseEntity<?> getSelectedUser(@PathVariable String id) {
        logger.info("Fetching user with ID: {}", id);
        try {
            UserDTO userDTO = userService.getUser(id);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching user by ID: {}", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userExists/{userId}")
    public ResponseEntity<?> isUserExist(@PathVariable String userId) {
        logger.info("Fetching user with ID: {}", userId);

        try {
            boolean exist = userService.isUserExist(userId);
            return new ResponseEntity<>(exist, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching user by ID: {}", userId, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent( exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
