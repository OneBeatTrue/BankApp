package entities.customer;

import entities.customer.builder.*;
import exceptions.EmptyStringException;
import exceptions.NotPositiveException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

public class Customer implements ICustomer{
    private Customer(long phone, String name, String surname, String address, int passportSeries, int passportNumber){
        this.phone = phone;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.passportNumber = passportNumber;
        this.passportSeries = passportSeries;
        this.notifications = new ArrayList<>();
    }

    private long phone;
    private String name;
    private String surname;
    private String address;
    private int passportSeries;
    private int passportNumber;
    @Getter
    private Collection<String> notifications;

    @Override
    public long getPhone() { return phone; }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public boolean isVerified() { return address != null && !address.isEmpty() && passportSeries != 0 && passportNumber != 0; }

    @Override
    public void updatePassport(int series, int number) throws NotPositiveException {
        if (series < 0) {
            throw new NotPositiveException("Passport series");
        }

        if (number < 0) {
            throw new NotPositiveException("Passport number");
        }

        passportSeries = series;
        passportNumber = number;
    }

    @Override
    public void updateAddress(String address) {
        this.address = address;
    }

    @Override
    public void Notify(String message) {
        notifications.add(message);
    }

    /**
     * @return Interface driven builder for step-by-step creation
     */
    public static ICustomerPhoneBuilder builder() {
        return new CustomerBuilder();
    }

    private static class CustomerBuilder implements
            ICustomerPhoneBuilder,
            ICustomerNameBuilder,
            ICustomerSurnameBuilder,
            ICustomerPassportNumberBuilder,
            ICustomerBuilder {

        private CustomerBuilder() {};
        private long Phone;
        private String Name;
        private String Surname;
        private String Address;
        private int PassportSeries;
        private int PassportNumber;

        @Override
        public ICustomer build() { return new Customer(Phone, Name, Surname, Address, PassportSeries, PassportNumber); }

        @Override
        public ICustomerBuilder setAddress(String address) throws EmptyStringException {
            if (address.isEmpty()) {
                throw new EmptyStringException("Address");
            }

            Address = address;
            return this;
        }

        @Override
        public ICustomerPassportNumberBuilder setPassportSeries(int series) throws NotPositiveException {
            if (series < 0) {
                throw new NotPositiveException("Passport series");
            }

            PassportSeries = series;
            return this;
        }

        @Override
        public ICustomerSurnameBuilder setName(String name) throws EmptyStringException {
            if (name.isEmpty()) {
                throw new EmptyStringException("Name");
            }

            Name = name;
            return this;
        }

        @Override
        public ICustomerNameBuilder setPhone(long phone) throws NotPositiveException {
            if (phone < 0) {
                throw new NotPositiveException("Phone");
            }

            Phone = phone;
            return this;
        }

        @Override
        public ICustomerBuilder setPassportNumber(int number) throws NotPositiveException {
            if (number < 0) {
                throw new NotPositiveException("Passport number");
            }

            PassportNumber = number;
            return this;
        }

        @Override
        public ICustomerBuilder setSurname(String surname) throws EmptyStringException {
            if (surname.isEmpty()) {
                throw new EmptyStringException("Surname");
            }

            Surname = surname;
            return this;
        }
    }
}
