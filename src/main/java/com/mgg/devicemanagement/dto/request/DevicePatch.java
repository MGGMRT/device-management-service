package com.mgg.devicemanagement.dto.request;

import lombok.*;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DevicePatch {
  public String path;
  public String value;
}
