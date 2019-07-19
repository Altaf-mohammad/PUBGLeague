package com.league.pubgleague;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class BattleListId {
    @Exclude
    public String BattleListId;

    public <T extends BattleListId> T withId(@NonNull final String id) {
        this.BattleListId = id;
        return (T) this;
    }

}
