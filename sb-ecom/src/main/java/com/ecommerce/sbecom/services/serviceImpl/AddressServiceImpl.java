package com.ecommerce.sbecom.services.serviceImpl;

import com.ecommerce.sbecom.models.Address;
import com.ecommerce.sbecom.models.User;
import com.ecommerce.sbecom.payload.AddressDTO;
import com.ecommerce.sbecom.repositories.AddressRepository;
import com.ecommerce.sbecom.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }
}
