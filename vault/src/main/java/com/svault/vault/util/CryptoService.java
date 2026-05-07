package com.svault.vault.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Service;

//TODO remove when implemented Redis

@Service
public class CryptoService {

    public boolean verifySignature(String nonce, String signature, String publicKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(
                new X509EncodedKeySpec(keyBytes)
            );

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(pubKey);
            sig.update(nonce.getBytes(StandardCharsets.UTF_8));

            return sig.verify(Base64.getDecoder().decode(signature));

        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed", e);
        }
    }
}