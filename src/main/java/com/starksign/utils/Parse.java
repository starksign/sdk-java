package com.starksign.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.starkbank.ellipticcurve.Ecdsa;
import com.starkbank.ellipticcurve.PublicKey;
import com.starkbank.ellipticcurve.Signature;
import com.starkbank.ellipticcurve.utils.ByteString;
import com.starksign.error.InvalidSignatureError;

import java.lang.reflect.Type;
import java.util.HashMap;

public final class Parse{

    public static <T extends SubResource> T parseAndVerify(Resource.ClassData resource, String content, String signature) throws Exception {
        String verifiedContent = verify(content, signature);
        Gson gson = GsonEvent.getInstance();
        JsonObject contentJson = gson.fromJson(verifiedContent, JsonObject.class);
        JsonObject jsonObject = contentJson.getAsJsonObject();
        if (Api.getLastName(resource).equals("event")){
            jsonObject = contentJson.get(Api.getLastName(resource)).getAsJsonObject();
        }
        return gson.fromJson(jsonObject, (Type) resource.cls);
    }

    public static String verify (String content, String signature) throws Exception {
        Signature signatureObject;
        try {
            signatureObject = Signature.fromBase64(new ByteString(signature.getBytes()));
        } catch (Error | RuntimeException e) {
            throw new InvalidSignatureError("The provided signature is not valid");
        }

        if (verifySignature(content, signatureObject, false)) {
            return content;
        }
        if (verifySignature(content, signatureObject, true)) {
            return content;
        }

        throw new InvalidSignatureError("The provided signature and content do not match the Stark Infra public key");
    }

    private static boolean verifySignature(String content, Signature signature, boolean refresh) throws Exception {
        PublicKey publicKey = Cache.starkPublicKey;
        if (publicKey == null || refresh) {
            publicKey = getStarkPublicKey();
            Cache.starkPublicKey = publicKey;
        }
        return Ecdsa.verify(content, signature, publicKey);
    }

    private static PublicKey getStarkPublicKey() throws Exception {
        HashMap<String, Object> query = new HashMap<>();
        query.put("limit", "1");
        String content = Response.fetch(
            "/public-key",
            "GET",
            null,
            query
        ).content();
        JsonObject contentJson = new Gson().fromJson(content, JsonObject.class);
        JsonArray publicKeys = contentJson.get("publicKeys").getAsJsonArray();
        return PublicKey.fromPem(
                publicKeys.get(0).getAsJsonObject().get("content").getAsString()
        );
    }
}

abstract class Cache {
    public static PublicKey starkPublicKey = null;
}
