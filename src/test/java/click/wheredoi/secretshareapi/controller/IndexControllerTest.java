/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.controller;

import click.wheredoi.secretshareapi.dto.SecretDTO;
import click.wheredoi.secretshareapi.exception.NotFoundException;
import click.wheredoi.secretshareapi.model.AccessRecord;
import click.wheredoi.secretshareapi.model.Secret;
import click.wheredoi.secretshareapi.service.AccessService;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IndexControllerTest extends AbstractRestControllerTest {
    private char[] allowCharactersInNanoId = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private String nanoId = NanoIdUtils.randomNanoId(new Random(), allowCharactersInNanoId,6);
    private Secret secret;
    private SecretDTO secretDTO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SecretService secretService;

    @Mock
    private AccessService accessService;

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

        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setIp("127.0.0.1");
        accessRecord.setId(1);
        accessRecord.setSecret(secret.getId());
        Set<AccessRecord> accessRecords = new HashSet<>();
        accessRecords.add(accessRecord);
        secret.setAccessRecords(accessRecords);
    }

    @Test
    void createSecretEmptyDataReturnsBadRequest() throws Exception {
        secretDTO.setExpires(TimeUnit.DAYS.toMillis(30));

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
        secretDTO.setExpires(TimeUnit.DAYS.toMinutes(-1));

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(secretDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSecretReturnsSecret() throws Exception {
        secretDTO.setData(secret.getData());
        secretDTO.setExpires(TimeUnit.DAYS.toMinutes(30));

        Mockito.when(secretService.createSecret(Mockito.any(SecretDTO.class))).thenReturn(secret);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(secretDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(nanoId)))
                .andExpect(jsonPath("$.data", equalTo(secret.getData())))
                .andExpect(jsonPath("$.expires", equalTo(secret.getExpires().getTime())));
    }

    @Test
    void getSecretShouldReturnSecretAndAccessRecords() throws Exception {
        Mockito.when(secretService.getSecret(Mockito.eq(nanoId), Mockito.any(HttpServletRequest.class))).thenReturn(secret);

        mockMvc.perform(get("/" + nanoId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(nanoId)))
                .andExpect(jsonPath("$.data", equalTo(secret.getData())))
                .andExpect(jsonPath("$.expires", equalTo(secret.getExpires().getTime())))
                .andExpect(jsonPath("$.accessRecords[0].secret", equalTo(secret.getId())));
    }

    @Test
    void getSecretWrongIdFormatReturnsMethodNotAllowed() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void getSecretNonExistingIdReturnsNotFound() throws Exception {
        Mockito.when(secretService.getSecret(Mockito.eq("test12"), Mockito.any(HttpServletRequest.class)))
                .thenThrow(NotFoundException.class);
        Mockito.doNothing().when(accessService).addAccessRecord(secret.getId(), request);

        mockMvc.perform(get("/test12"))
                .andExpect(status().isNotFound());
    }
}
