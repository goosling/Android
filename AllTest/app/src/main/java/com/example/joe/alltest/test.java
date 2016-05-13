package com.example.joe.alltest;

import java.util.List;

/**
 * Created by JOE on 2016/3/14.
 */
public class test {

     /**
     * firstName : Bill
     * lastName : Gates
     */

    private List<EmployeesEntity> employees;

    public void setEmployees(List<EmployeesEntity> employees) {
        this.employees = employees;
    }

    public List<EmployeesEntity> getEmployees() {
        return employees;
    }

    public static class EmployeesEntity {
        private String firstName;
        private String lastName;

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}
