package com.dleague.lakeshoreimporters.networkcalls;

import android.content.Context;

import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.ApplyDiscountMutation;
import com.dleague.lakeshoreimporters.CreateCheckoutMutation;
import com.dleague.lakeshoreimporters.CreateCustomerAddressMutation;
import com.dleague.lakeshoreimporters.CreateUserAccessTokenMutation;
import com.dleague.lakeshoreimporters.CustomerRecoverMutation;
import com.dleague.lakeshoreimporters.DeleteCustomerAddressMutation;
import com.dleague.lakeshoreimporters.GetCollectionsByIdNextQuery;
import com.dleague.lakeshoreimporters.GetCollectionsByIdQuery;
import com.dleague.lakeshoreimporters.GetCustomerAddressesQuery;
import com.dleague.lakeshoreimporters.GetCustomerQuery;
import com.dleague.lakeshoreimporters.GetLastOrdersQuery;
import com.dleague.lakeshoreimporters.GetNextProductsQuery;
import com.dleague.lakeshoreimporters.GetProductByHandleQuery;
import com.dleague.lakeshoreimporters.GetProductsQuery;
import com.dleague.lakeshoreimporters.RegisterUserMutation;
import com.dleague.lakeshoreimporters.UpdateCustomerAddressMutation;
import com.dleague.lakeshoreimporters.UpdateCustomerMutation;
import com.dleague.lakeshoreimporters.UpdateDefaultCustomerAddressMutation;
import com.dleague.lakeshoreimporters.activities.AppSpace;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.dtos.UserDTO;
import com.dleague.lakeshoreimporters.type.CheckoutCreateInput;
import com.dleague.lakeshoreimporters.type.CheckoutLineItemInput;
import com.dleague.lakeshoreimporters.type.CustomerAccessTokenCreateInput;
import com.dleague.lakeshoreimporters.type.CustomerCreateInput;
import com.dleague.lakeshoreimporters.type.CustomerUpdateInput;
import com.dleague.lakeshoreimporters.type.MailingAddressInput;
import com.dleague.lakeshoreimporters.utils.Constants;

import static com.dleague.lakeshoreimporters.utils.Constants.APPLY_DISCOUNT;
import static com.dleague.lakeshoreimporters.utils.Constants.CREATE_ADDRESS;
import static com.dleague.lakeshoreimporters.utils.Constants.CREATE_CHECKOUT;
import static com.dleague.lakeshoreimporters.utils.Constants.CREATE_CUSTOMER;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_RECOVER;
import static com.dleague.lakeshoreimporters.utils.Constants.DELETE_ADDRESS;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_COLLECTION_BY_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_COLLECTION_BY_ID_NEXT;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_CUSTOMER;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_CUSTOMER_ADDRESSES;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_NEXT_PRODUCTS;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_PRODUCTS;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_PRODUCT_BY_HANDLE;
import static com.dleague.lakeshoreimporters.utils.Constants.LAST_ORDERS;
import static com.dleague.lakeshoreimporters.utils.Constants.LOGIN_CUSTOMER;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;
import static com.dleague.lakeshoreimporters.utils.Constants.SET_DEFAULT_ADDRESS;
import static com.dleague.lakeshoreimporters.utils.Constants.UPDATE_ADDRESS;
import static com.dleague.lakeshoreimporters.utils.Constants.UPDATE_CUSTOMER;

public class NetworkCalls extends BaseServiceImpl {
    private Context context;
    private NetworkCallbacks networkCallbacks ;

    public NetworkCalls(Context context, NetworkCallbacks networkCallbacks) {
        super(context);
        this.context = context;
        this.networkCallbacks = networkCallbacks;
    }

    ///////////////// QUERY CALLS ///////////////////////////////////

    public void getProducts(int productsCount){
        GetProductsQuery query = GetProductsQuery.builder()
                .first(productsCount)
                .build();
        baseServiceQueryCall(query, networkCallbacks, GET_PRODUCTS);
    }

    public void getNextProducts(int productsCount, String lastCursor){
        GetNextProductsQuery query = GetNextProductsQuery.builder()
                .first(productsCount)
                .after(lastCursor)
                .build();
        baseServiceQueryCall(query, networkCallbacks, GET_NEXT_PRODUCTS);
    }

    public void getFirstCollectionsById(String handle, int productsCount){
        GetCollectionsByIdQuery query = GetCollectionsByIdQuery.builder()
                .handle(handle)
                .productsCount(productsCount)
                .build();
        baseServiceQueryCall(query, networkCallbacks, GET_COLLECTION_BY_ID);
    }

    public void getNextCollectionsById(String handle, int productsCount, String lastCursor){
        GetCollectionsByIdNextQuery query = GetCollectionsByIdNextQuery.builder()
                .handle(handle)
                .productsCount(productsCount)
                .after(lastCursor)
                .build();
        baseServiceQueryCall(query, networkCallbacks, GET_COLLECTION_BY_ID_NEXT);
    }

    public void getCustomerAddresses(String id){
        GetCustomerAddressesQuery query = GetCustomerAddressesQuery.builder()
                .id(id)
                .build();
        baseServiceQueryCall(query, networkCallbacks, GET_CUSTOMER_ADDRESSES);
    }

