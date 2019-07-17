package top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.encrypt;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class SHA1UtilTest {

    @Test
    public void encrypt() throws NoSuchAlgorithmException {
        Assert.assertEquals("74bfa73ebeacd499113998cd67ab593e57e9be6b", SHA1Util.encrypt("13103511151562827868ArvinLee"));
    }
}