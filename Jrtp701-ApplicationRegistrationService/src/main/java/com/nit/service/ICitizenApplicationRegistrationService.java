package com.nit.service;

import com.nit.bindings.CitizenAppRegistrationInputs;
import com.nit.exceptions.InvalidSSNException;

public interface ICitizenApplicationRegistrationService {
    public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs)throws InvalidSSNException;
}
