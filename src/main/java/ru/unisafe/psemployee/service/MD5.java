package ru.unisafe.psemployee.service;

import java.security.NoSuchAlgorithmException;

public interface MD5 {
    String calculateMD5(String data) throws NoSuchAlgorithmException;
}
