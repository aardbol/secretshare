/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SecretServiceTest {
    private SecretDTO secretDTO;
    private Secret secret;
    private Secret secretNotExpired;
    private char[] allowCharactersInNanoId = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    @InjectMocks
    private SecretService secretService;

    @Mock
    private SecretRepository secretRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        secretDTO = new SecretDTO();
        secretDTO.setData("hpYKofvmUoE=");
        secretDTO.setExpires(60);

        secret = new Secret();
        secret.setId(NanoIdUtils.randomNanoId(new Random(), allowCharactersInNanoId,6));
        secret.setData(secretDTO.getData());
        secret.setExpires(new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(secretDTO.getExpires())));

        secretNotExpired = new Secret();
        secret.setId(NanoIdUtils.randomNanoId(new Random(), allowCharactersInNanoId,6));
        secret.setData(secretDTO.getData());
        secretNotExpired.setExpires(new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30)));
    }

    @Test
    // It this a useful test or can it be improved? Because all it does is "test" the save() method of the repository
    void doesCreateSecretReturnSecretWithNanoId() {
        Mockito.when(secretRepository.save(Mockito.any(Secret.class))).thenReturn(secret);
        Secret secret = secretService.createSecret(secretDTO);

        assertNotNull(secret);
        assertTrue(secret.getId().matches("[0-9a-zA-Z]{6}"));
    }

    @Test
    void getSecretExpiredShouldThrowNotFound() {
        Mockito.when(secretRepository.findById(secret.getId())).thenReturn(Optional.of(secret));

        assertThrows(NotFoundException.class, ()-> secretService.getSecret(secret.getId()));
    }

    @Test
    void getSecretNonExpiredShouldReturnSecret() {
        Mockito.when(secretRepository.findById(secretNotExpired.getId())).thenReturn(Optional.of(secretNotExpired));
        Secret secret1 = secretService.getSecret(secretNotExpired.getId());

        assertEquals(secretNotExpired.getId(), secret1.getId());
    }

    @Test
    void getSecretNonExistingShouldReturnNull() {
        Mockito.when(secretRepository.findById("test")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> secretService.getSecret("test"));
    }
}
