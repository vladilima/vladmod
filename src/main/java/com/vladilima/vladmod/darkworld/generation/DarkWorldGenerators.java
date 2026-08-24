package com.vladilima.vladmod.darkworld.generation;

import com.vladilima.vladmod.darkworld.generation.generators.CliffsGenerator;
import com.vladilima.vladmod.darkworld.generation.generators.Generator;

import java.util.HashMap;

public class DarkWorldGenerators {
    public static HashMap<String, Generator> GENERATORS = new HashMap<>();

    public static void registerGenerators() {
        GENERATORS.put("cliffs", new CliffsGenerator());
    }
}
