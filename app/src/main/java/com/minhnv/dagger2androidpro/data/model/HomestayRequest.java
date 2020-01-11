package com.minhnv.dagger2androidpro.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class HomestayRequest {
    public static class ServerDeleteHomeStays {
        @Expose
        @SerializedName("id")
        private Integer id;

        public ServerDeleteHomeStays(Integer id) {
            this.id = id;
        }
    }
}
