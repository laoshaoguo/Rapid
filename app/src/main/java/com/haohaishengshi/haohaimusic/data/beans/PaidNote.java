package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PaidNote implements Serializable, Parcelable {
        private static final long serialVersionUID = 1234L;
        /**
         * paid : true
         * node : 9
         * amount : 20
         */

        private boolean paid;
        private int node;
        private int amount;

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public int getNode() {
            return node;
        }

        public void setNode(int node) {
            this.node = node;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
        dest.writeInt(this.node);
        dest.writeInt(this.amount);
    }

    public PaidNote() {
    }

    protected PaidNote(Parcel in) {
        this.paid = in.readByte() != 0;
        this.node = in.readInt();
        this.amount = in.readInt();
    }

    public static final Creator<PaidNote> CREATOR = new Creator<PaidNote>() {
        @Override
        public PaidNote createFromParcel(Parcel source) {
            return new PaidNote(source);
        }

        @Override
        public PaidNote[] newArray(int size) {
            return new PaidNote[size];
        }
    };
}