package com.projectbox.uploadfile.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailsModel {

    /**
     * header : Insurance Company Details
     * questions : [{"hint":"Customer Name","type":"text","comment":"Input type is text"},{"hint":"Customer Phone","type":"textNumeric","validation":{"size":10},"comment":"Input type is text/Numeric and apply the validation when the user leaves the field"},{"hint":"Customer Address","type":"text","comment":"Input type is text"},{"hint":"ZipCode","type":"textNumeric","validation":{"size":6},"comment":"Input type is text and apply the validation when the user leaves the field"}]
     */

    private String header;
    private List<QuestionsBean> questions;

    public static List<DetailsModel> arrayDetailsModelFromData(String str) {

        Type listType = new com.google.gson.reflect.TypeToken<ArrayList<DetailsModel>>() {
        }.getType();

        return new com.google.gson.Gson().fromJson(str, listType);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<QuestionsBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsBean> questions) {
        this.questions = questions;
    }

    public static class QuestionsBean {
        /**
         * hint : Customer Name
         * type : text
         * comment : Input type is text
         * validation : {"size":10}
         */

        private String hint;
        private String type;
        private String comment;
        private ValidationBean validation;

        public static List<QuestionsBean> arrayQuestionsBeanFromData(String str) {

            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<QuestionsBean>>() {
            }.getType();

            return new com.google.gson.Gson().fromJson(str, listType);
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public ValidationBean getValidation() {
            return validation;
        }

        public void setValidation(ValidationBean validation) {
            this.validation = validation;
        }

        public static class ValidationBean {
            /**
             * size : 10
             */

            private int size;

            public static List<ValidationBean> arrayValidationBeanFromData(String str) {

                Type listType = new com.google.gson.reflect.TypeToken<ArrayList<ValidationBean>>() {
                }.getType();

                return new com.google.gson.Gson().fromJson(str, listType);
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }
        }
    }
}
