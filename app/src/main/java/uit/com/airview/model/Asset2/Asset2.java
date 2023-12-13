package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Asset2 {
    @SerializedName("id")
    private String id;
    @SerializedName("createdOn")
    private long createdOn;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("attributes")
    private uit.com.airview.model.Asset2.Attributes attributes;

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
