package ru.unisafe.psemployee.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MD5Test {
    private MD5impl md5impl;

    @BeforeEach
    void setUp() {
        md5impl = new MD5impl();
    }

    @DisplayName("MD5 обычной валидной строки")
    @Test
    void testCalculateMD5_validInput() throws NoSuchAlgorithmException {
        String input = "hello";
        String expectedHash = "5d41402abc4b2a76b9719d911017c592";
        assertEquals(expectedHash, md5impl.calculateMD5(input));
    }

    @DisplayName("MD5 пустой строки")
    @Test
    void testCalculateMD5_emptyString() throws NoSuchAlgorithmException {
        String input = "";
        String expectedHash = "d41d8cd98f00b204e9800998ecf8427e";
        assertEquals(expectedHash, md5impl.calculateMD5(input));
    }

    @DisplayName("MD5 разные входные и выходные данные")
    @Test
    void testCalculateMD5_differentInputs() throws NoSuchAlgorithmException {
        String input1 = "test";
        String input2 = "Test";
        assertNotEquals(md5impl.calculateMD5(input1), md5impl.calculateMD5(input2));
    }
}
