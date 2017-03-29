package it.polito.group05.group05.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.text.SpannableString;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.nekocode.badge.BadgeDrawable;
import it.polito.group05.group05.R;

/**
 * Created by Marco on 24/03/2017.
 */

public class Group {

    private String name;
    private String balance;
    private BadgeDrawable badge;
    private String lmTime;
    private String groupProfile;
    private int groupColor;
    private List<User> members;

    public Group(String groupName, String currentBalance, String groupProfile, String lmTime, BadgeDrawable badge) {
        this.name = groupName;
        this.balance = currentBalance;
        this.groupProfile = groupProfile;
        this.lmTime = lmTime;
        this.badge = badge;
    }

    public Group() {
        this.members = new ArrayList<>();
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(String Uri) {
        groupProfile = Uri;

    }

    public SpannableString getBadge() {
        if(badge != null)
            return badge.toSpannable();
        return null;
    }

    public void setBadge(int count) {
        if(count == 0)
            return;
             badge = new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_NUMBER)
                        .badgeColor(Color.parseColor("#FFC107"))
                        .textSize(40)
                        .number(count)
                        .build();

    }

    public String getLmTime() {
        return lmTime;
    }

    public void setLmTime(String lmTime) {
        this.lmTime = lmTime;
    }

    public int getGroupColor() {
        return groupColor;
    }

    public void setGroupColor(int groupColor) {
        this.groupColor = groupColor;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User u) {
        members.add(u);
    }

    public User getMember(String id) {
        for (User u :
                members) {
            if(u.getId() == id) {
                return u;
            }

        }
        return null;
    }
}
