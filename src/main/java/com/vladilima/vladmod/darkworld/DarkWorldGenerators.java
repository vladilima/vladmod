package com.vladilima.vladmod.darkworld;

import com.vladilima.vladmod.darkworld.generators.CliffsGenerator;
import com.vladilima.vladmod.darkworld.generators.Generator;

import java.util.HashMap;

public class DarkWorldGenerators {
    public static HashMap<String, Generator> GENERATORS = new HashMap<>();

    public static void registerGenerators() {
        GENERATORS.put("cliffs", new CliffsGenerator());
    }
}
