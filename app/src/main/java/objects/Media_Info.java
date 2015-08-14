package objects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import com.kingkrooks.followup.R;

/**
 * Created by Arjun Bansil on 8/2/2015.
 */
public class Media_Info {
    private String type;
    private String url;
    private Context p;
    private String finalUrl;
    private int color_header;
    private Intent action;
    private EmailValidator emailValidator;

    private static final String phone= "Phone Number";
    private static final String twitter = "Twitter";
    private static final String facebook = "Facebook";
    private static final String email = "Email";

    public Media_Info(String u, Context c){
        this.url = u;
        this.p = c;

        this.emailValidator = new EmailValidator();
        generateType();
        generateClick();
        setHeader();

    }

    public Context getP(){
        return p;
    }

    public String getUrl(){
        return finalUrl;
    }

    private void generateType(){
        if(url.contains("@") && !emailValidator.validate(url)){
            this.type = twitter;
            url = url.replace("@","");
            finalUrl = "http://twitter.com/" + url;
        }else if(url.contains("facebook")){
            this.type = facebook;
            finalUrl = url;
        }else if(emailValidator.validate(url)){
            this.type = email;
            finalUrl = url;
        }else{
            this.type = phone;
            finalUrl = url;
        }
    }

    private void generateClick(){
        if(type.equals(phone)){
            action = new Intent(Intent.ACTION_CALL);
            action.setData(Uri.parse("tel:"+finalUrl));
        }else{
            action = new Intent(Intent.ACTION_VIEW);
            action.setData(Uri.parse(finalUrl));
        }
    }

    public Intent getAction(){
        return action;
    }

    public void setHeader(){
        if(this.type.equals("Facebook")){
            this.color_header = p.getResources().getColor(R.color.facebook);
        }else if(this.type.equals("Twitter")){
            this.color_header = p.getResources().getColor(R.color.twitter);
        }else if(this.type.equals("Email")){
            this.color_header = p.getResources().getColor(R.color.email);
        }else{
            this.color_header = p.getResources().getColor(R.color.colorPrimary);
        }
    }

    public int getColor(){
        return color_header;
    }

    public String returnType(){
        return type;
    }


}
