package com.nit.service;

import com.nit.binding.ElgibilityDetailsOutput;

public interface IElgibilityDeterminationMgmtService {
   public ElgibilityDetailsOutput    determineElgibility(int caseNo);
}
