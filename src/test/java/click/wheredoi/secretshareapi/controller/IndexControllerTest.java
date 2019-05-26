/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.controller;

import click.wheredoi.secretshareapi.dto.SecretDTO;
import click.wheredoi.secretshareapi.exceptions.NotFoundException;
import click.wheredoi.secretshareapi.model.Secret;
import click.wheredoi.secretshareapi.service.SecretService;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IndexControllerTest extends AbstractRestControllerTest {
    private char[] allowCharactersInNanoId = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private String nanoId = NanoIdUtils.randomNanoId(new Random(), allowCharactersInNanoId,6);
    private Secret secret;
    private SecretDTO secretDTO;

    @Mock
    private SecretService secretService;

    @InjectMocks
    private IndexController indexController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        secretDTO = new SecretDTO();

        secret = new Secret();
        secret.setId(nanoId);
        secret.setData("hpYKofvmUoE=");
        secret.setExpires(new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30)));
        secret.setCreated(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void createSecretEmptyDataReturnsBadRequest() throws Exception {
        secretDTO.setExpires(new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30)));

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(secretDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSecretEmptyExpiresReturnsBadRequest() throws Exception {
        secretDTO.setData(secret.getData());

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(secretDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSecretNonFutureExpiresReturnsBadRequest() throws Exception {
        secretDTO.setExpires(new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(secretDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSecretReturnsId() throws Exception {
        secretDTO.setData(secret.getData());
        secretDTO.setExpires(new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30)));

        Mockito.when(secretService.createSecret(Mockito.any(SecretDTO.class))).thenReturn(nanoId);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(secretDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(nanoId));
    }

    @Test
    void getSecretShouldReturnSecret() throws Exception {
        Mockito.when(secretService.getSecret(nanoId)).thenReturn(secret);

        mockMvc.perform(get("/" + nanoId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(nanoId)))
                .andExpect(jsonPath("$.data", equalTo(secret.getData())))
                .andExpect(jsonPath("$.expires", equalTo(secret.getExpires().getTime())));
    }

    @Test
    void getSecretWrongIdFormatReturnsMethodNotAllowed() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void getSecretNonExistingIdReturnsNotFound() throws Exception {
        Mockito.when(secretService.getSecret(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/test12"))
                .andExpect(status().isNotFound());
    }
}
