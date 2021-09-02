package com.mgg.devicemanagement.exception;

import lombok.Value;

@Value
public class Violation {
  String fieldName;
  String message;
}
