/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.service;

import click.wheredoi.secretshareapi.dto.SecretDTO;
import click.wheredoi.secretshareapi.exceptions.NotFoundException;
import click.wheredoi.secretshareapi.model.Secret;
import click.wheredoi.secretshareapi.repo.SecretRepository;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SecretService {

    private SecretRepository secretRepository;

    public SecretService(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    /**
     * Create a secret and return but the ID
     *
     * @param secretDTO SecretDTO object containing the encrypted data and expiring info
     * @return ID of the secret
     */
    public Secret createSecret(SecretDTO secretDTO) {
        Secret secret = new Secret();
        char[] allowCharactersInNanoId = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        secret.setId(NanoIdUtils.randomNanoId(new Random(), allowCharactersInNanoId,6));
        secret.setData(secretDTO.getData());
        secret.setExpires(new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(secretDTO.getExpires())));

        return secretRepository.save(secret);
    }

    /**
     * Get a non-expired secret by ID
     *
     * @param id NanoId
     * @return Secret
     */
    public Secret getSecret(String id) {
        Secret secret = secretRepository.findById(id).orElse(null);

        // Also check for expired shares that haven't been deleted yet
        if (secret == null || secret.getExpires().before(new Timestamp(System.currentTimeMillis()))) {
            throw new NotFoundException();
        }
        return secret;
    }

    /**
     * Delete expired, should be triggered by a cron job or so
     * TODO: implement automation
     */
    public void deleteExpiredSecrets() {
        secretRepository.deleteExpired();
    }
}
