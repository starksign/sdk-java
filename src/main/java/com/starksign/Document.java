package com.starksign;

import com.starkbank.ellipticcurve.utils.BinaryAscii;
import com.starkbank.ellipticcurve.PrivateKey;
import com.starkbank.ellipticcurve.Ecdsa;
import com.starkbank.ellipticcurve.Curve;
import com.starksign.utils.SubResource;
import com.starksign.utils.Resource;
import com.starksign.utils.Rest;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Document extends Resource {
    /**
     * Document object
     * <p>
     * Documents represent the contracts that should be signed by all parties.
     * <p>
     * Parameters:
     * content    [string]: HTML content of the document. This is also the message that should be signed by the provided ECDSA private key.
     * status     [string]: Document status. ex: "pending", "success", "canceled" or "expired"
     * signers    [list of Document.Signers or HashMap]: list with parties that are or were expected to sign the contract.
     * signatures [list of Document.Signatures or HashMap]: list with current Signatures the contract has received.
     */
    static ClassData data = new ClassData(Document.class, "Document");

    public String content;
    public String status;
    public List<Signer> signers;
    public List<Signature> signatures;

    /**
     * Document object
     * <p>
     * Documents represent the contracts that should be signed by all parties.
     * <p>
     * Parameters:
     * @param content    [string]: HTML content of the document. This is also the message that should be signed by the provided ECDSA private key.
     * @param status     [string]: Document status. ex: "pending", "success", "canceled" or "expired"
     * @param signers    [list of Document.Signers or HashMap]: list with parties that are or were expected to sign the contract.
     * @param signatures [list of Document.Signatures or HashMap]: list with current Signatures the contract has received.
     */
    public Document(String content, String status, List<Signer> signers, List<Signature> signatures, String id) {
        super(id);
        this.content = content;
        this.status = status;
        this.signers = signers;
        this.signatures = signatures;
    }

    /**
     * Document object
     * <p>
     * Documents represent the contracts that should be signed by all parties.
     * <p>
     * Parameters:
     * content [string]: HTML content of the document. This is also the message that should be signed by the provided ECDSA private key.
     * status [string]: Document status. ex: "pending", "success", "canceled" or "expired"
     * signers [list of document.Signers or HashMap]: list with parties that are or were expected to sign the contract.
     * signatures [list of document.Signatures or HashMap]: list with current Signatures the contract has received.
     * @throws Exception error in the request
     */
    public Document(Map<String, Object> data) throws Exception {
        super(null);
        HashMap<String, Object> dataCopy = new HashMap<>(data);

        this.id = (String) dataCopy.remove("id");
        this.content = (String) dataCopy.remove("content");
        this.status = (String) dataCopy.remove("status");;
        this.signers = Signer.parseSigners((List<Object>) dataCopy.remove("signers"));
        this.signatures = Signature.parseSignatures((List<Object>) dataCopy.remove("signatures"));

        if (!dataCopy.isEmpty()) {
            throw new Exception("Unknown parameters used in constructor: [" + String.join(", ", dataCopy.keySet()) + "]");
        }
    }

    /**
     * Retrieve a specific Document
     * <p>
     * Receive a single Document object previously created in the Stark Sign API by its id
     * <p>
     * Parameters:
     * @param id [string]: object unique id. ex: "d186044b38be41598aaccfc5770b991a"
     * <p>
     * Return:
     * @return Document object with updated attributes
     * @throws Exception error in the request
     */
    public static Document get(String id) throws Exception {
        return Rest.getId(data, id, new HashMap<>());
    }

    /**
     * Sign a specific Document
     * <p>
     * Add a Signer's Signature to a specific document. Either a private_key or a token must be informed.
     * <p>
     * Parameters:
     * @param data parameters to sign a document
     * Parameters (required):
     * id       [string]: ID of the Document that is being signed. ex: "d186044b38be41598aaccfc5770b991a"
     * content  [string]: HTML content of the document that is being signed.
     * signerId [string]: ID of the document Signer that is creating the Signature. ex: "6785678567856785"
     * <p>
     * Parameters (conditionally-required):
     * privateKey [string]: Private key PEM content that was received on the registered endpoint. Only valid for "server" signatures. ex: "-----BEGIN EC PRIVATE KEY-----\nMHQCAQEEICldfevoktjOcGGbeLZFn4VjmQAI7H4A2o3XwI6nA1mtoAcGBSuBBAAK\noUQDQgAEb0YLOXkxyF266wSD/yA0NBKVclBuyBaIEsvYnT6MCUppngXUMgrzqA+A\nXgUSnsWcPSy+mhnDJF6qtEaXHyoidQ==\n-----END EC PRIVATE KEY-----"
     * token      [string]: Token received via email, SMS, etc. by a non-server signer. ex: "a8B1kxJ"
     * Return:
     * @return Signature object
     * @throws Exception error in the request
     */
    public static Signature sign(HashMap<String, Object> data) throws Exception {
        String documentId = (String) data.get("id");
        SignData signData = new SignData(
            documentId,
            (String) data.get("content"),
            (String) data.get("signerId"),
            (String) data.get("privateKey"),
            (String) data.get("token")
        );
        return Rest.postSubResource(Document.data, documentId, Signature.data, signData);
    }

    private final static class SignData extends SubResource {

        public String signerId;
        public String signature;

        private SignData(String id, String content, String signerId, String privateKey, String token)
                throws NoSuchAlgorithmException {
            this.signerId = signerId;
            if (privateKey != null) {
                PrivateKey privateKeyObject = PrivateKey.fromPem(privateKey);
                this.signature = Ecdsa.sign(content, privateKeyObject).toBase64();
            }
            if (privateKey == null) {
                String message = id + ":" + signerId + ":" + token;
                byte[] hashMessage = MessageDigest.getInstance("SHA-256").digest(message.getBytes());
                BigInteger numberMessage = BinaryAscii.numberFromString(hashMessage);
                PrivateKey privateKeyObject = new PrivateKey(Curve.secp256k1, numberMessage);
                this.signature = Ecdsa.sign(content, privateKeyObject).toBase64();
            }
        }
    }

    public static final class Signer extends Resource {
        /**
         * Signer object
         * <p>
         * Signers represent each of the parties that are expected to sign a document.
         * <p>
         * Parameters:
         * name       [string]: Signer's name. ex: Jon Ygritte
         * contact    [string]: Signer's contact information. ex: "jon@starksign.com"
         * method     [string]: Signer's signature method. ex: "server", "token" or "link"
         * isSent     [bool]: If true, the signer has been notified about the signature request. ex: True
         * status     [string]: Signer status. ex: "pending", "success" or "canceled"
         * documentId [string]: ID of the Document that should be signed. ex: "6785678567856785"
         * tags       [list of strings, default []]: list of strings for reference when searching for the Signer. ex: tags=["always-on-time"]
         * created    [datetime.datetime]: creation datetime of the Signer. ex: datetime.datetime(2020, 3, 10, 10, 30, 0, 0)
         * updated    [datetime.datetime]: latest update datetime for the Signer. ex: datetime.datetime(2020, 3, 10, 10, 30, 0, 0)
         */
        ClassData data = new ClassData(Signer.class, "Signer");

        public String name;
        public String contact;
        public String method;
        public String isSent;
        public String status;
        public String documentId;
        public List<String> tags;
        public String created;
        public String updated;

        /**
         * Signer object
         * <p>
         * Signers represent each of the parties that are expected to sign a document.
         * <p>
         * Parameters:
         * @param name [string]: Signer's name. ex: Jon Ygritte
         * @param contact [string]: Signer's contact information. ex: "jon@starksign.com"
         * @param method [string]: Signer's signature method. ex: "server", "token" or "link"
         * @param isSent [bool]: If True, the signer has been notified about the signature request. ex: True
         * @param status [string]: Signer status. ex: "pending", "success" or "canceled"
         * @param documentId [string]: ID of the Document that should be signed. ex: "6785678567856785"
         * @param tags [list of strings, default []]: list of strings for reference when searching for the Signer. ex: tags=["always-on-time"]
         * @param created [datetime.datetime]: creation datetime of the Signer. ex: datetime.datetime(2020, 3, 10, 10, 30, 0, 0)
         * @param updated [datetime.datetime]: latest update datetime for the Signer. ex: datetime.datetime(2020, 3, 10, 10, 30, 0, 0)
         *
         */
        public Signer(String name, String contact, String method, String isSent, String status,
                      String documentId, List<String> tags, String created, String updated, String id
        ) {
            super(id);
            this.name = name;
            this.contact = contact;
            this.method = method;
            this.isSent = isSent;
            this.status = status;
            this.documentId = documentId;
            this.tags = tags;
            this.created = created;
            this.updated = updated;
        }

        /**
         * Signer object
         * <p>
         * Signers represent each of the parties that are expected to sign a document.
         * <p>
         * Parameters:
         * content [string]: HTML content of the document. This is also the message that should be signed by the provided ECDSA private key.
         * status [string]: Document status. ex: "pending", "success", "canceled" or "expired"
         * signers [list of document.Signers]: list with parties that are or were expected to sign the contract.
         * signatures [list of document.Signatures]: list with current Signatures the contract has received.
         * @throws Exception error in the request
         */
        public Signer(Map<String, Object> data) throws Exception {
            super(null);
            HashMap<String, Object> dataCopy = new HashMap<>(data);

            this.name = null;
            this.contact = null;
            this.method = null;
            this.isSent = null;
            this.status = null;
            this.documentId = null;
            this.tags = null;
            this.created = null;
            this.updated = null;

            if (!dataCopy.isEmpty()) {
                throw new Exception("Unknown parameters used in constructor: [" + String.join(", ", dataCopy.keySet()) + "]");
            }
        }

        @SuppressWarnings("unchecked")
        static List<Signer> parseSigners(List<Object> signers) throws Exception {
            if (signers == null)
                return null;

            List<Signer> parsed = new ArrayList<>();
            if (signers.size() == 0 || signers.get(0) instanceof Signer) {
                for (Object signer : signers) {
                    parsed.add((Signer) signer);
                }
                return parsed;
            }

            for (Object signer : signers) {
                Signer signerObject = new Signer((Map<String, Object>) signer);
                parsed.add(signerObject);
            }

            return parsed;
        }
    }

    public static final class Signature extends SubResource {
        /**
         * Signature object
         * <p>
         * Whenever a Document is signed by any of its Signers, a document Signature object is registered.
         * When all Signatures are received, the Document status changes to "success".
         * <p>
         * Parameters:
         * signerId  [string]: ID of the document signer that has created this Signature. ex: "6785678567856785"
         * name      [string]: Document signer's name. ex: name="Edward Stark"
         * contact   [string]: signer's contact information. ex: "tony@starkinfra.com"
         * signature [string]: base-64 ECDSA digital signature generated to sign the document. ex: "MEUCIQD6cymQq40/06XuIelkv2t9qd9rPACooRH8faCB8SuPIQIgOqIil/1Vm/jni8eTDsoO5ytdoDitZocm3KSLzUYHCrQ\u003d"
         * publicKey [string]: public key that was used to validate the signature against the HTML content of the document. ex: "-----BEGIN PUBLIC KEY-----\nMFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEgHEBU5JNNgoJ1pWNUaEM7PvRbDvvNw3W\n+rZPqVhor/2vEqB5+fpYjTQp3EdGlKtEtSizeHsL9Vwm5MSt3CQrzA\u003d\u003d\n-----END PUBLIC KEY-----"
         * ip        [string]: IP that sent the signature to Stark Infra. ex: "2804:14c:6a:85d3:b8a3:ddb4:a4e9:e11e"
         * created   [datetime.datetime]: creation datetime for the Signature. ex: datetime.datetime(2020, 3, 10, 10, 30, 0, 0)
         */
        static ClassData data = new ClassData(Signature.class, "Signature");

        public String signerId;
        public String name;
        public String contact;
        public String signature;
        public String publicKey;
        public String ip;
        public String created;

        /**
         * Signature object
         * <p>
         * Whenever a Document is signed by any of its Signers, a document Signature object is registered.
         * When all Signatures are received, the Document status changes to "success".
         * <p>
         * Parameters:
         * @param signerId [string]: ID of the document signer that has created this Signature. ex: "6785678567856785"
         * @param name [string]: Document signer's name. ex: name="Edward Stark"
         * @param contact [string]: signer's contact information. ex: "tony@starkinfra.com"
         * @param signature [string]: base-64 ECDSA digital signature generated to sign the document. ex: "MEUCIQD6cymQq40/06XuIelkv2t9qd9rPACooRH8faCB8SuPIQIgOqIil/1Vm/jni8eTDsoO5ytdoDitZocm3KSLzUYHCrQ\u003d"
         * @param publicKey [string]: public key that was used to validate the signature against the HTML content of the document. ex: "-----BEGIN PUBLIC KEY-----\nMFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEgHEBU5JNNgoJ1pWNUaEM7PvRbDvvNw3W\n+rZPqVhor/2vEqB5+fpYjTQp3EdGlKtEtSizeHsL9Vwm5MSt3CQrzA\u003d\u003d\n-----END PUBLIC KEY-----"
         * @param ip [string]: IP that sent the signature to Stark Infra. ex: "2804:14c:6a:85d3:b8a3:ddb4:a4e9:e11e"
         * @param created [datetime.datetime]: creation datetime for the Signature. ex: datetime.datetime(2020, 3, 10, 10, 30, 0, 0)
         */
        public Signature(String signerId, String name, String contact, String signature, String publicKey,
                         String ip, String created
        ) {
            this.signerId = signerId;
            this.name = name;
            this.contact = contact;
            this.signature = signature;
            this.publicKey = publicKey;
            this.ip = ip;
            this.created = created;
        }

        /**
         * Signature object
         * <p>
         * Whenever a Document is signed by any of its Signers, a document Signature object is registered.
         * When all Signatures are received, the Document status changes to "success".
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
        public Signature(Map<String, Object> data) throws Exception {
            HashMap<String, Object> dataCopy = new HashMap<>(data);

            this.signerId = null;
            this.name = null;
            this.contact = null;
            this.signature = null;
            this.publicKey = null;
            this.ip = null;
            this.created = null;

            if (!dataCopy.isEmpty()) {
                throw new Exception("Unknown parameters used in constructor: [" + String.join(", ", dataCopy.keySet()) + "]");
            }
        }

        @SuppressWarnings("unchecked")
        static List<Signature> parseSignatures(List<Object> signatures) throws Exception {
            if (signatures == null)
                return null;

            List<Signature> parsed = new ArrayList<>();
            if (signatures.size() == 0 || signatures.get(0) instanceof Signature) {
                for (Object signature : signatures) {
                    parsed.add((Signature) signature);
                }
                return parsed;
            }

            for (Object signature : signatures) {
                Signature signatureObject = new Signature((Map<String, Object>) signature);
                parsed.add(signatureObject);
            }

            return parsed;
        }
    }
}
