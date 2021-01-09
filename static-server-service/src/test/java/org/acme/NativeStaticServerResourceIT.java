package org.acme;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeStaticServerResourceIT extends StaticServerResourceTest {

    // Execute the same tests but in native mode.
}