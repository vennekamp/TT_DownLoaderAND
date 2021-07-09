package com.teufelsturm.tt_downloader3.repositories;

import java.util.List;

/**
 * Definition of the Repository
 *
 * @param <T> The type of data that the repository holds
 */
public interface IRepository<T> {

    /**
     * Save an individual record
     * @param record the record to save
     * @return the updated record
     * @throws RepositoryException if the data cannot be saved.
     */
    T saveItem(T record) throws RepositoryException;

    /**
     * Removes an individual record
     * @param record the record to remove
     * @throws RepositoryException if the data cannot be removed.
     * @throws ItemMissingException if the data is not present.
     */
    void removeItem(T record) throws RepositoryException, ItemMissingException;

    /**
     * Retrieves an individual record based on the string ID
     * @param id the ID of the item to retrieve
     * @return the retrieved record
     * @throws RepositoryException if the data cannot be retrieved.
     * @throws ItemMissingException is the data is not present.
     */
    T getItem(int id) throws RepositoryException, ItemMissingException;

    /**
     * Obtain a list of items
     * @return all items in the list
     * @throws RepositoryException if the data cannot be retrieved.
     */
    List<T> getmItems() throws RepositoryException;

    /**
     * Obtain a list of items, with paging support.  If no more data is
     * available (start > length), then the return will be an empty list.
     * @param start the start index of the items to return
     * @param count the number of items to return
     * @return the requested items.
     * @throws RepositoryException if the data cannot be retrieved.
     */
    List<T> getItems(int start, int count) throws RepositoryException;

    /**
     * Returns the number of items in the repository.
     * @return the number of items in the repository.
     * @throws RepositoryException if the data cannot be retrieved.
     */
    int getLength() throws RepositoryException;
}