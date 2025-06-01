package com.learnjavawithkaushi.cdpatientmicroservice.repository;

import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {}
