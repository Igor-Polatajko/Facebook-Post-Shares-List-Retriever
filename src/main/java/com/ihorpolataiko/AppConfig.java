package com.ihorpolataiko;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AppConfig {
    private String email;
    private String password;
    private String link;
    private String driverLocation;
    private boolean seeMagic;
}
