/*
 * Copyright (c) 2019 Leonardo Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.service;

import click.wheredoi.secretshareapi.dto.SecretDTO;
import click.wheredoi.secretshareapi.exception.NotFoundException;
import click.wheredoi.secretshareapi.model.Secret;
import click.wheredoi.secretshareapi.repo.SecretRepository;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SecretService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SecretRepository secretRepository;
    private final AccessService accessService;

    public SecretService(SecretRepository secretRepository, AccessService accessService) {
        this.secretRepository = secretRepository;
        this.accessService = accessService;
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
     * Get a non-expired secret by ID and add an keep a record of the user accessing this secret
     *
     * @param id NanoId
     * @return Secret
     */
    public Secret getSecret(String id, HttpServletRequest request) {
        Secret secret = secretRepository.findById(id).orElse(null);

        // Also check for expired shares that haven't been deleted yet
        if (secret == null || secret.getExpires().before(new Timestamp(System.currentTimeMillis()))) {
            throw new NotFoundException();
        }
        accessService.addAccessRecord(id, request);
        // Because of lazy loading, we need to force the loading of the access records
        secret.getAccessRecords().size();

        return secret;
    }

    /**
     * Delete expired, is automatically executed by cronDeleteExpiredSecrets()
     *
     * @see click.wheredoi.secretshareapi.ScheduledTasks
     */
    public void deleteExpiredSecrets() {
        secretRepository.deleteExpired();
        logger.info("deleteExpiredSecrets(): expired secrets have been deleted");
    }
}
