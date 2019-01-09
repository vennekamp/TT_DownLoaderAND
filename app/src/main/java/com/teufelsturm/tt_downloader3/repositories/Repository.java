package com.teufelsturm.tt_downloader3.repositories;

import android.content.Context;


import com.teufelsturm.tt_downloader3.model.BaseModel;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;

import java.util.ArrayList;
import java.util.List;

/**
 * An in-memory repository.  The data goes away when the app is restarted
 *
 * @param <T> the mType that the repository holds.
 */
public class Repository<T extends BaseModel> implements IRepository<T> {
    private List<T> mItems = new ArrayList<>();
    private Context mContext;
    private BaseModel.Type mType;

    public Repository(Context context, BaseModel.Type mType) {
        this.mContext = context;
        this.mType = mType;
    }

    @Override
    public T saveItem(T record) throws RepositoryException {
        /* Update the record so that the updated value is set */
        record.setUpdated(System.currentTimeMillis());
        int index = mItems.indexOf(record);
        if (index == -1) {
            mItems.add(record);
        } else {
            mItems.set(index, record);
        }
        return record;
    }

    @Override
    public void removeItem(T record) throws RepositoryException {
        if (!mItems.remove(record)) {
            throw new ItemMissingException(String.format("Item %s does not exist", record.getIntTT_IDOrdinal()));
        }
    }


    /**
     * Retrieves an individual record based on the string ID
     * @param id the ID of the item to retrieve
     * @return the retrieved record
     * @throws RepositoryException if the data cannot be retrieved.
     * @throws ItemMissingException is the data is not present.
     */
    @Override
    public T getItem(int id) throws RepositoryException {
        for (T item : mItems) {
            if (item.getIntTT_IDOrdinal().equals(id)) {
                return item;
            }
        }

        switch (mType) {
            case SUMMIT:
                T tt_summit_and = (T) new TT_Summit_AND(mContext, id);        // TT_Summit_AND
                mItems.add(tt_summit_and);
                return  tt_summit_and;
            case ROUTE:
                T tt_route_and = (T) new TT_Route_AND(mContext, id);          // TT_Route_AND
                mItems.add(tt_route_and);
                return tt_route_and;
            case COMMEMT:
            default:
                throw new ItemMissingException(String.format("Item %s does not exist: Should never happer have you forgetten to save the comment of the route here?", id));
        }
    }

    /**
     * Obtain a list of mItems
     * @return all mItems in the list
     * @throws RepositoryException if the data cannot be retrieved.
     */
    @Override
    public List<T> getmItems() throws RepositoryException {
        return mItems;
    }

    /**
     * Obtain a list of mItems, with paging support.  If no more data is
     * available (start > length), then the return will be an empty list.
     * @param start the start index of the mItems to return
     * @param count the number of mItems to return
     * @return the requested mItems.
     * @throws RepositoryException if the data cannot be retrieved.
     */
    @Override
    public List<T> getItems(int start, int count) throws RepositoryException {
        if (start > mItems.size()) {
            return new ArrayList<>();
        }
        int endIndex = (start + count > mItems.size()) ? mItems.size() : start + count;
        return mItems.subList(start, endIndex);
    }

    /**
     * Returns the number of mItems in the repository.
     * @return the number of mItems in the repository.
     * @throws RepositoryException if the data cannot be retrieved.
     */
    @Override
    public int getLength() throws RepositoryException {
        return mItems.size();
    }
}