package org.grupo.tacs;

import org.grupo.tacs.extras.Helper;
import org.grupo.tacs.model.Interaction;
import org.grupo.tacs.model.InteractionMethod;
import org.grupo.tacs.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    User user;
    private static final String HASHCODE_DE_PASSWORD_CORRECTO = "5464c64a7c1c8f0a05a8cd2382415898d3a2c5e7b2fc1c22cf30ac230b7801ab";
    @Before
    public void inicializar(){
        this.user = new User();
        this.user.setName("Bob");
        this.user.setPassword("Password123");
        this.user.setConfirmPassword("Password123");
    }
    @Test
    public void passwordTest(){
        Assert.assertTrue(this.user.passwordIguales());
        this.user.setConfirmPassword("WrongPassword123");
        Assert.assertFalse(this.user.passwordIguales());
    }

    @Test
    public void hashPasswordTest(){
        Assert.assertEquals(HASHCODE_DE_PASSWORD_CORRECTO,Helper.obtenerHash(this.user.getPassword()));
        this.user.setPassword("WrongPassword123");
        Assert.assertNotEquals(HASHCODE_DE_PASSWORD_CORRECTO,Helper.obtenerHash(this.user.getPassword()));
    }
}
