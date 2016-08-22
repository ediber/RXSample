package com.example.guest.testapp;

/**
 * Created by Guest on 16/08/2016.
 */
public class UserParent {
       private User user;

    public UserParent(String firstName, String lastName, String email, String city, String phoneNumber) {
        user = new User( firstName,  lastName,  email,  city,  phoneNumber);

    }


    private class User{
        String userType;
        String firstName;
        String lastName;
        String email;
        String city;
        String phoneNumber;
        Float credibility;

        private User(String firstName, String lastName, String email, String city, String phoneNumber) {
            this.userType = "DEFAULT";
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.city = city;
            this.phoneNumber = phoneNumber;
            this.credibility = 20.0f;
        }


    }



}
