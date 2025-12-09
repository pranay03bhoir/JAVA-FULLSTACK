package com.ecommerce.sbecom.controllers;

import com.ecommerce.sbecom.models.User;
import com.ecommerce.sbecom.payload.AddressDTO;
import com.ecommerce.sbecom.services.AddressService;
import com.ecommerce.sbecom.utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressList = addressService.getAllAddresses();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.FOUND);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressByUser() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTO = addressService.getAddressByUser(user);
        return new ResponseEntity<>(addressDTO, HttpStatus.FOUND);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddresses(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        AddressDTO updateAddress = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(updateAddress, HttpStatus.FOUND);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String deletedAddress = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(deletedAddress, HttpStatus.OK);
    }
}
