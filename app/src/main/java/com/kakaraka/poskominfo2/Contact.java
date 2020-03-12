package com.kakaraka.poskominfo2;



public class Contact {
    String mName;
    String mNoizin;
    String mKodepos;
    String mMasaberlaku;


    public Contact(String nama, String noizin, String kodepos, String masaberlaku) {
        mName = nama;
        mNoizin = noizin;
        mKodepos = kodepos;
        mMasaberlaku = masaberlaku;

    }

    public String getName() {
        return mName;
    }

    public String getNoizin() {
        return mNoizin;
    }

    public String getKodepos() {
        return mKodepos;
    }

    public String getMasaberlaku() {
        return mMasaberlaku;
    }

}