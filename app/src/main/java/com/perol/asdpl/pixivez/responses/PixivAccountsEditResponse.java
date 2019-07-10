package com.perol.asdpl.pixivez.responses;

/**
 * Created by Notsfsssf on 2018/3/24.
 */

public class PixivAccountsEditResponse {

    /**
     * error : false
     * message :
     * body : {"is_succeed":true,"validation_errors":{}}
     */

    private boolean error;
    private String message;
    private BodyBean body;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * is_succeed : true
         * validation_errors : {}
         */

        private boolean is_succeed;
        private ValidationErrorsBean validation_errors;

        public boolean isIs_succeed() {
            return is_succeed;
        }

        public void setIs_succeed(boolean is_succeed) {
            this.is_succeed = is_succeed;
        }

        public ValidationErrorsBean getValidation_errors() {
            return validation_errors;
        }

        public void setValidation_errors(ValidationErrorsBean validation_errors) {
            this.validation_errors = validation_errors;
        }

        public static class ValidationErrorsBean {
        }
    }
}
