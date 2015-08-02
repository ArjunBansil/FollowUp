package objects;

/**
 * Created by Arjun Bansil on 8/1/2015.
 */
public class Media_Container {

    private String social_media;
    private String link;
    private int id;

    public Media_Container(){

    }

    public Media_Container(String s_media, String l){
        this.social_media = s_media;
        this.link = l;
    }

    public void setSMedia(String s){
        this.social_media = s;
    }

    public void setLink(String l){
        this.link = l;
    }

    public String getSMedia(){
        return social_media;
    }

    public String getLink(){
        return link;
    }

    public void setId(int i){
        this.id = i;
    }

    public int getId(){
        return id;
    }
}
