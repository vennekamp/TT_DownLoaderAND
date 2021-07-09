package com.teufelsturm.tt_downloader3.repositories;

/**
 * Main exception class for the repository.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}