/*
 * Copyright (c) 2019 Leonardo Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.controller;

import click.wheredoi.secretshareapi.dto.SecretDTO;
import click.wheredoi.secretshareapi.exception.BadRequestException;
import click.wheredoi.secretshareapi.model.Secret;
import click.wheredoi.secretshareapi.service.SecretService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    Secret getSecret(@PathVariable String id, HttpServletRequest request) {
        return secretService.getSecret(id, request);
    }

    @GetMapping("version")
    String getVersion() {
        return getClass().getPackage().getName() + " " + getClass().getPackage().getImplementationVersion();
    }
}
