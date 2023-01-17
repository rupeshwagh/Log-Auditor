package com.rupesh.customeventlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rupesh.customeventlog.model.Alert;

@Repository
public interface AlertRepository extends CrudRepository<Alert, String> {
}
