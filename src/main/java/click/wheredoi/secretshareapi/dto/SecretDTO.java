/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.dto;

import click.wheredoi.secretshareapi.validation.TimeLimitation;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class SecretDTO {
    @NotEmpty
    private String data;

    @NotNull
    @Future
    // min = 5 mins, max = 30 days
    @TimeLimitation(min = 5, max = 43_200)
    private Timestamp expires;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getExpires() {
        return expires;
    }

    public void setExpires(Timestamp expires) {
        this.expires = expires;
    }
}
