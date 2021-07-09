package com.teufelsturm.tt_downloader3.repositories;

/**
 * Exception thrown when a requested item is missing from the repository.
 */
public class ItemMissingException extends RepositoryException {
    public ItemMissingException(String message) {
        super(message);
    }
}