package com.example.myapplication.loveyeqin99;

class UpLoadBean {
    private int code;
    private Data data;
    private String message;

    public UpLoadBean(int code, Data data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public UpLoadBean() {
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data{
        private int recognition_result;
        private float face_distances;

        public Data(int recognition_result, float face_distances) {
            this.recognition_result = recognition_result;
            this.face_distances = face_distances;
        }

        public int getRecognition_result() {
            return recognition_result;
        }

        public void setRecognition_result(int recognition_result) {
            this.recognition_result = recognition_result;
        }

        public float getFace_distances() {
            return face_distances;
        }

        public void setFace_distances(float face_distances) {
            this.face_distances = face_distances;
        }
    }
}
