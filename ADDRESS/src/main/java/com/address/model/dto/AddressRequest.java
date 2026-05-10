package com.address.model.dto;

import java.util.List;

public class AddressRequest {
    private Long empId;
    private List<AddressRequestDto> addressRequestDtoList;

    public AddressRequest(Long empId, List<AddressRequestDto> addressRequestDtoList) {
        this.empId = empId;
        this.addressRequestDtoList = addressRequestDtoList;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public List<AddressRequestDto> getAddressRequestDtoList() {
        return addressRequestDtoList;
    }

    public void setAddressRequestDtoList(List<AddressRequestDto> addressRequestDtoList) {
        this.addressRequestDtoList = addressRequestDtoList;
    }

    @Override
    public String toString() {
        return "AddressRequest{" + "empId=" + empId + ", addressRequestDtoList=" + addressRequestDtoList + '}';
    }
}
