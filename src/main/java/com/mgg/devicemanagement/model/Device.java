package com.mgg.devicemanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "devices")
public class Device {

  @Id @GeneratedValue Long id;

  @Column(name = "deviceKey", nullable = false)
  String deviceKey;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "brand", nullable = false)
  String brand;

  @Version Long version;

  @Column(name = "created_time", nullable = false, updatable = false)
  LocalDateTime createdTime;

  @Column(name = "modified_time", nullable = false)
  LocalDateTime modifiedTime;

  @PrePersist
  protected void prePersist() {
    this.createdTime = LocalDateTime.now();
    this.modifiedTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.modifiedTime = LocalDateTime.now();
  }
}
