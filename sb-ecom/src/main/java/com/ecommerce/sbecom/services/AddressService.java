package com.ecommerce.sbecom.services;

import com.ecommerce.sbecom.models.User;
import com.ecommerce.sbecom.payload.AddressDTO;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);
}
