package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Asset2 {
    @SerializedName("id")
    private String id;
    @SerializedName("version")
    private String version;
    @SerializedName("createdOn")
    private long createdOn;
    @SerializedName("name")
    private String name;
    @SerializedName("attributes")
    private Attributes attributes;


//    @SerializedName("attributes")
//    public Object attributes;
    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return attributes;
    }
}
