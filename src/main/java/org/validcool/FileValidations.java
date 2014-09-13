package org.validcool;

import org.validcool.validators.IoValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import static java.nio.file.StandardOpenOption.READ;

public class FileValidations {

    public static <E extends Path> Validator<E> fileExists() {
        return new Validator<>(
                Files::exists,
                "file or directory \"${actual}\" does not exist"
        );
    }

    public static <E extends Path> Validator<E> isFile() {
        return new Validator<>(
                Files::isRegularFile,
                "\"${actual}\" is not a regular file"
        );
    }

    public static <E extends Path> Validator<E> isDirectory() {
        return new Validator<>(
                Files::isDirectory,
                "\"${actual}\" is not a directory"
        );
    }

    public static <E extends Path> Validator<E> withinPath(Path parent) {
        return new Validator<>(
                val -> val.startsWith(parent),
                String.format("\"${actual}\" is not within path \"%s\"", parent)
        );
    }

    public static <E extends Path> Validator<E> sameContent(String content) {
        return new IoValidator<>(
                val -> new String(Files.readAllBytes(val)).equals(content),
                String.format("${actual} does not have this content \"%s\"", content)
        );
    }

    public static <E extends Path> Validator<E> sameContent(InputStream stream) {
        return new IoValidator<>(
                val -> compareLargeFiles(Files.newInputStream(val, READ), stream, 4096),
                "${actual} does not contain the expected content"
        );
    }

    public static <E extends Path> Validator<E> sameContent(InputStream stream, int bufferSize) {
        return new IoValidator<>(
                val -> compareLargeFiles(Files.newInputStream(val, READ), stream, bufferSize),
                "${actual} does not contain the expected content"
        );
    }

    private static boolean compareLargeFiles(InputStream one, InputStream two, int bufferSize) throws IOException {
        byte[] bufferOne = new byte[bufferSize], bufferTwo = new byte[bufferSize];
        while(true) {
            boolean oneEOF = one.read(bufferOne) == -1,
                    twoEOF = two.read(bufferTwo) == -1;
            if(oneEOF || twoEOF) {
                // are both done?
                return oneEOF == twoEOF;
            } else if(!Arrays.equals(bufferOne, bufferTwo)) {
                return false;
            }
        }
    }

}
