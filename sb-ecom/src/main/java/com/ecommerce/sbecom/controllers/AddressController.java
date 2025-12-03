package com.ecommerce.sbecom.controllers;

import com.ecommerce.sbecom.models.User;
import com.ecommerce.sbecom.payload.AddressDTO;
import com.ecommerce.sbecom.services.AddressService;
import com.ecommerce.sbecom.utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddresses(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO newAddress = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<AddressDTO>(newAddress, HttpStatus.CREATED);
    }
}
