package lk.ijse.vehicle_service.dao;

import lk.ijse.vehicle_service.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, String> {
}
