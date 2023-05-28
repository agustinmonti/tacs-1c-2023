package org.grupo.tacs.extras;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class Helper {
    private static final int ITERATIONS = 100000;
    private static final int SALT_LENGTH = 32;

    /**
     * El método {@code obtenerHash(String)} genera un hashCode con el algoritmo de cifrado SHA3.
     * @param password Es el password a cifrar.
     * @return hashCode.
     */
    public static String obtenerHash(String password){
        SHA3.DigestSHA3 codigoHashSHA3 = new SHA3.DigestSHA3(256);
        codigoHashSHA3.update(password.getBytes());
        return Hex.toHexString(codigoHashSHA3.digest());
    }
}
