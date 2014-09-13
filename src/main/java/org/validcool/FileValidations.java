package org.validcool;

import java.nio.file.Files;
import java.nio.file.Path;

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
                (E value) -> value.startsWith(parent),
                String.format("\"${actual}\" is not within path \"%s\"", parent)
        );
    }

}
