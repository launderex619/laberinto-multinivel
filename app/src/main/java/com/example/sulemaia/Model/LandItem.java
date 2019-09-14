package com.example.sulemaia.Model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

public class LandItem implements Parcelable {
    private Button btnItemMap;
    private int code;
    private boolean isInitial;
    private boolean isFinal;
    private String Name;
    private int color;

    public LandItem(Button btnItemMap, int code) {
        this.btnItemMap = btnItemMap;
        this.code = code;
        isInitial = false;
        isFinal = false;
        Name = "";
        color = 0;
    }

    public LandItem(Parcel source) {
        //btnItemMap =(Button) source.readValue(ClassLoader.getSystemClassLoader());
        code = source.readInt();
        isInitial = (Boolean) source.readValue(ClassLoader.getSystemClassLoader());
        isFinal = (Boolean) source.readValue(ClassLoader.getSystemClassLoader());
        Name = source.readString();
        color= source.readInt();
    }

    public Button getBtnItemMap() {
        return btnItemMap;
    }

    public void setBtnItemMap(Button btnItemMap) {
        this.btnItemMap = btnItemMap;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getColor() {
        return color;
    }

    public int getCode() {
        return code;
    }

    public void setColor(int color) {
        this.color = color;
        btnItemMap.setBackgroundColor(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       // dest.writeValue(btnItemMap);
        dest.writeInt(code);
        dest.writeValue(isInitial);
        dest.writeValue(isFinal);
        dest.writeString(Name);
        dest.writeInt(color);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new LandItem(source);
        }

        @Override
        public LandItem[] newArray(int size) {
            return new LandItem[size];
        }
    };
}
