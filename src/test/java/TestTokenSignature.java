import com.starksign.Document;
import com.starksign.Settings;
import org.junit.Test;

import java.util.HashMap;


public class TestTokenSignature {

    @Test
    public void testSuccess() throws Exception {
        Settings.environment = "sandbox";

        String token = "EzNahUcN";
        Document document = Document.get("0d9bf711fb804c448332c05dbb8e563d");

        HashMap<String, Document.Signer> signersByContact = new HashMap<>();
        for (Document.Signer signer : document.signers){
            signersByContact.put(signer.contact, signer);
        }
        Document.Signer signer = signersByContact.get("developers@starkbank.com");
        System.out.println(signer);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", document.id);
        params.put("content", document.content);
        params.put("signerId", signer.id);
        params.put("token", token);

        Document.Signature signature = Document.sign(params);

        System.out.println(signature);
    }
}
