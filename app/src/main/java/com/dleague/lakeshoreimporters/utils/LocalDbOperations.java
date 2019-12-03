package com.dleague.lakeshoreimporters.utils;

import android.content.Context;

import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.dtos.CartDTO;
import com.dleague.lakeshoreimporters.dtos.UserDTO;

import java.util.List;

public class LocalDbOperations {

    private AppDatabase dataBase;


    public LocalDbOperations(Context context) {
        dataBase = AppDatabase.getAppDatabase(context);
    }

    //////////User Operations//////////////
    public void storeUserData(UserDTO obj) {
        if (dataBase == null)
            return;
        dataBase.userDTOInterface().insert(obj);
    }

    public UserDTO getUser() {
        if (dataBase == null)
            return null;
        return dataBase.userDTOInterface().load();
    }

    public void deleteUser(String email) {
        if (dataBase == null)
            return;
        else
            dataBase.userDTOInterface().delete(email);
        return;
    }

    /////////ADDRESS DTO//////////////////

    public void StoreAddress(AddressDTO obj) {
        if (dataBase == null)
            return;
        dataBase.addressDTOInterface().insert(obj);
    }

    public List<AddressDTO> getAllAddresses() {
        if (dataBase == null)
            return null;
        return dataBase.addressDTOInterface().loadAll();
    }

    public AddressDTO getAddress(String addressId) {
        if (dataBase == null)
            return null;
        return dataBase.addressDTOInterface().load(addressId);
    }

    public void deleteAddress(String addressId) {
        if (dataBase == null)
            return;
        dataBase.addressDTOInterface().delete(addressId);
        return;
    }

    public void deleteAllAddress() {
        if (dataBase == null)
            return;
        dataBase.addressDTOInterface().deleteAll();
        return;
    }

    /////////CART DTO//////////////////

    public void storeCartItem(CartDTO obj) {
        if (dataBase == null)
            return;
        dataBase.cartDTOInterface().insert(obj);
    }

    public List<CartDTO> getAllCartItems() {
        if (dataBase == null)
            return null;
        return dataBase.cartDTOInterface().loadAll();
    }

    public CartDTO getCartItem(String cursor) {
        if (dataBase == null)
            return null;
        return dataBase.cartDTOInterface().load(cursor);
    }

    public void deleteCartItem(String cursor) {
        if (dataBase == null)
            return;
        dataBase.cartDTOInterface().delete(cursor);
        return;
    }

    public void deleteAllCartItems() {
        if (dataBase == null)
            return;
        dataBase.cartDTOInterface().deleteAll();
        return;
    }
}
