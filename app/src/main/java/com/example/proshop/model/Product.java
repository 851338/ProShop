package com.example.proshop.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    String id;
    String name;
    String CPU;
    String image;
    String RAM;
    String disk;
    String gCard;
    Float price;
    int quantity;
    String seller;

    public Product(String id, String name, String CPU, String image, String RAM, String disk, String gCard, Float price, int quantity, String seller) {
        this.id = id;
        this.name = name;
        this.CPU = CPU;
        this.image = image;
        this.RAM = RAM;
        this.disk = disk;
        this.gCard = gCard;
        this.price = price;
        this.quantity = quantity;
        this.seller = seller;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        CPU = in.readString();
        image = in.readString();
        RAM = in.readString();
        disk = in.readString();
        gCard = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        quantity = in.readInt();
        seller = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(CPU);
        dest.writeString(image);
        dest.writeString(RAM);
        dest.writeString(disk);
        dest.writeString(gCard);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeInt(quantity);
        dest.writeString(seller);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getgCard() {
        return gCard;
    }

    public void setgCard(String gCard) {
        this.gCard = gCard;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public static Creator<Product> getCREATOR() {
        return CREATOR;
    }
}

