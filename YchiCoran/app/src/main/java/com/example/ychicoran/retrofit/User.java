package com.example.ychicoran.retrofit;

import com.google.gson.annotations.SerializedName;

public class User {

        
        private Integer Id_user;
        private String username;
        private Object status;

        public  User(Integer Id_user, String username, Integer status){
            this.Id_user = Id_user;
            this.username = username;
            this.status = status;
        }

        public Integer getId_user() {
            return Id_user;
        }

        public void setId_user(Integer Id_user) {
            this.Id_user = Id_user;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Object getStatus() {
            return status;
        }
        public void setStatus(Object status) {
            this.status = status;
        }

}


