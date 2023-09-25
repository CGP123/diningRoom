package com.example.diningroom.client;

import java.util.List;

public class PassData {
    private List<EmployeeData> employeeData;

    public List<EmployeeData> getEmployeeData() {
        return employeeData;
    }

    public class EmployeeData{
        private String Surname;
        private String Name;
        private String Patronymic;
        private String PassNumber;
        private String Organization;
        private String Active;
        private String ValidUntil;
        private String PhotoPath;
        private String PropuskGuid;

        public String getSurname() {
            return Surname;
        }

        public String getName() {
            return Name;
        }

        public String getPatronymic() {
            return Patronymic;
        }

        public String getPassNumber() {
            return PassNumber;
        }

        public String getOrganization() {
            return Organization;
        }

        public String getActive() {
            return Active;
        }

        public String getValidUntil() {
            return ValidUntil;
        }

        public String getPhotoPath() {
            return PhotoPath;
        }

        public String getPropuskGuid() {
            return PropuskGuid;
        }
    }

}
