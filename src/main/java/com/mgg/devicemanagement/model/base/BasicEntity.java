package com.mgg.devicemanagement.model.base;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BasicEntity {

    @Id
    @GeneratedValue
    Long id;
}
