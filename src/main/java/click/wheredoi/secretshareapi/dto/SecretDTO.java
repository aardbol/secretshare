/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class SecretDTO {
    @NotEmpty
    private String data;

    // min = 5 mins, max = 30 days
    @Min(5)
    @Max(43200)
    private long expires;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
