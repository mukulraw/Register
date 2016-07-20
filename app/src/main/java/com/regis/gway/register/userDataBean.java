package com.regis.gway.register;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by hi on 7/12/2016.
 */

public class userDataBean implements ParentListItem{
    private String name , phone , address , date , userId;

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public List<?> getChildItemList() {
        return null;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
