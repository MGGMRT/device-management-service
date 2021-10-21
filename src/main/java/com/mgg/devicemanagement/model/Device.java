package com.mgg.devicemanagement.model;

import com.mgg.devicemanagement.model.base.AuditEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

@Data
@Entity
@Table(name = "devices")
public class Device extends AuditEntity implements Serializable {

  @Column(name = "device_key", nullable = false)
  String deviceKey;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "brand", nullable = false)
  String brand;

  @Version Long version;

//  @Column(name = "created_time", nullable = false, updatable = false)
//  LocalDateTime createdTime;
//
//  @Column(name = "modified_time", nullable = false)
//  LocalDateTime modifiedTime;
//
//  @PrePersist
//  protected void prePersist() {
//    this.createdTime = LocalDateTime.now();
//    this.modifiedTime = LocalDateTime.now();
//  }
//
//  @PreUpdate
//  public void preUpdate() {
//    this.modifiedTime = LocalDateTime.now();
//  }
}
