package com.jwt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class TokenBlackListTest {
    @InjectMocks
    private TokenBlackList tokenBlackList;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void addToBlacklistTest()
    {
        tokenBlackList.addToBlacklist("token add kro");
    }

    @Test
    public void isBlacklistedTest()
    {
        tokenBlackList.isBlacklisted("token add kro");
    }
}
