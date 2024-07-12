package lk.ijse.vehicle_service.service.impl;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lk.ijse.vehicle_service.dao.VehicleRepo;
import lk.ijse.vehicle_service.dto.VehicleDTO;
import lk.ijse.vehicle_service.entity.Vehicle;
import lk.ijse.vehicle_service.exception.QuantityExceededException;
import lk.ijse.vehicle_service.service.VehicleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepo vehicleRepo;


    @Override
    public String newVehicle(VehicleDTO vehicleDTO) {
        logger.info("Attempting to save Vehicle: {}", vehicleDTO.getId());
        boolean opt = vehicleRepo.existsById(vehicleDTO.getId());
        if (opt) {
            logger.warn("Vehicle id already exists: {}", vehicleDTO.getId());
            throw new QuantityExceededException("Vehicle already exists");
        } else {
            vehicleRepo.save(new Vehicle(vehicleDTO.getId(),vehicleDTO.getType(),vehicleDTO.getUserId()));
            logger.info("Vehicle saved successfully: {}", vehicleDTO.getId());
            return "Vehicle saved successfully";
        }
    }

    @Override
    public void deleteVehicle(String id) {

    }

    @Override
    public VehicleDTO getVehicle(String id) {
        logger.info("Fetching Vehicle: {}", id);
        Vehicle vehicle = vehicleRepo.getReferenceById(id);
        return new VehicleDTO(vehicle.getId(), vehicle.getType(),vehicle.getUserId());
    }

    @Override
    public String updateVehicle(VehicleDTO vehicleDTO) {
        logger.info("Attempting to update Vehicle: {}", vehicleDTO.getId());
        Vehicle existingVehicleOpt = vehicleRepo.getReferenceById(vehicleDTO.getId());

        // Update the customer entity with new values
        Vehicle updateVehicle =new Vehicle(vehicleDTO.getId(),vehicleDTO.getType(),vehicleDTO.getUserId());
        updateVehicle.setId(existingVehicleOpt.getId()); // Ensure the ID remains the same

        vehicleRepo.save(updateVehicle);
        logger.info("Vehicle updated successfully: {}", updateVehicle.getId());
        return "Vehicle updated successfully";
    }

    @Override
    public boolean isVehicleExist(String id) {
        return vehicleRepo.existsById(id);
    }
}
