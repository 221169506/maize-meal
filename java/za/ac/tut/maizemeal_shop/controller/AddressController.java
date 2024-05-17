package za.ac.tut.maizemeal_shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.tut.maizemeal_shop.model.Address;
import za.ac.tut.maizemeal_shop.repository.AddressRepository;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    // Find address by email
    @GetMapping("/find")
    public ResponseEntity<Address> findAddressByEmail(@RequestParam String email) {
        Address address = addressRepository.findAddressByEmail(email);
        if (address != null) {
            return new ResponseEntity<>(address, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // List all addresses by email
    @GetMapping("/all")
    public ResponseEntity<List<Address>> findAllAddressesByEmail(@RequestParam String email) {
        List<Address> addresses = addressRepository.findAllByEmail(email);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    // Add a new address
    @PostMapping("/add")
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        Address savedAddress = addressRepository.save(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    // Delete an address
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update an address
    @PutMapping("/update/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
        Address address = addressRepository.findById(id).orElse(null);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        address.setHouseNumber(addressDetails.getHouseNumber());
        address.setStreetName(addressDetails.getStreetName());
        address.setCity(addressDetails.getCity());
        address.setPostalCode(addressDetails.getPostalCode());
        address.setProvince(addressDetails.getProvince());
        Address updatedAddress = addressRepository.save(address);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
