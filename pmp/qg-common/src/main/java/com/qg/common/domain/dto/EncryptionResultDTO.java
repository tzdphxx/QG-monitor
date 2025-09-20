package com.qg.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionResultDTO {
    private  String encryptedData;
    private  String encryptedKey;
}
