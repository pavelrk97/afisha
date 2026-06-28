package com.afisha.main.location;

import org.springframework.data.jpa.repository.JpaRepository;
import com.afisha.main.location.model.Location;

public interface LocationRepository extends JpaRepository<Location,Long> {

}
