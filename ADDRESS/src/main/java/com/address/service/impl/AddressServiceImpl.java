package com.address.service.impl;

import com.address.client.EmployeeClient;
import com.address.exception.ResourceNotFoundException;
import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.model.dto.AddressRequestDto;
import com.address.model.dto.EmployeeDto;
import com.address.model.entity.Address;
import com.address.reprository.AddressRepository;
import com.address.service.AddressService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final EmployeeClient employeeClient;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, EmployeeClient employeeClient) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.employeeClient = employeeClient;
    }

    @Override
    public List<AddressDto> saveAddress(AddressRequest addressRequest) {
        employeeClient.getSingleEmployee(addressRequest.getEmpId());

        List<Address> listToSave = saveOrUpdateAddresses(addressRequest);

        List<Address> savedAddresses = addressRepository.saveAll(listToSave);

        return savedAddresses.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> updateAddress(AddressRequest addressRequest) {
        employeeClient.getSingleEmployee(addressRequest.getEmpId());
        List<Address> addressByEmpId = addressRepository.findAllByEmpId(addressRequest.getEmpId());
        if(addressByEmpId.isEmpty())
        {
            log.info("No addresses found for employee ID: {}", addressRequest.getEmpId());
            log.info("Creating new address for employee ID: {}", addressRequest.getEmpId());
        }

        List<Address> listToUpdate = saveOrUpdateAddresses(addressRequest);

        List<Long> upcomingNonNullIds = listToUpdate.stream().map(Address::getId).filter(Objects::nonNull).toList();
        List<Long> existingIds = addressByEmpId.stream().map(Address::getId).toList();

        List<Long> idsToDelete = existingIds.stream().filter(Id -> !upcomingNonNullIds.contains(Id)).toList();

        if(!idsToDelete.isEmpty())
        {
            addressRepository.deleteAllById(idsToDelete);
        }

        List<Address> updatedAddress = addressRepository.saveAll(listToUpdate);
        return updatedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public AddressDto getSingleAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> all = addressRepository.findAll();
        if(all.isEmpty())
        {
            throw new ResourceNotFoundException("No addresses found");
        }
        return all.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        addressRepository.delete(address);
    }

    @Override
    public List<AddressDto> getAddressByEmpId(Long empId) {
        List<Address> addressByEmpId = addressRepository.findAllByEmpId(empId);
        if(addressByEmpId.isEmpty()){
            throw new ResourceNotFoundException("No address found for employee id: " + empId);
        }
        return addressByEmpId.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    private List<Address> saveOrUpdateAddresses(AddressRequest addressRequest) {
        List<Address> listToSave = new ArrayList<>();
        for(AddressRequestDto addressRequestDto : addressRequest.getAddressRequestDtoList())
        {
            Address address = new Address();
            address.setStreet(addressRequestDto.getStreet());
            address.setPinCode(addressRequestDto.getPinCode());
            address.setCity(addressRequestDto.getCity());
            address.setCountry(addressRequestDto.getCountry());
            address.setAddressType(addressRequestDto.getAddressType());
            address.setEmpId(addressRequest.getEmpId());
            address.setId(addressRequestDto.getId() != null ? addressRequestDto.getId() : null);
            listToSave.add(address);
        }
        return listToSave;
    }
}
