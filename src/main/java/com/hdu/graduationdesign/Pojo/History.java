package com.hdu.graduationdesign.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {
    private int id;
    private String history;
    private LocalDateTime operateTime;
}
