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
     */
    T saveItem(T record);

    /**
     * Removes an individual record
     * @param record the record to remove
     */
    void removeItem(T record);

    /**
     * Retrieves an individual record based on the string ID
     * @param id the ID of the item to retrieve
     * @return the retrieved record
     */
    T getItem(int id);

    /**
     * Obtain a list of items
     * @return all items in the list
     */
    List<T> getmItems();

    /**
     * Obtain a list of items, with paging support.  If no more data is
     * available (start > length), then the return will be an empty list.
     * @param start the start index of the items to return
     * @param count the number of items to return
     * @return the requested items.
     */
    List<T> getItems(int start, int count);

    /**
     * Returns the number of items in the repository.
     * @return the number of items in the repository.
     */
    int getLength();
}