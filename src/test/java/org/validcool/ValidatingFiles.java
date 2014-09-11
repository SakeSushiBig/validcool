package org.validcool;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static org.validcool.Validations.validate;
import static org.validcool.Validations.validcoolConfig;
import static org.validcool.FileValidations.*;

public class ValidatingFiles {

    @BeforeClass
    public static void setupValidating() {
        validcoolConfig.startLogging();
    }

    private static Path tmp = get("/tmp");
    private static Path tmpTextFile = tmp.resolve("helloworld.txt");
    private static Path tmpSubdir = tmp.resolve("subdir");

    @BeforeClass
    public static void setupFiles() throws IOException {
        Files.createFile(tmpTextFile);
        Files.createDirectories(tmpSubdir);
    }

    @AfterClass
    public static void cleanupFiles() throws IOException {
        Files.deleteIfExists(tmpTextFile);
        Files.deleteIfExists(tmpSubdir);
    }

    @Test
    public void fileExists_win() {
        validate(tmp, fileExists());
        validate(tmpTextFile, fileExists());
    }

    @Test(expected = ValidationException.class)
    public void fileExists_fail() {
        validate(tmp.resolve("greenLanternCoreUnite"), fileExists());
    }

    @Test
    public void isFile_win() {
        validate(tmpTextFile, isFile());
    }

    @Test(expected = ValidationException.class)
    public void isFile_fail() {
        validate(tmp, isFile());
    }

    @Test
    public void isDirectory_win() {
        validate(tmp, isDirectory());
    }

    @Test(expected = ValidationException.class)
    public void isDirectory_fail() {
        validate(tmpTextFile, isDirectory());
    }

    @Test
    public void withinPath_win() {
        validate(tmpTextFile, withinPath(tmp));
    }

    @Test(expected = ValidationException.class)
    public void withinPath_fail() {
        validate(tmpTextFile, withinPath(tmpSubdir));
    }

}
