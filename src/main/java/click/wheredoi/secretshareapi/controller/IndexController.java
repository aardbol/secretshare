/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.controller;

import click.wheredoi.secretshareapi.dto.SecretDTO;
import click.wheredoi.secretshareapi.exceptions.BadRequestException;
import click.wheredoi.secretshareapi.model.Secret;
import click.wheredoi.secretshareapi.service.SecretService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class IndexController {
    private SecretService secretService;

    public IndexController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping
    Secret createSecret(@Valid @RequestBody SecretDTO secretDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().toString());
        }
        return secretService.createSecret(secretDTO);
    }

    @GetMapping("{id:[a-zA-Z0-9]{6}}")
    Secret getSecret(@PathVariable String id) {
        return secretService.getSecret(id);
    }
}
