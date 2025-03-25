package ru.unisafe.psemployee.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MasterKeyResponse extends BaseResponse {
    private String key;
    private String master;

    public MasterKeyResponse(String key, String master, boolean success, String msg) {
        super(success, msg);
        this.key = key;
        this.master = master;
    }
}
