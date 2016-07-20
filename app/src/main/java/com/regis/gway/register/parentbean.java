package com.regis.gway.register;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by hi on 7/14/2016.
 */

public class parentbean implements ParentListItem {

    List<userDataBean> list;

    public parentbean(List<userDataBean> list)
    {
        this.list = list;
    }

    @Override
    public List<userDataBean> getChildItemList() {
        return list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
