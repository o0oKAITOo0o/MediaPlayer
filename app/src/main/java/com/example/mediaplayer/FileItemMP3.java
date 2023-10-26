package com.example.mediaplayer;

import javax.xml.namespace.QName;

public class FileItemMP3 {
    private String DISPLAY_NAME;
    private String DATA;


    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public FileItemMP3(String DISPLAY_NAME_file,String DATA_file){
        DISPLAY_NAME = DISPLAY_NAME_file;
        DATA = DATA_file;
    }
}
