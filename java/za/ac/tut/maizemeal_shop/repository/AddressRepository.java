package za.ac.tut.maizemeal_shop.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.tut.maizemeal_shop.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findAddressByEmail(String email);
    List<Address> findAllByEmail(String email);
    
}
