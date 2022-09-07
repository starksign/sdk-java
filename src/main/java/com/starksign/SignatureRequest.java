package com.starksign;

import com.starksign.utils.Parse;
import com.starksign.utils.SubResource;

import java.util.HashMap;
import java.util.Map;


public final class SignatureRequest extends SubResource {
    /**
     * SignatureRequest object
     * <p>
     * SignatureRequests are received when a signer with the "server" method is called to sign a specific document.
     * You should use the signaturequest.parse() method safely verify if this is a legitimate request and then use its
     * information to sign the document, if adequate.
     * <p>
     * Parameters:
     * signerId [string]: ID of the document signer that has been requested. ex: "6785678567856785"
     * documentId [string]: ID of the document that is being signed. ex: "5678567856785678"
     * privateKey [string]: ECDSA private key generated specifically for the signer to sign this document. ex: "-----BEGIN EC PRIVATE KEY-----\nMHQCAQEEICldfevoktjOcGGbeLZFn4VjmQAI7H4A2o3XwI6nA1mtoAcGBSuBBAAK\noUQDQgAEb0YLOXkxyF266wSD/yA0NBKVclBuyBaIEsvYnT6MCUppngXUMgrzqA+A\nXgUSnsWcPSy+mhnDJF6qtEaXHyoidQ==\n-----END EC PRIVATE KEY-----"
     *
     */
    static ClassData data = new ClassData(SignatureRequest.class, "SignatureRequest");

    public String signerId;
    public String documentId;
    public String privateKey;

    /**
     * SignatureRequest object
     * <p>
     * SignatureRequests are received when a signer with the "server" method is called to sign a specific document.
     * You should use the signaturequest.parse() method safely verify if this is a legitimate request and then use its
     * information to sign the document, if adequate.
     * <p>
     * Parameters:
     * @param signerId [string]: ID of the document signer that has been requested. ex: "6785678567856785"
     * @param documentId [string]: ID of the document that is being signed. ex: "5678567856785678"
     * @param privateKey [string]: ECDSA private key generated specifically for the signer to sign this document. ex: "-----BEGIN EC PRIVATE KEY-----\nMHQCAQEEICldfevoktjOcGGbeLZFn4VjmQAI7H4A2o3XwI6nA1mtoAcGBSuBBAAK\noUQDQgAEb0YLOXkxyF266wSD/yA0NBKVclBuyBaIEsvYnT6MCUppngXUMgrzqA+A\nXgUSnsWcPSy+mhnDJF6qtEaXHyoidQ==\n-----END EC PRIVATE KEY-----"
     *
     * */
    public SignatureRequest(String signerId, String documentId, String privateKey
    ) {
        this.signerId = signerId;
        this.documentId = documentId;
        this.privateKey = privateKey;
    }

    /**
     * Signature object
     * <p>
     * Whenever a Document is signed by any of its parties, a document Signature object is registered.
     * When all signatures are received, the document status changes to "success".
     * <p>
     * Parameters:
     * signerId [string]: ID of the document signer that has created this Signature. ex: "6785678567856785"
     * name [string]: Document signer's name. ex: name="Edward Stark"
     * contact [string]: signer's contact information. ex: "tony@starkinfra.com"
     * signature [string]: base-64 ECDSA digital signature generated to sign the document. ex: "MEUCIQD6cymQq40/06XuIelkv2t9qd9rPACooRH8faCB8SuPIQIgOqIil/1Vm/jni8eTDsoO5ytdoDitZocm3KSLzUYHCrQ\u003d"
     * publicKey [string]: public key that was used to validate the signature against the HTML content of the document. ex: "-----BEGIN PUBLIC KEY-----\nMFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEgHEBU5JNNgoJ1pWNUaEM7PvRbDvvNw3W\n+rZPqVhor/2vEqB5+fpYjTQp3EdGlKtEtSizeHsL9Vwm5MSt3CQrzA\u003d\u003d\n-----END PUBLIC KEY-----"
     * ip [string]: IP that sent the signature to Stark Infra. ex: "2804:14c:6a:85d3:b8a3:ddb4:a4e9:e11e"
     * created [datetime.datetime]: creation datetime for the Signature. ex: datetime.datetime(2020, 3, 10, 10, 30, 0, 0)
     * @throws Exception error in the request
     */
    public SignatureRequest(Map<String, Object> data) throws Exception {
        HashMap<String, Object> dataCopy = new HashMap<>(data);

        this.signerId = null;
        this.documentId = null;
        this.privateKey = null;

        if (!dataCopy.isEmpty()) {
            throw new Exception("Unknown parameters used in constructor: [" + String.join(", ", dataCopy.keySet()) + "]");
        }
    }

    /**
     * Create a single verified SignatureRequest object from a content string
     * <p>
     * Create a single SignatureRequest object from a content string received from a handler listening at the request url.
     * If the provided digital signature does not check out with the StarkSign public key, a
     * starksign.error.InvalidSignatureError will be raised.
     * <p>
     * Parameters:
     * @param content [string]: response content from request received at user endpoint (not parsed)
     * @param signature [string]: base-64 digital signature received at response header "Digital-Signature"
     * <p>
     * Return:
     * @return Parsed SignatureRequest object
     * @throws Exception error in the request
     */
    public static<T extends SubResource> T parse(String content, String signature) throws Exception {
        return Parse.parseAndVerify(data, content, signature);
    }
}
