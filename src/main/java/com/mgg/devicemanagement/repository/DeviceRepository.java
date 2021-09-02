package com.mgg.devicemanagement.repository;

import com.mgg.devicemanagement.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

  Optional<Device> findByDeviceKey(String deviceKey);

  List<Device> findByBrand(String brandName);
}
