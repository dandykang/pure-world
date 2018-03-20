package com.dandykang.learn.brain.base;

import lombok.Data;

import java.io.Serializable;

/**
 * Administrator create on 2017/12/16
 **/
@Data
public class MainDim implements Serializable {

    private String bUuid;
    private String dim;

    public MainDim(String bUuid, String dim) {
        this.bUuid = bUuid;
        this.dim = dim;
    }

    @Override
    public String toString() {
        return bUuid + "_|_" + dim;
    }

    @Override
    public int hashCode() {
        return this.dim.hashCode() + this.bUuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != MainDim.class) {
            return false;
        }
        MainDim mainDim = (MainDim) obj;
        boolean bUuidEquel = false;
        if (mainDim.getBUuid() == null && this.getBUuid() == null) {
            bUuidEquel = true;
        } else if (mainDim.getBUuid() != null && this.getBUuid() != null && mainDim.getBUuid().equals(this.getBUuid())) {
            bUuidEquel = true;
        }
        if (!bUuidEquel) {
            return false;
        }
        if (mainDim.getDim() == null && this.getDim() == null) {
            return true;
        } else if (mainDim.getDim() != null && this.getDim() != null && mainDim.getDim().equals(this.getDim())) {
            return true;
        }
        return false;
    }
}
