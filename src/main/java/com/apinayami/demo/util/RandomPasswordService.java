package com.apinayami.demo.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomPasswordService {
    public String randomPassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000); // generates number from 100000 to 999999
        return password+"";
    }
}
