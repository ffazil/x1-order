package com.tracebucket.x1.order;

import com.fasterxml.jackson.databind.Module;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ffazil
 * @since 04/03/16
 */
public class JacksonTestUtils extends JacksonCustomization {

    public static Set<Module> getModules() {
        return new HashSet<>(Arrays.asList(new X1OrderModule(), new MoneyModule()));
    }
}
