package com.nokia.inms.helper;

public class ApiKey {

        private String apiKey;
        private String apiValue;

        public ApiKey (String apiKey,String apiValue){
                this.apiKey = apiKey;
                this.apiValue = apiValue;
        }

        public String getApiKey () {
                return apiKey;
        }

        public String getApiValue() {
                return apiValue;
        }
}

