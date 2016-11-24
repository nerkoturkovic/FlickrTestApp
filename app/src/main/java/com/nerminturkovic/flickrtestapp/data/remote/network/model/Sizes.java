
package com.nerminturkovic.flickrtestapp.data.remote.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Sizes {

    @SerializedName("canblog")
    @Expose
    private Integer canblog;
    @SerializedName("canprint")
    @Expose
    private Integer canprint;
    @SerializedName("candownload")
    @Expose
    private Integer candownload;
    @SerializedName("size")
    @Expose
    private List<Size> size = new ArrayList<Size>();

    /**
     * 
     * @return
     *     The canblog
     */
    public Integer getCanblog() {
        return canblog;
    }

    /**
     * 
     * @param canblog
     *     The canblog
     */
    public void setCanblog(Integer canblog) {
        this.canblog = canblog;
    }

    /**
     * 
     * @return
     *     The canprint
     */
    public Integer getCanprint() {
        return canprint;
    }

    /**
     * 
     * @param canprint
     *     The canprint
     */
    public void setCanprint(Integer canprint) {
        this.canprint = canprint;
    }

    /**
     * 
     * @return
     *     The candownload
     */
    public Integer getCandownload() {
        return candownload;
    }

    /**
     * 
     * @param candownload
     *     The candownload
     */
    public void setCandownload(Integer candownload) {
        this.candownload = candownload;
    }

    /**
     * 
     * @return
     *     The size
     */
    public List<Size> getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(List<Size> size) {
        this.size = size;
    }

}
