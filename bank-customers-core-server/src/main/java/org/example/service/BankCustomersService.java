package org.example.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.example.BankCustomer;
import org.example.dao.IDao;
import org.springframework.stereotype.Service;

@Service
public class BankCustomersService {
    private IDao iDao;
    private MeterRegistry meterRegistry;
    private Counter missedCustomer;

  public BankCustomersService(IDao iDao, MeterRegistry meterRegistry) {
    this.iDao = iDao;
    this.meterRegistry = meterRegistry;
    this.missedCustomer = meterRegistry.counter("missed.customers");
  }

    public void createCustomer(BankCustomer bankCustomer) {
        iDao.createCustomer(bankCustomer);
    }

    public BankCustomer getCustomerDetail(String bankCustomerName) {
      BankCustomer bankCustomer = iDao.getCustomerDetail(bankCustomerName);
      if (bankCustomer == null) {
        missedCustomer.increment();
      }
      return
          bankCustomer;
    }

    public void removeCustomer(String bankCustomerName) {
        iDao.removeCustomerDetail(bankCustomerName);
    }

    public void updateCustomer(String bankCustomerName, BankCustomer bankCustomerUpdated) {
        iDao.updateCustomerDetail(bankCustomerName, bankCustomerUpdated);
    }

}
