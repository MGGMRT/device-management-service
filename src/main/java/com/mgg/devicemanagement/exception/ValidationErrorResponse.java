package com.mgg.devicemanagement.exception;

import lombok.Value;
import java.util.List;

@Value
public class ValidationErrorResponse {
  List<Violation> violations;
}
