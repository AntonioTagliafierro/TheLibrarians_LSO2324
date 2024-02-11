package com.project.thelibrarians_lso2324.daos;

import android.content.Context;

import com.project.thelibrarians_lso2324.model.BookBag;

public interface LendLeaseDaoInterface {

    public void pushBookBagToServer(BookBag cart, Context context);

    public void fetchLendLeaseFromServer(Context context);
}
