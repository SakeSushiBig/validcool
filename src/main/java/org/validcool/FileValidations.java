package org.validcool;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileValidations {

    public static <E extends Path> Validator<E> fileExists() {
        return new Validator<>(
                Files::exists,
                "file or directory exists",
                (E value) -> String.format("path \"%s\" does not exist", value)
        );
    }

    public static <E extends Path> Validator<E> isFile() {
        return new Validator<>(
                Files::isRegularFile,
                "is a regular file",
                (E value) -> String.format("\"%s\" is not a regular file", value)
        );
    }

    public static <E extends Path> Validator<E> isDirectory() {
        return new Validator<>(
                Files::isDirectory,
                "is a directory",
                (E value) -> String.format("\"%s\" is not a directory", value)
        );
    }

    public static <E extends Path> Validator<E> withinPath(Path parent) {
        return new Validator<>(
                (E value) -> value.startsWith(parent),
                String.format("is within path \"%s\"", parent),
                (E value) -> String.format("\"%s\" is not within \"%s\"", value, parent)
        );
    }

}
