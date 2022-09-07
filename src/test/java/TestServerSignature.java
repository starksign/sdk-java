import com.starksign.Document;
import com.starksign.Settings;
import com.starksign.SignatureRequest;
import org.junit.Test;

import java.util.HashMap;


public class TestServerSignature {

    @Test
    public void testSuccess() throws Exception {
        Settings.environment = "sandbox";

        SignatureRequest signatureRequest = SignatureRequest.parse(
            "{\"documentId\": \"0d9bf711fb804c448332c05dbb8e563d\", \"privateKey\": \"\\n-----BEGIN EC PRIVATE KEY-----\\nMHQCAQEEIDLbDgqXe6FT/dGJHBZEGFf3w18nq/PfjdooqIB+YUBooAcGBSuBBAAK\\noUQDQgAEubA3Ij8VEXH0z6XvlYe6LOSYKrlAxnhYMSFAHRQq/Gszpt1JeTbPuM16\\nXUC+hXwiiZi9Ep7vLSo4dcP3vngSlA==\\n-----END EC PRIVATE KEY-----\\n\", \"signerId\": \"6713235394789376\"}",
            "MEUCIB1Q2aU3y/9ObiIB7oBEI/jk7vnGhdIwz6ygSmKzbPm9AiEAoQd0z88nSt9Fy19Az2SiRRjsUDwYJyTsv1cRDWinzL8="
        );
        System.out.println(signatureRequest);

        Document document = Document.get(signatureRequest.documentId);
        System.out.println(document);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", document.id);
        params.put("content", document.content);
        params.put("signerId", signatureRequest.signerId);
        params.put("privateKey", signatureRequest.privateKey);

        Document.Signature signature = Document.sign(params);

        System.out.println(signature);
    }
}
