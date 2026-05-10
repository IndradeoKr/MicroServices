package com.address.controller;

import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/save")
    public ResponseEntity<List<AddressDto>> saveAddress(@RequestBody AddressRequest addressRequest) {
        List<AddressDto> response = addressService.saveAddress(addressRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<List<AddressDto>> updateAddress(@RequestBody AddressRequest addressRequest) {
        List<AddressDto> response = addressService.updateAddress(addressRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/all-address")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> response = addressService.getAllAddresses();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getSingleAddress(@PathVariable Long id) {
        AddressDto response = addressService.getSingleAddress(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/empId/{empId}")
    public ResponseEntity<List<AddressDto>> getAddressByEmpId(@PathVariable Long empId) {
        List<AddressDto> response = addressService.getAddressByEmpId(empId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
