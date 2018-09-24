package net.moveltrack.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class EncryptionDecryptionAES {  
    
    static SecretKey secretKey ;
    

    public static void main(String[] args) throws Exception {
    	EncryptionDecryptionAES d = new EncryptionDecryptionAES();
        System.out.println(d.createJWT("sincoplema","1234", "subject", 60*1000));
    }

    

  //Sample method to construct a JWT
    public String createJWT(String id, String issuer, String subject, long ttlMillis) {
     
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
     
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
     
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("moveltr@ck&re@ct");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
     
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                                    .setIssuedAt(now)
                                    .setSubject(subject)
                                    .setIssuer(issuer)
                                    .signWith(signatureAlgorithm, signingKey);
     
        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
        long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
     
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
    
}