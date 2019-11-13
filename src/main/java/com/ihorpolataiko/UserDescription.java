package com.ihorpolataiko;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDescription {
    private String title;
    private String link;
}