    public void getLastOrders(String id){
        GetLastOrdersQuery query = GetLastOrdersQuery.builder()
                .id(id)
                .build();
        baseServiceQueryCall(query, networkCallbacks, LAST_ORDERS);
    }


    public void getCustomer(String id){
        GetCustomerQuery query = GetCustomerQuery.builder()
                .id(id)
                .build();
        baseServiceQueryCall(query, networkCallbacks, GET_CUSTOMER);
    }

    public void getProductByHandle(String handle){
            GetProductByHandleQuery query = GetProductByHandleQuery.builder()
                    .handle(handle)
                    .build();
            baseServiceQueryCall(query, networkCallbacks, GET_PRODUCT_BY_HANDLE);
        }

    ///////////////////////////////////////////////////////////////////

    ///////////////////// MUTATION CALLS /////////////////////////////

    public void createUser(UserDTO userDTO){
        CustomerCreateInput input = CustomerCreateInput.builder()
                .email(userDTO.email)
                .password(userDTO.password)
                .firstName(userDTO.firstName)
                .lastName(userDTO.lastName)
                .build();
        RegisterUserMutation mutation = RegisterUserMutation.builder()
                .input(input)
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, CREATE_CUSTOMER);
    }

    public void loginUser(UserDTO userDTO){
        CustomerAccessTokenCreateInput input = CustomerAccessTokenCreateInput.builder()
                .email(userDTO.email)
                .password(userDTO.password)
                .build();
        CreateUserAccessTokenMutation mutation = CreateUserAccessTokenMutation.builder()
                .input(input).build();
        baseServiceMutationCall(mutation, networkCallbacks, LOGIN_CUSTOMER);
    }

    public void createAddress(AddressDTO addressDTO){
        MailingAddressInput input = MailingAddressInput.builder()
                .firstName(addressDTO.getFirstName())
                .lastName(addressDTO.getLastName())
                .company(addressDTO.getCompany())
                .address1(addressDTO.getAddress1())
                .address2(addressDTO.getAddress2())
                .city(addressDTO.getCity())
                .province(addressDTO.getState())
                .country(addressDTO.getCountry())
                .zip(addressDTO.getPostalCode())
                .phone(addressDTO.getPhone())
                .build();
        CreateCustomerAddressMutation mutation = CreateCustomerAddressMutation.builder()
                .address(input)
                .token(AppSpace.sharedPref.readValue(SESSION, "0"))
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, CREATE_ADDRESS);
    }

    public void setDefaultCustomerAddress(String addressID){

        UpdateDefaultCustomerAddressMutation mutation = UpdateDefaultCustomerAddressMutation.builder()
                .address(addressID)
                .token(AppSpace.sharedPref.readValue(SESSION, "0"))
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, SET_DEFAULT_ADDRESS);
    }

    public void deleteCustomerAddress(String addressID){

        DeleteCustomerAddressMutation mutation = DeleteCustomerAddressMutation.builder()
                .id(addressID)
                .token(AppSpace.sharedPref.readValue(SESSION, "0"))
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, DELETE_ADDRESS);
    }

    public void updateCustomerAddress(AddressDTO addressDTO){
        MailingAddressInput input = MailingAddressInput.builder()
                .firstName(addressDTO.getFirstName())
                .lastName(addressDTO.getLastName())
                .company(addressDTO.getCompany())
                .address1(addressDTO.getAddress1())
                .address2(addressDTO.getAddress2())
                .city(addressDTO.getCity())
                .province(addressDTO.getState())
                .country(addressDTO.getCountry())
                .zip(addressDTO.getPostalCode())
                .phone(addressDTO.getPhone())
                .build();
        UpdateCustomerAddressMutation mutation = UpdateCustomerAddressMutation.builder()
                .id(addressDTO.getAddressId())
                .address(input)
                .token(AppSpace.sharedPref.readValue(SESSION, "0"))
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, UPDATE_ADDRESS);
    }

    public void createCheckoout(CheckoutCreateInput checkoutData){
        CheckoutCreateInput input = CheckoutCreateInput.builder()
                .email(checkoutData.email())
                .lineItems(checkoutData.lineItems())
                .shippingAddress(checkoutData.shippingAddress())
                .note(checkoutData.note())
                .allowPartialAddresses(checkoutData.allowPartialAddresses())
                .build();

        CreateCheckoutMutation mutation = CreateCheckoutMutation.builder()
                .input(input)
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, CREATE_CHECKOUT);
    }

    public void applyDiscount(String checkoutID, String discountCode){
        ApplyDiscountMutation mutation = ApplyDiscountMutation.builder()
                .checkoutId(checkoutID)
                .discountCode(discountCode)
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, APPLY_DISCOUNT);
    }

    public void updateCustomer(String userId, CustomerUpdateInput input){
        UpdateCustomerMutation mutation = UpdateCustomerMutation.builder()
                .id(userId)
                .input(input)
                .build();

        baseServiceMutationCall(mutation, networkCallbacks, UPDATE_CUSTOMER);
    }

    public void recoverCustomer(String email){
        CustomerRecoverMutation mutation = CustomerRecoverMutation.builder()
                .email(email).build();
        baseServiceMutationCall(mutation, networkCallbacks, CUSTOMER_RECOVER);
    }

    ///////////////////////////////////////////////////////////////////
}