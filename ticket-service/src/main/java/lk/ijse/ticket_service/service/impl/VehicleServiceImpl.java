package lk.ijse.ticket_service.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.ticket_service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

    @Override
    public boolean isVehicleExists(String vehicleId) {
        System.out.println(vehicleId);
        try {
            String url = "http://vehicle-service/api/v1/vehicle/vehicleExists/" + vehicleId;
            Boolean vehicleExists = restTemplate.getForObject(url, Boolean.class);
            logger.info("Vehicle Exists: {}", vehicleExists);
            return vehicleExists != null && vehicleExists;
        } catch (Exception e) {
            logger.error("Error checking if vehicle exists", e);
        }
        return false;
    }
}
